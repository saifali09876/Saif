package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.QuizAttemptEntity
import com.example.data.QuizQuestionEntity
import com.example.data.TextbookPassage

@Composable
fun PracticeScreen(
    questions: List<QuizQuestionEntity>,
    passages: List<TextbookPassage>,
    attempts: List<QuizAttemptEntity>,
    quizGenStatus: String?,
    onGenerateAiQuiz: (TextbookPassage, String) -> Unit,
    onClearGenStatus: () -> Unit,
    onSaveAttempt: (String, String, Int, Int, Int) -> Unit
) {
    var selectedDifficulty by remember { mutableStateOf("All") }
    var selectedSubject by remember { mutableStateOf("All") }
    var activeQuiz by remember { mutableStateOf<List<QuizQuestionEntity>?>(null) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var score by remember { mutableIntStateOf(0) }
    var startTimeMs by remember { mutableLongStateOf(0L) }
    var showResult by remember { mutableStateOf(false) }

    val subjects = remember(questions) {
        listOf("All") + questions.map { it.subject }.distinct()
    }
    val difficulties = listOf("All", "Easy", "Medium", "Hard")

    if (activeQuiz != null) {
        val quiz = activeQuiz!!
        if (showResult) {
            val accuracy = (score * 100) / quiz.size.coerceAtLeast(1)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Quiz complete",
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text("Quiz Complete!", style = MaterialTheme.typography.headlineMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Score: $score / ${quiz.size} ($accuracy% Accuracy)",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (accuracy >= 80) "Excellent mastery! Keep up the streak." else "Good effort! Review the chapter in Library and try again.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        activeQuiz = null
                        showResult = false
                    },
                    modifier = Modifier.testTag("finish_quiz_button")
                ) {
                    Text("Back to Practice")
                }
            }
        } else {
            val q = quiz[currentIndex]
            val options = listOf(q.optionA, q.optionB, q.optionC, q.optionD)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Question ${currentIndex + 1} of ${quiz.size}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        TextButton(onClick = { activeQuiz = null }) { Text("Exit") }
                    }
                    LinearProgressIndicator(
                        progress = { (currentIndex + 1f) / quiz.size },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "${q.subject} • ${q.chapterName} • ${q.difficulty}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = q.question,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                items(options.indices.toList()) { idx ->
                    val isSelected = selectedOption == idx
                    val isCorrect = idx == q.correctIndex
                    val containerColor = when {
                        selectedOption == null -> MaterialTheme.colorScheme.surface
                        isCorrect -> MaterialTheme.colorScheme.primaryContainer
                        isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surface
                    }
                    Card(
                        onClick = {
                            if (selectedOption == null) {
                                selectedOption = idx
                                if (idx == q.correctIndex) score++
                            }
                        },
                        colors = CardDefaults.cardColors(containerColor = containerColor),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${('A' + idx)}. ",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Text(text = options[idx], style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }

                if (selectedOption != null) {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = if (selectedOption == q.correctIndex) "Correct! Why?" else "Not quite — Here's why:",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = q.explanation,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (currentIndex + 1 < quiz.size) {
                                    currentIndex++
                                    selectedOption = null
                                } else {
                                    val duration = ((System.currentTimeMillis() - startTimeMs) / 1000L).toInt().coerceAtLeast(1)
                                    onSaveAttempt(q.subject, q.chapterName, score, quiz.size, duration)
                                    showResult = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("next_question_button")
                        ) {
                            Text(if (currentIndex + 1 < quiz.size) "Next Question" else "Finish & Save Score")
                        }
                    }
                }
            }
        }
        return
    }

    // Practice Hub & Weakness Tracker
    val filteredQuestions = remember(questions, selectedSubject, selectedDifficulty) {
        questions.filter {
            (selectedSubject == "All" || it.subject == selectedSubject) &&
                (selectedDifficulty == "All" || it.difficulty == selectedDifficulty)
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text("Offline Practice & AI Quizzes", style = MaterialTheme.typography.headlineSmall)
            Text(
                text = "Practice chapter MCQs offline or generate fresh questions with Gemini AI.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (quizGenStatus != null) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(quizGenStatus, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                        TextButton(onClick = onClearGenStatus) { Text("Dismiss") }
                    }
                }
            }
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(subjects) { subj ->
                    FilterChip(
                        selected = selectedSubject == subj,
                        onClick = { selectedSubject = subj },
                        label = { Text(subj) }
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(difficulties) { diff ->
                    FilterChip(
                        selected = selectedDifficulty == diff,
                        onClick = { selectedDifficulty = diff },
                        label = { Text(diff) }
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "${filteredQuestions.size} Offline Questions Ready",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = {
                                if (filteredQuestions.isNotEmpty()) {
                                    activeQuiz = filteredQuestions.shuffled().take(5)
                                    currentIndex = 0
                                    selectedOption = null
                                    score = 0
                                    startTimeMs = System.currentTimeMillis()
                                    showResult = false
                                }
                            },
                            enabled = filteredQuestions.isNotEmpty(),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("start_quiz_button")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Start Quiz")
                        }
                        OutlinedButton(
                            onClick = {
                                val target = passages.firstOrNull {
                                    selectedSubject == "All" || it.subject == selectedSubject
                                } ?: passages.firstOrNull()
                                if (target != null) {
                                    onGenerateAiQuiz(target, if (selectedDifficulty == "All") "Medium" else selectedDifficulty)
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("generate_ai_quiz_button")
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("AI Generate")
                        }
                    }
                }
            }
        }

        item {
            Text("My Progress & Weak Areas", style = MaterialTheme.typography.titleLarge)
        }

        if (attempts.isEmpty()) {
            item {
                Text(
                    text = "Complete your first quiz to see accuracy, study time, and weak chapter recommendations.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            items(attempts) { att ->
                val pct = (att.score * 100) / att.totalQuestions.coerceAtLeast(1)
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(att.chapterName, style = MaterialTheme.typography.titleSmall)
                            Text(
                                text = "${att.subject} • ${att.durationSeconds}s",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (pct >= 70) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                        ) {
                            Text(
                                text = "${att.score}/${att.totalQuestions} ($pct%)",
                                style = MaterialTheme.typography.labelMedium,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
