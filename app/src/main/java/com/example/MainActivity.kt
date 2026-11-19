package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ai.configureAppCheck
import com.example.ui.screens.*
import com.example.ui.theme.GramVidyaTheme
import com.example.viewmodel.GramVidyaViewModel
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // STEP 1: Configure App Check with launch intent before any UI, ViewModels, or Firebase operations
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        setContent {
            GramVidyaTheme {
                var currentUser by remember { mutableStateOf(com.google.firebase.Firebase.auth.currentUser) }
                DisposableEffect(Unit) {
                    val listener = com.google.firebase.auth.FirebaseAuth.AuthStateListener { auth ->
                        currentUser = auth.currentUser
                    }
                    com.google.firebase.Firebase.auth.addAuthStateListener(listener)
                    onDispose { com.google.firebase.Firebase.auth.removeAuthStateListener(listener) }
                }

                if (currentUser == null) {
                    AuthScreen(
                        onAuthSuccess = { currentUser = com.google.firebase.Firebase.auth.currentUser }
                    )
                } else {
                    val uid = currentUser!!.uid
                    val displayName = currentUser!!.displayName?.takeIf { it.isNotBlank() } ?: "Student"
                    val email = currentUser!!.email.orEmpty()
                    key(uid) {
                        GramVidyaApp(
                            userDisplayName = displayName,
                            userEmail = email,
                            onSignOut = { currentUser = null }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GramVidyaApp(
    userDisplayName: String = "Student",
    userEmail: String = "",
    onSignOut: () -> Unit = {},
    viewModel: GramVidyaViewModel = viewModel()
) {
    LaunchedEffect(userDisplayName) {
        viewModel.updateProfile { it.copy(name = userDisplayName) }
    }
    val profile by viewModel.profile.collectAsStateWithLifecycle()
    val tutorState by viewModel.tutorState.collectAsStateWithLifecycle()
    val passages by viewModel.passages.collectAsStateWithLifecycle()
    val quizQuestions by viewModel.quizQuestions.collectAsStateWithLifecycle()
    val quizAttempts by viewModel.quizAttempts.collectAsStateWithLifecycle()
    val studyPlans by viewModel.studyPlans.collectAsStateWithLifecycle()
    val notes by viewModel.notes.collectAsStateWithLifecycle()
    val dictionary by viewModel.dictionary.collectAsStateWithLifecycle()
    val flashcards by viewModel.flashcards.collectAsStateWithLifecycle()
    val formulas by viewModel.formulas.collectAsStateWithLifecycle()
    val quizGenStatus by viewModel.quizGenStatus.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    val isOnline = remember(selectedTab, profile.forceOfflineMode) {
        viewModel.isNetworkAvailable()
    }

    val navItems = listOf(
        Triple("Home", Icons.Default.Home, 0),
        Triple("AI Tutor", Icons.Default.Psychology, 1),
        Triple("Library", Icons.AutoMirrored.Filled.MenuBook, 2),
        Triple("Practice", Icons.Default.Quiz, 3),
        Triple("Plan", Icons.Default.Person, 4)
    )

    androidx.compose.foundation.layout.BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isWideScreen = maxWidth >= 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = {
                if (!isWideScreen) {
                    NavigationBar {
                        navItems.forEach { (label, icon, index) ->
                            NavigationBarItem(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                icon = { Icon(imageVector = icon, contentDescription = label) },
                                label = { Text(label) }
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            androidx.compose.foundation.layout.Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (isWideScreen) {
                    NavigationRail {
                        navItems.forEach { (label, icon, index) ->
                            NavigationRailItem(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                icon = { Icon(imageVector = icon, contentDescription = label) },
                                label = { Text(label) }
                            )
                        }
                    }
                }
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    when (selectedTab) {
                0 -> HomeScreen(
                    profile = profile,
                    isOnline = isOnline,
                    passages = passages,
                    attempts = quizAttempts,
                    dictionary = dictionary,
                    onNavigateTab = { selectedTab = it },
                    onToggleOfflineMode = {
                        viewModel.updateProfile { it.copy(forceOfflineMode = !it.forceOfflineMode) }
                    },
                    onAskQuickQuestion = { q -> viewModel.askTutor(q) }
                )

                1 -> TutorScreen(
                    tutorState = tutorState,
                    profile = profile,
                    passages = passages,
                    onSelectTextbook = { book -> viewModel.setTextbookFilter(book) },
                    onSelectModel = { model -> viewModel.selectGeminiModel(model) },
                    onClearChat = { viewModel.clearChatHistory() },
                    onAskQuestion = { q -> viewModel.askTutor(q) },
                    onSolvePhoto = { bmp -> viewModel.solveCameraQuestion(bmp) },
                    onBookmarkAnswer = { resp ->
                        val citation = if (resp.sourceTextbook != null) {
                            "${resp.sourceTextbook} • ${resp.sourceChapter} (p. ${resp.sourcePage})"
                        } else ""
                        viewModel.saveNote(
                            title = resp.question,
                            subject = resp.sourceChapter ?: "AI Tutor",
                            content = resp.answerText,
                            isBookmark = true,
                            citation = citation
                        )
                    }
                )

                2 -> LibraryScreen(
                    profile = profile,
                    passages = passages,
                    notes = notes,
                    dictionary = dictionary,
                    flashcards = flashcards,
                    formulas = formulas,
                    onToggleDownload = { book, dl -> viewModel.toggleTextbookDownload(book, dl) },
                    onToggleCompleted = { id, done -> viewModel.togglePassageCompleted(id, done) },
                    onSaveNote = { t, s, c -> viewModel.saveNote(t, s, c) },
                    onDeleteNote = { id -> viewModel.deleteNote(id) },
                    onToggleDictFav = { id, fav -> viewModel.toggleDictionaryFavorite(id, fav) },
                    onToggleFlashcardMastered = { id, m -> viewModel.toggleFlashcardMastered(id, m) },
                    onAskAboutChapter = { q ->
                        viewModel.askTutor(q)
                        selectedTab = 1
                    }
                )

                3 -> PracticeScreen(
                    questions = quizQuestions,
                    passages = passages,
                    attempts = quizAttempts,
                    quizGenStatus = quizGenStatus,
                    onGenerateAiQuiz = { p, d -> viewModel.generateAiQuizForPassage(p, d) },
                    onClearGenStatus = { viewModel.clearQuizGenStatus() },
                    onSaveAttempt = { s, c, sc, tot, dur -> viewModel.saveQuizAttempt(s, c, sc, tot, dur) }
                )

                4 -> PlanProfileScreen(
                    profile = profile,
                    userEmail = userEmail,
                    studyPlans = studyPlans,
                    passages = passages,
                    attempts = quizAttempts,
                    onUpdateProfile = { viewModel.updateProfile(it) },
                    onSignOut = onSignOut,
                    onAddPlan = { t, s, d, e -> viewModel.addStudyPlan(t, s, d, e) },
                    onTogglePlan = { id, done -> viewModel.toggleStudyPlan(id, done) },
                    onDeletePlan = { id -> viewModel.deleteStudyPlan(id) },
                    onAddTeacherPassage = { b, l, s, c, p, cnt, ex, f ->
                        viewModel.addTeacherPassage(b, l, s, c, p, cnt, ex, f)
                    }
                )
            }
        }
        }
    }
    }
}
