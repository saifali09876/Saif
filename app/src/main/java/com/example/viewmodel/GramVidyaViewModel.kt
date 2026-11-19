package com.example.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.HybridAiService
import com.example.ai.OfflineRagEngine
import com.example.ai.TutorResponse
import com.example.data.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class StudentProfile(
    val name: String = "Aarav",
    val educationLevel: String = "Class 10",
    val language: String = "English",
    val forceOfflineMode: Boolean = false,
    val lowEndDeviceMode: Boolean = false,
    val role: String = "Student" // "Student", "Teacher", "Admin"
)

data class TutorUiState(
    val messages: List<TutorResponse> = emptyList(),
    val isLoading: Boolean = false,
    val pendingQuestion: String? = null,
    val streamingText: String = "",
    val errorMessage: String? = null,
    val selectedTextbook: String = "All Downloaded Books",
    val selectedChapter: String = "All Chapters",
    val selectedModel: com.example.ai.GeminiModelOption = com.example.ai.GeminiModelOption.GENERAL
)

class GramVidyaViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getInstance(application).dao()
    private val aiService = HybridAiService()

    private val _profile = MutableStateFlow(StudentProfile())
    val profile: StateFlow<StudentProfile> = _profile.asStateFlow()

    private val _tutorState = MutableStateFlow(TutorUiState())
    val tutorState: StateFlow<TutorUiState> = _tutorState.asStateFlow()

    private val _quizGenStatus = MutableStateFlow<String?>(null)
    val quizGenStatus: StateFlow<String?> = _quizGenStatus.asStateFlow()

    val passages: StateFlow<List<TextbookPassage>> = dao.getAllPassages()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizQuestions: StateFlow<List<QuizQuestionEntity>> = dao.getAllQuizQuestions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val quizAttempts: StateFlow<List<QuizAttemptEntity>> = dao.getAllQuizAttempts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val studyPlans: StateFlow<List<StudyPlanEntity>> = dao.getAllStudyPlans()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notes: StateFlow<List<StudyNoteEntity>> = dao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val dictionary: StateFlow<List<DictionaryEntryEntity>> = dao.getAllDictionaryEntries()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flashcards: StateFlow<List<FlashcardEntity>> = dao.getAllFlashcards()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val formulas: StateFlow<List<FormulaEntity>> = dao.getAllFormulas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            if (dao.getPassageCount() == 0) {
                dao.insertPassages(SeedData.initialPassages)
                dao.insertQuizQuestions(SeedData.initialQuizQuestions)
                dao.insertDictionaryEntries(SeedData.initialDictionary)
                dao.insertStudyPlans(SeedData.initialStudyPlans)
                dao.insertFlashcards(SeedData.initialFlashcards)
                dao.insertFormulas(SeedData.initialFormulas)
            }
        }
    }

    fun isNetworkAvailable(): Boolean {
        val cm = getApplication<Application>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun updateProfile(transform: (StudentProfile) -> StudentProfile) {
        _profile.update(transform)
    }

    fun setTextbookFilter(textbook: String, chapter: String = "All Chapters") {
        _tutorState.update { it.copy(selectedTextbook = textbook, selectedChapter = chapter) }
    }

    fun selectGeminiModel(model: com.example.ai.GeminiModelOption) {
        _tutorState.update { it.copy(selectedModel = model) }
    }

    fun clearChatHistory() {
        aiService.resetChat()
        _tutorState.update { it.copy(messages = emptyList(), errorMessage = null, streamingText = "", pendingQuestion = null) }
    }

    fun askTutor(question: String) {
        if (question.isBlank()) return
        viewModelScope.launch {
            _tutorState.update {
                it.copy(isLoading = true, pendingQuestion = question, streamingText = "", errorMessage = null)
            }
            val corpus = dao.getDownloadedPassagesSync()
            val state = _tutorState.value
            val prof = _profile.value

            val topResult = OfflineRagEngine.retrieveTopPassages(
                query = question,
                corpus = corpus,
                selectedTextbook = state.selectedTextbook,
                selectedChapter = state.selectedChapter
            ).firstOrNull()

            val useGeminiOnline = !prof.forceOfflineMode && isNetworkAvailable()
            if (!useGeminiOnline) {
                val offlineResp = OfflineRagEngine.buildOfflineStructuredAnswer(
                    query = question,
                    topResult = topResult,
                    educationLevel = prof.educationLevel,
                    language = prof.language
                )
                _tutorState.update {
                    it.copy(
                        isLoading = false,
                        pendingQuestion = null,
                        messages = it.messages + offlineResp
                    )
                }
            } else {
                try {
                    val aiText = aiService.sendMultiTurnChatMessage(
                        userMessage = question,
                        retrieved = topResult,
                        modelOption = state.selectedModel,
                        educationLevel = prof.educationLevel,
                        language = prof.language
                    ) { chunk ->
                        _tutorState.update { it.copy(streamingText = chunk) }
                    }
                    val p = topResult?.takeIf { it.score >= 0.14 }?.passage
                    val onlineResp = TutorResponse(
                        question = question,
                        answerText = aiText,
                        sourceTextbook = p?.textbookName,
                        sourceChapter = p?.chapterName,
                        sourcePage = p?.pageNumber,
                        supportingPassage = p?.content,
                        confidencePercent = topResult?.confidencePercent ?: 95,
                        modeLabel = "${state.selectedModel.label} • ${prof.language}",
                        isReliable = true
                    )
                    _tutorState.update {
                        it.copy(
                            isLoading = false,
                            pendingQuestion = null,
                            streamingText = "",
                            messages = it.messages + onlineResp
                        )
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    val offlineResp = OfflineRagEngine.buildOfflineStructuredAnswer(
                        query = question,
                        topResult = topResult,
                        educationLevel = prof.educationLevel,
                        language = prof.language
                    ).copy(modeLabel = "Offline RAG Fallback")
                    _tutorState.update {
                        it.copy(
                            isLoading = false,
                            pendingQuestion = null,
                            streamingText = "",
                            errorMessage = "Gemini AI unavailable (${e.localizedMessage ?: "network error"}). Answered using Offline Textbook RAG.",
                            messages = it.messages + offlineResp
                        )
                    }
                }
            }
        }
    }

    fun solveCameraQuestion(bitmap: Bitmap) {
        viewModelScope.launch {
            val prof = _profile.value
            if (prof.forceOfflineMode || !isNetworkAvailable()) {
                _tutorState.update {
                    it.copy(errorMessage = "Camera Vision OCR requires an internet connection. Turn off Offline-Only mode or type your question for Offline RAG.")
                }
                return@launch
            }
            _tutorState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val corpus = dao.getDownloadedPassagesSync()
                val resp = aiService.solveQuestionFromImage(
                    bitmap = bitmap,
                    educationLevel = prof.educationLevel,
                    language = prof.language,
                    downloadedCorpus = corpus
                )
                _tutorState.update {
                    it.copy(isLoading = false, messages = listOf(resp) + it.messages)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _tutorState.update {
                    it.copy(isLoading = false, errorMessage = "Photo analysis failed: ${e.localizedMessage ?: "Please try again."}")
                }
            }
        }
    }

    fun generateAiQuizForPassage(passage: TextbookPassage, difficulty: String) {
        viewModelScope.launch {
            if (_profile.value.forceOfflineMode || !isNetworkAvailable()) {
                _quizGenStatus.value = "Offline mode active — practicing with downloaded local MCQs."
                return@launch
            }
            _quizGenStatus.value = "Generating fresh $difficulty MCQs with Gemini AI..."
            try {
                val newQs = aiService.generateChapterQuiz(passage, difficulty, 3)
                dao.insertQuizQuestions(newQs)
                _quizGenStatus.value = "Added ${newQs.size} new AI questions to ${passage.chapterName}!"
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _quizGenStatus.value = "Could not generate AI quiz: ${e.localizedMessage ?: "error"}"
            }
        }
    }

    fun clearQuizGenStatus() {
        _quizGenStatus.value = null
    }

    // Room Mutations
    fun toggleTextbookDownload(textbookName: String, downloaded: Boolean) = viewModelScope.launch {
        dao.setTextbookDownloaded(textbookName, downloaded)
    }

    fun togglePassageCompleted(id: Int, completed: Boolean) = viewModelScope.launch {
        dao.setPassageCompleted(id, completed)
    }

    fun addTeacherPassage(
        textbookName: String,
        level: String,
        subject: String,
        chapterName: String,
        page: Int,
        content: String,
        example: String,
        formula: String
    ) = viewModelScope.launch {
        dao.insertPassage(
            TextbookPassage(
                textbookName = textbookName,
                educationLevel = level,
                subject = subject,
                chapterName = chapterName,
                pageNumber = page,
                content = content,
                realLifeExample = example,
                formulaOrEquation = formula,
                isDownloaded = true
            )
        )
    }

    fun saveQuizAttempt(subject: String, chapter: String, score: Int, total: Int, durationSec: Int) = viewModelScope.launch {
        dao.insertQuizAttempt(
            QuizAttemptEntity(
                subject = subject,
                chapterName = chapter,
                score = score,
                totalQuestions = total,
                durationSeconds = durationSec
            )
        )
    }

    fun addStudyPlan(title: String, subject: String, targetDate: String, isExam: Boolean) = viewModelScope.launch {
        dao.insertStudyPlan(StudyPlanEntity(title = title, subject = subject, targetDate = targetDate, isExam = isExam))
    }

    fun toggleStudyPlan(id: Int, completed: Boolean) = viewModelScope.launch {
        dao.setStudyPlanCompleted(id, completed)
    }

    fun deleteStudyPlan(id: Int) = viewModelScope.launch {
        dao.deleteStudyPlan(id)
    }

    fun saveNote(title: String, subject: String, content: String, isBookmark: Boolean = false, citation: String = "") = viewModelScope.launch {
        dao.insertNote(
            StudyNoteEntity(
                title = title,
                subject = subject,
                content = content,
                isBookmark = isBookmark,
                sourceCitation = citation
            )
        )
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun toggleDictionaryFavorite(id: Int, fav: Boolean) = viewModelScope.launch {
        dao.setDictionaryFavorite(id, fav)
    }

    fun toggleFlashcardMastered(id: Int, mastered: Boolean) = viewModelScope.launch {
        dao.setFlashcardMastered(id, mastered)
    }
}
