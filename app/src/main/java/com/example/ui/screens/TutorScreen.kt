package com.example.ui.screens

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ai.GeminiModelOption
import com.example.ai.TutorResponse
import com.example.data.TextbookPassage
import com.example.viewmodel.StudentProfile
import com.example.viewmodel.TutorUiState
import java.util.Locale

@Composable
fun TutorScreen(
    tutorState: TutorUiState,
    profile: StudentProfile,
    passages: List<TextbookPassage>,
    onSelectTextbook: (String) -> Unit,
    onSelectModel: (GeminiModelOption) -> Unit,
    onClearChat: () -> Unit,
    onAskQuestion: (String) -> Unit,
    onSolvePhoto: (Bitmap) -> Unit,
    onBookmarkAnswer: (TutorResponse) -> Unit
) {
    val context = LocalContext.current
    var queryText by remember { mutableStateOf("") }
    var bookmarkedNotice by remember { mutableStateOf<String?>(null) }
    val listState = rememberLazyListState()

    // Auto-scroll to latest message in the chat thread
    LaunchedEffect(tutorState.messages.size, tutorState.streamingText.length) {
        val totalItems = tutorState.messages.size + if (tutorState.isLoading) 1 else 0
        if (totalItems > 0) {
            listState.animateScrollToItem(totalItems - 1)
        }
    }

    // Text-to-Speech engine for Voice-Based Learning
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        val engine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
        tts = engine
        onDispose {
            engine.stop()
            engine.shutdown()
        }
    }

    // Voice Speech-to-Text Launcher
    val voiceLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spoken = matches?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                queryText = ""
                onAskQuestion(spoken)
            }
        }
    }

    // Camera Question Scanner Launcher
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            onSolvePhoto(bitmap)
        }
    }

    val downloadedBooks = remember(passages) {
        listOf("All Downloaded Books") + passages.filter { it.isDownloaded }.map { it.textbookName }.distinct()
    }

    val sampleQuestions = listOf(
        "Explain Newton's third law",
        "What is photosynthesis?",
        "Explain Ohm's law with an example",
        "How does pH help farmers?",
        "What is binary search?"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Header with Gemini Model Selector, Camera Scan & New Chat
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Gemini AI Tutor",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${tutorState.selectedModel.label} • ${profile.educationLevel} • ${profile.language}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        if (tutorState.messages.isNotEmpty()) {
                            IconButton(
                                onClick = onClearChat,
                                modifier = Modifier.testTag("new_chat_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Start new chat"
                                )
                            }
                        }
                        FilledTonalIconButton(
                            onClick = { cameraLauncher.launch(null) },
                            modifier = Modifier.testTag("camera_scan_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "Scan textbook question photo"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                // Gemini Model Selector Chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(GeminiModelOption.entries) { model ->
                        FilterChip(
                            selected = tutorState.selectedModel == model,
                            onClick = { onSelectModel(model) },
                            label = {
                                Text(
                                    text = "${model.label} (${model.subtitle})",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                // Textbook Grounding Selector
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(downloadedBooks) { book ->
                        FilterChip(
                            selected = tutorState.selectedTextbook == book,
                            onClick = { onSelectTextbook(book) },
                            label = { Text(book, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }
        }

        if (bookmarkedNotice != null) {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(bookmarkedNotice!!, style = MaterialTheme.typography.bodySmall)
                    TextButton(onClick = { bookmarkedNotice = null }) { Text("OK") }
                }
            }
        }

        if (tutorState.errorMessage != null) {
            Surface(
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = tutorState.errorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        // Scrollable Multi-Turn Chat Thread
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            if (tutorState.messages.isEmpty() && !tutorState.isLoading) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Multi-Turn Gemini AI Tutor",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Ask questions and natural follow-ups in ${profile.language}. Grounded in your downloaded textbooks with automatic Offline RAG fallback.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            sampleQuestions.forEach { sample ->
                                SuggestionChip(
                                    onClick = { onAskQuestion(sample) },
                                    label = { Text(sample) },
                                    modifier = Modifier.padding(vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            items(tutorState.messages) { msg ->
                // User Bubble (Right-aligned)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp),
                        modifier = Modifier.widthIn(max = 300.dp)
                    ) {
                        Text(
                            text = msg.question,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                // Gemini AI Bubble (Left-aligned)
                TutorAnswerCard(
                    response = msg,
                    onSpeak = {
                        tts?.speak(msg.answerText, TextToSpeech.QUEUE_FLUSH, null, "tutor_ans")
                    },
                    onBookmark = {
                        onBookmarkAnswer(msg)
                        bookmarkedNotice = "Saved answer to Offline Notes & Bookmarks!"
                    }
                )
            }

            if (tutorState.isLoading) {
                item {
                    if (!tutorState.pendingQuestion.isNullOrBlank()) {
                        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                            Surface(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(16.dp, 16.dp, 4.dp, 16.dp),
                                modifier = Modifier.widthIn(max = 300.dp)
                            ) {
                                Text(
                                    text = tutorState.pendingQuestion,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                        shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                Text(
                                    text = if (tutorState.streamingText.isBlank()) "${tutorState.selectedModel.label} is thinking..." else "${tutorState.selectedModel.label} is replying...",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                            if (tutorState.streamingText.isNotBlank()) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = tutorState.streamingText,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        }

        // Bottom Input Bar with Voice + Send
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Ask Gemini AI Tutor...")
                        }
                        try {
                            voiceLauncher.launch(intent)
                        } catch (_: Exception) {
                        }
                    },
                    modifier = Modifier.testTag("voice_input_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Ask with voice",
                        tint = MaterialTheme.colorScheme.secondary
                    )
                }

                OutlinedTextField(
                    value = queryText,
                    onValueChange = { queryText = it },
                    placeholder = { Text("Ask Gemini AI or a follow-up...") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("tutor_question_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(24.dp)
                )

                FilledIconButton(
                    onClick = {
                        if (queryText.isNotBlank()) {
                            onAskQuestion(queryText)
                            queryText = ""
                        }
                    },
                    enabled = queryText.isNotBlank() && !tutorState.isLoading,
                    modifier = Modifier.testTag("tutor_send_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send message"
                    )
                }
            }
        }
    }
}

@Composable
private fun TutorAnswerCard(
    response: TutorResponse,
    onSpeak: () -> Unit,
    onBookmark: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (response.isReliable) {
                        MaterialTheme.colorScheme.primaryContainer
                    } else {
                        MaterialTheme.colorScheme.errorContainer
                    }
                ) {
                    Text(
                        text = response.modeLabel,
                        style = MaterialTheme.typography.labelSmall,
                        color = if (response.isReliable) {
                            MaterialTheme.colorScheme.onPrimaryContainer
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
                Row {
                    IconButton(onClick = onSpeak) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Read answer aloud",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onBookmark) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark answer",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = response.answerText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Source Citation Box
            if (response.sourceTextbook != null) {
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "VERIFIED TEXTBOOK SOURCE (${response.confidencePercent}% Match)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${response.sourceTextbook} • ${response.sourceChapter} • Page ${response.sourcePage}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (!response.supportingPassage.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "\"${response.supportingPassage}\"",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}
