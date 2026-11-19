package com.example.ai

import com.example.data.TextbookPassage
import kotlin.math.ln
import kotlin.math.sqrt

data class RagRetrievalResult(
    val passage: TextbookPassage,
    val score: Double,
    val confidencePercent: Int
)

data class TutorResponse(
    val question: String,
    val answerText: String,
    val sourceTextbook: String? = null,
    val sourceChapter: String? = null,
    val sourcePage: Int? = null,
    val supportingPassage: String? = null,
    val confidencePercent: Int = 0,
    val modeLabel: String = "Offline RAG",
    val isReliable: Boolean = true
)

object OfflineRagEngine {
    const val UNRELIABLE_MESSAGE =
        "I couldn't find a sufficiently reliable answer in your downloaded study materials. Please select another chapter or add the relevant textbook."

    private val stopWords = setOf(
        "what", "is", "are", "the", "a", "an", "of", "in", "on", "to", "for", "and", "or",
        "how", "why", "when", "where", "explain", "define", "tell", "me", "about", "does",
        "do", "can", "you", "with", "from", "by", "at", "it", "this", "that", "be", "as"
    )

    private val synonymMap = mapOf(
        "reaction" to listOf("chemical", "equations", "oxidation", "reduction", "redox", "decomposition", "corrosion", "rancidity"),
        "rust" to listOf("corrosion", "oxidation", "iron", "galvanization", "metals"),
        "acid" to listOf("ph", "base", "salts", "neutralization", "litmus", "baking", "washing", "plaster"),
        "base" to listOf("ph", "acid", "salts", "alkaline", "neutralization", "lime"),
        "metal" to listOf("metals", "non-metals", "reactivity", "ionic", "roasting", "calcination", "alloy"),
        "carbon" to listOf("covalent", "catenation", "hydrocarbons", "alkane", "alkene", "alkyne", "soap", "micelle", "ester"),
        "plant" to listOf("photosynthesis", "chlorophyll", "xylem", "phloem", "tropism", "auxin", "pollination"),
        "heart" to listOf("circulation", "blood", "life", "processes", "respiration", "nephron", "digestion"),
        "brain" to listOf("nervous", "reflex", "neuron", "hormone", "endocrine", "control", "coordination"),
        "reproduction" to listOf("asexual", "sexual", "fission", "budding", "pollination", "zygote", "placenta"),
        "mendel" to listOf("heredity", "genes", "chromosomes", "dominant", "monohybrid", "dihybrid", "sex"),
        "ozone" to listOf("environment", "ecosystem", "food", "chain", "trophic", "biodegradable", "cfc"),
        "light" to listOf("reflection", "refraction", "mirror", "lens", "concave", "convex", "snell", "dioptre"),
        "eye" to listOf("myopia", "hypermetropia", "presbyopia", "prism", "rainbow", "scattering", "twinkling"),
        "current" to listOf("ohm", "voltage", "resistance", "electricity", "series", "parallel", "joule", "power"),
        "magnet" to listOf("magnetic", "solenoid", "fleming", "motor", "electromagnet", "fuse", "earth"),
        "hcf" to listOf("lcm", "prime", "irrational", "real", "numbers", "polynomials", "zeroes"),
        "quadratic" to listOf("roots", "discriminant", "equation", "formula", "linear", "substitution"),
        "ap" to listOf("arithmetic", "progressions", "common", "difference", "nth", "sum"),
        "triangle" to listOf("thales", "similar", "coordinate", "distance", "section", "midpoint"),
        "trigonometry" to listOf("sin", "cos", "tan", "identities", "elevation", "depression", "heights"),
        "circle" to listOf("tangent", "sector", "cylinder", "cone", "sphere", "hemisphere", "volume"),
        "mean" to listOf("median", "mode", "statistics", "probability", "outcomes", "die"),
        "gandhi" to listOf("nationalism", "satyagraha", "dandi", "non-cooperation", "civil", "disobedience", "europe"),
        "soil" to listOf("alluvial", "black", "regur", "cotton", "kharif", "rabi", "rainwater", "resources"),
        "democracy" to listOf("power", "sharing", "federalism", "panchayati", "sectors", "primary", "gdp", "shg"),
        "grammar" to listOf("tenses", "passive", "voice", "reported", "speech", "modals", "letter", "english")
    )

    fun tokenize(text: String): List<String> {
        return text.lowercase()
            .replace(Regex("[^a-z0-9\\s]"), " ")
            .split(Regex("\\s+"))
            .filter { it.length > 1 && it !in stopWords }
    }

    private fun expandQueryTokens(tokens: List<String>): List<String> {
        val expanded = mutableListOf<String>()
        expanded.addAll(tokens)
        for (token in tokens) {
            synonymMap[token]?.let { expanded.addAll(it) }
        }
        return expanded
    }

    fun retrieveTopPassages(
        query: String,
        corpus: List<TextbookPassage>,
        selectedTextbook: String? = null,
        selectedChapter: String? = null,
        topK: Int = 3
    ): List<RagRetrievalResult> {
        val filtered = corpus.filter { p ->
            (selectedTextbook.isNullOrBlank() || selectedTextbook == "All Downloaded Books" || p.textbookName == selectedTextbook) &&
                (selectedChapter.isNullOrBlank() || selectedChapter == "All Chapters" || p.chapterName == selectedChapter)
        }
        if (filtered.isEmpty()) return emptyList()

        val rawQueryTokens = tokenize(query)
        if (rawQueryTokens.isEmpty()) return emptyList()
        val expandedQuery = expandQueryTokens(rawQueryTokens)

        // Build document token lists
        val docTokensList = filtered.map { p ->
            tokenize("${p.subject} ${p.chapterName} ${p.textbookName} ${p.content} ${p.realLifeExample} ${p.formulaOrEquation}")
        }

        // Compute IDF across corpus
        val docCount = filtered.size.toDouble()
        val dfMap = mutableMapOf<String, Int>()
        for (docTokens in docTokensList) {
            for (term in docTokens.toSet()) {
                dfMap[term] = (dfMap[term] ?: 0) + 1
            }
        }

        fun tfidfVector(tokens: List<String>): Map<String, Double> {
            val tf = tokens.groupingBy { it }.eachCount()
            val maxTf = (tf.values.maxOrNull() ?: 1).toDouble()
            return tf.mapValues { (term, count) ->
                val idf = ln((docCount + 1.0) / ((dfMap[term] ?: 0) + 1.0)) + 1.0
                (count / maxTf) * idf
            }
        }

        val queryVec = tfidfVector(expandedQuery)
        val queryNorm = sqrt(queryVec.values.sumOf { it * it })
        if (queryNorm == 0.0) return emptyList()

        val queryLower = query.lowercase()

        val scored = filtered.mapIndexed { index, passage ->
            val docVec = tfidfVector(docTokensList[index])
            val docNorm = sqrt(docVec.values.sumOf { it * it })
            val cosine = if (docNorm == 0.0) 0.0 else {
                queryVec.entries.sumOf { (term, w) -> w * (docVec[term] ?: 0.0) } / (queryNorm * docNorm)
            }

            // Hybrid reranking boost for exact keyword/chapter matches
            val chapterMatchBoost = rawQueryTokens.count { passage.chapterName.lowercase().contains(it) } * 0.14
            val exactPhraseBoost = if (passage.content.lowercase().contains(queryLower) ||
                passage.chapterName.lowercase().contains(queryLower)
            ) 0.20 else 0.0

            val finalScore = (cosine + chapterMatchBoost + exactPhraseBoost).coerceAtMost(1.0)
            val confidence = (finalScore * 100).toInt().coerceIn(0, 99)
            RagRetrievalResult(passage, finalScore, confidence)
        }

        return scored
            .filter { it.score >= 0.14 }
            .sortedByDescending { it.score }
            .take(topK)
    }

    fun buildOfflineStructuredAnswer(
        query: String,
        topResult: RagRetrievalResult?,
        educationLevel: String,
        language: String
    ): TutorResponse {
        if (topResult == null || topResult.score < 0.14) {
            return TutorResponse(
                question = query,
                answerText = UNRELIABLE_MESSAGE,
                confidencePercent = 0,
                modeLabel = "Offline RAG • Low Confidence",
                isReliable = false
            )
        }

        val p = topResult.passage
        val langNote = if (language != "English") {
            "\n[Offline Mode Note: Showing verified textbook explanation in English. Connect briefly to enable full $language translation via AI.]\n"
        } else ""

        val levelTag = when {
            educationLevel.contains("School") -> "Simple School-Level Explanation"
            educationLevel.contains("PUC") -> "PUC Exam-Oriented Explanation"
            else -> "College Conceptual Explanation"
        }

        val sb = StringBuilder()
        sb.append("[$levelTag]$langNote\n")
        sb.append("1. Definition & Core Concept:\n${p.content}\n\n")
        sb.append("2. Everyday Life Example:\n${p.realLifeExample}\n\n")
        if (p.formulaOrEquation.isNotBlank()) {
            sb.append("3. Key Formula / Equation:\n${p.formulaOrEquation}\n\n")
        }
        if (p.practiceQuestion.isNotBlank()) {
            sb.append("4. Quick Practice Question:\n${p.practiceQuestion}")
        }

        return TutorResponse(
            question = query,
            answerText = sb.toString().trim(),
            sourceTextbook = p.textbookName,
            sourceChapter = p.chapterName,
            sourcePage = p.pageNumber,
            supportingPassage = p.content,
            confidencePercent = topResult.confidencePercent,
            modeLabel = "Offline RAG • Verified Textbook",
            isReliable = true
        )
    }
}
