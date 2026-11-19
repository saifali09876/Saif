package com.example.ai

import android.graphics.Bitmap
import android.util.Log
import com.example.data.QuizQuestionEntity
import com.example.data.TextbookPassage
import com.google.firebase.Firebase
import com.google.firebase.ai.Chat
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.Schema
import com.google.firebase.ai.type.content
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import org.json.JSONObject

private const val TAG = "HybridAiService"

enum class GeminiModelOption(val modelId: String, val label: String, val subtitle: String) {
    GENERAL("gemini-4-hydrogen", "Gemini 4 Hydrogen", "General Tutor"),
    DEEP_REASONING("gemini-4-carbon", "Gemini 4 Carbon", "Deep Reasoning"),
    INTERACTIVE("gemini-4-neon", "Gemini 4 Neon", "Complex Interactive"),
    FAST("gemini-3.1-flash-lite", "Gemini 3.1 Flash-Lite", "Fast & Lightweight")
}

class HybridAiService {
    private var activeChat: Chat? = null
    private var activeChatKey: String? = null

    private val visionModel by lazy {
        Firebase.ai.generativeModel("gemini-4-hydrogen")
    }

    private val quizModel by lazy {
        val quizSchema = Schema.obj(
            properties = mapOf(
                "questions" to Schema.array(
                    items = Schema.obj(
                        properties = mapOf(
                            "question" to Schema.string(description = "Clear MCQ question"),
                            "optionA" to Schema.string(),
                            "optionB" to Schema.string(),
                            "optionC" to Schema.string(),
                            "optionD" to Schema.string(),
                            "correctIndex" to Schema.integer(description = "0 for A, 1 for B, 2 for C, 3 for D"),
                            "explanation" to Schema.string(description = "Step-by-step explanation of why the answer is correct")
                        )
                    )
                )
            )
        )
        Firebase.ai.generativeModel(
            modelName = "gemini-3.1-flash-lite",
            generationConfig = generationConfig {
                responseMimeType = "application/json"
                responseSchema = quizSchema
            }
        )
    }

    fun resetChat() {
        activeChat = null
        activeChatKey = null
    }

    private fun getOrCreateChat(
        modelOption: GeminiModelOption,
        educationLevel: String,
        language: String
    ): Chat {
        val key = "${modelOption.modelId}|$educationLevel|$language"
        if (activeChat == null || activeChatKey != key) {
            val systemPrompt = """
                You are GramVidya, a warm, patient Gemini AI tutor for students in rural India.
                Student Education Level: $educationLevel
                Preferred Language: $language (respond clearly in $language; keep standard scientific terms and formulas readable).
                Role & Guidelines:
                - Maintain multi-turn conversation context and answer follow-up questions naturally.
                - Explain concepts step-by-step using simple language and everyday rural/real-life examples.
                - When verified textbook context is provided in the user message, prioritize it as your primary source and cite the textbook/chapter.
                - End explanations with a short check-in or practice question to encourage active learning.
            """.trimIndent()

            val model = Firebase.ai.generativeModel(
                modelName = modelOption.modelId,
                systemInstruction = content { text(systemPrompt) }
            )
            activeChat = model.startChat()
            activeChatKey = key
        }
        return activeChat!!
    }

    suspend fun sendMultiTurnChatMessage(
        userMessage: String,
        retrieved: RagRetrievalResult?,
        modelOption: GeminiModelOption,
        educationLevel: String,
        language: String,
        onStreamChunk: (String) -> Unit
    ): String {
        val chat = getOrCreateChat(modelOption, educationLevel, language)
        val enrichedPrompt = if (retrieved != null && retrieved.score >= 0.14) {
            val p = retrieved.passage
            """
                [Verified Textbook Context — ${p.textbookName}, Chapter: ${p.chapterName}, Page ${p.pageNumber}]
                Passage: ${p.content}
                Example: ${p.realLifeExample}
                Formula: ${p.formulaOrEquation}

                Student Message: $userMessage
            """.trimIndent()
        } else {
            userMessage
        }

        val sb = StringBuilder()
        try {
            withTimeout(60_000L) {
                chat.sendMessageStream(enrichedPrompt).collect { chunk ->
                    val t = chunk.text.orEmpty()
                    sb.append(t)
                    onStreamChunk(sb.toString())
                }
            }
            return sb.toString().ifBlank {
                throw IllegalStateException("Gemini returned an empty response.")
            }
        } catch (e: TimeoutCancellationException) {
            Log.e(TAG, "Gemini chat streaming timed out", e)
            throw e
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Gemini chat generation failed", e)
            throw e
        }
    }

    suspend fun solveQuestionFromImage(
        bitmap: Bitmap,
        educationLevel: String,
        language: String,
        downloadedCorpus: List<TextbookPassage>
    ): TutorResponse {
        val prompt = """
            You are GramVidya, a Gemini AI textbook question scanner and doubt solver.
            Education Level: $educationLevel
            Response Language: $language

            Look at this photograph of a textbook question or equation:
            1. Transcribe the exact question or mathematical equation you see.
            2. Identify the Subject and Topic.
            3. Teach the student how to solve it step-by-step (not just the final answer) in $language.
            4. Give a simple real-life example and a follow-up check question.
        """.trimIndent()

        try {
            val response = withTimeout(60_000L) {
                visionModel.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )
            }
            val text = response.text ?: throw IllegalStateException("Empty response from Gemini Vision.")
            val ragMatch = OfflineRagEngine.retrieveTopPassages(text.take(200), downloadedCorpus).firstOrNull()
            return TutorResponse(
                question = "[Scanned Textbook Photo]",
                answerText = text,
                sourceTextbook = ragMatch?.passage?.textbookName,
                sourceChapter = ragMatch?.passage?.chapterName,
                sourcePage = ragMatch?.passage?.pageNumber,
                supportingPassage = ragMatch?.passage?.content,
                confidencePercent = ragMatch?.confidencePercent ?: 90,
                modeLabel = "Gemini Vision + Textbook RAG",
                isReliable = true
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "Camera question solver failed", e)
            throw e
        }
    }

    suspend fun generateChapterQuiz(
        passage: TextbookPassage,
        difficulty: String,
        count: Int = 3
    ): List<QuizQuestionEntity> {
        val prompt = """
            Generate $count multiple-choice questions ($difficulty difficulty) strictly based on this textbook chapter:
            Textbook: ${passage.textbookName}
            Subject: ${passage.subject}
            Chapter: ${passage.chapterName}
            Content: ${passage.content}
            Formula: ${passage.formulaOrEquation}
            Include a clear step-by-step explanation for why the correct answer is right.
        """.trimIndent()

        try {
            val response = withTimeout(60_000L) {
                quizModel.generateContent(prompt)
            }
            val jsonString = response.text ?: throw IllegalStateException("Empty quiz JSON.")
            val root = JSONObject(jsonString)
            val arr = root.getJSONArray("questions")
            val result = mutableListOf<QuizQuestionEntity>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                result.add(
                    QuizQuestionEntity(
                        subject = passage.subject,
                        chapterName = passage.chapterName,
                        difficulty = difficulty,
                        question = obj.getString("question"),
                        optionA = obj.getString("optionA"),
                        optionB = obj.getString("optionB"),
                        optionC = obj.getString("optionC"),
                        optionD = obj.getString("optionD"),
                        correctIndex = obj.getInt("correctIndex").coerceIn(0, 3),
                        explanation = obj.getString("explanation")
                    )
                )
            }
            return result
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e(TAG, "AI Quiz generation failed", e)
            throw e
        }
    }
}
