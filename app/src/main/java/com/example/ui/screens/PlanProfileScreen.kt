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
import androidx.compose.ui.unit.dp
import com.example.data.QuizAttemptEntity
import com.example.data.StudyPlanEntity
import com.example.data.TextbookPassage
import com.example.viewmodel.StudentProfile

@Composable
fun PlanProfileScreen(
    profile: StudentProfile,
    userEmail: String = "",
    studyPlans: List<StudyPlanEntity>,
    passages: List<TextbookPassage>,
    attempts: List<QuizAttemptEntity>,
    onUpdateProfile: ((StudentProfile) -> StudentProfile) -> Unit,
    onSignOut: () -> Unit = {},
    onAddPlan: (String, String, String, Boolean) -> Unit,
    onTogglePlan: (Int, Boolean) -> Unit,
    onDeletePlan: (Int) -> Unit,
    onAddTeacherPassage: (String, String, String, String, Int, String, String, String) -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    val credentialManager = remember(context) { androidx.credentials.CredentialManager.create(context) }

    var showAddPlanDialog by remember { mutableStateOf(false) }
    var showTeacherUploadDialog by remember { mutableStateOf(false) }

    val levels = listOf("Class 10", "Class 10 (NCERT / CBSE)", "Class 10 (State Board)")
    val languages = listOf("English", "Kannada", "Hindi", "Telugu", "Tamil")
    val roles = listOf("Student", "Teacher", "Admin")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Signed-in User Account Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(profile.name, style = MaterialTheme.typography.titleMedium)
                        if (userEmail.isNotBlank()) {
                            Text(
                                text = userEmail,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    OutlinedButton(
                        onClick = {
                            signOutUser(credentialManager, scope, onSignOutComplete = onSignOut)
                        },
                        modifier = Modifier.testTag("sign_out_button")
                    ) {
                        Text("Sign Out")
                    }
                }
            }
        }

        // Role Switcher
        item {
            Text("Role & Workspace", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(roles) { role ->
                    FilterChip(
                        selected = profile.role == role,
                        onClick = { onUpdateProfile { it.copy(role = role) } },
                        label = { Text("$role View") }
                    )
                }
            }
        }

        if (profile.role == "Teacher") {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Teacher Dashboard", style = MaterialTheme.typography.titleLarge)
                        Text(
                            text = "Upload verified textbook chapters into the offline RAG database and monitor student quiz performance.",
                            style = MaterialTheme.typography.bodySmall
                        )
                        Button(
                            onClick = { showTeacherUploadDialog = true },
                            modifier = Modifier.testTag("teacher_upload_button")
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Add Verified Textbook Chapter")
                        }
                        Text(
                            text = "Active Chapters in Offline Corpus: ${passages.size}  |  Student Quiz Attempts: ${attempts.size}",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        } else if (profile.role == "Admin") {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Application Owner Dashboard", style = MaterialTheme.typography.titleLarge)
                        Text("Downloaded Offline Passages: ${passages.count { it.isDownloaded }} / ${passages.size}", style = MaterialTheme.typography.bodyMedium)
                        Text("Supported Languages: English, Kannada, Hindi, Telugu, Tamil", style = MaterialTheme.typography.bodyMedium)
                        Text("AI Engine: Hybrid Offline TF-IDF RAG + Firebase AI Logic (gemini-3.8-flash)", style = MaterialTheme.typography.bodyMedium)
                        Text("Low-Resource Mode: ${if (profile.lowEndDeviceMode) "Enabled" else "Standard"}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }

        // Smart Study Planner
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Smart Study Planner", style = MaterialTheme.typography.titleLarge)
                FilledTonalButton(
                    onClick = { showAddPlanDialog = true },
                    modifier = Modifier.testTag("add_plan_button")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Add Goal")
                }
            }
        }

        items(studyPlans) { plan ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (plan.isExam) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = plan.isCompleted,
                        onCheckedChange = { onTogglePlan(plan.id, it) }
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (plan.isExam) "EXAM: ${plan.title}" else plan.title,
                            style = MaterialTheme.typography.titleSmall
                        )
                        Text(
                            text = "${plan.subject} • ${plan.targetDate}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { onDeletePlan(plan.id) }) {
                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete study goal")
                    }
                }
            }
        }

        // Class Level & Regional Language Settings
        item {
            Text("Learn According to Your Class", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(levels) { lvl ->
                    FilterChip(
                        selected = profile.educationLevel == lvl,
                        onClick = { onUpdateProfile { it.copy(educationLevel = lvl) } },
                        label = { Text(lvl) }
                    )
                }
            }
        }

        item {
            Text("Regional Language", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(languages) { lang ->
                    FilterChip(
                        selected = profile.language == lang,
                        onClick = { onUpdateProfile { it.copy(language = lang) } },
                        label = { Text(lang) }
                    )
                }
            }
        }

        // Low-Connectivity & Low-End Device Controls
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("Rural Low-Connectivity Settings", style = MaterialTheme.typography.titleMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Offline-Only / Low-Data Mode", style = MaterialTheme.typography.bodyMedium)
                            Text("Force 100% local textbook RAG with zero data usage", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = profile.forceOfflineMode,
                            onCheckedChange = { checked -> onUpdateProfile { it.copy(forceOfflineMode = checked) } }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Low-End Device Mode", style = MaterialTheme.typography.bodyMedium)
                            Text("Battery-friendly lightweight mode for low-RAM phones", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = profile.lowEndDeviceMode,
                            onCheckedChange = { checked -> onUpdateProfile { it.copy(lowEndDeviceMode = checked) } }
                        )
                    }
                }
            }
        }
    }

    if (showAddPlanDialog) {
        var title by remember { mutableStateOf("") }
        var subject by remember { mutableStateOf("Physics") }
        var date by remember { mutableStateOf("Tomorrow, 6:00 PM") }
        var isExam by remember { mutableStateOf(false) }

        AlertDialog(
            onDismissRequest = { showAddPlanDialog = false },
            title = { Text("Add Study Goal / Exam") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Chapter or Exam Title") })
                    OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject") })
                    OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Target Date / Time") })
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(checked = isExam, onCheckedChange = { isExam = it })
                        Text("Mark as Examination Date")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (title.isNotBlank()) {
                        onAddPlan(title, subject, date, isExam)
                        showAddPlanDialog = false
                    }
                }) { Text("Save") }
            },
            dismissButton = {
                TextButton(onClick = { showAddPlanDialog = false }) { Text("Cancel") }
            }
        )
    }

    if (showTeacherUploadDialog) {
        var book by remember { mutableStateOf("Teacher Verified Notes") }
        var subject by remember { mutableStateOf("Physics") }
        var chapter by remember { mutableStateOf("") }
        var content by remember { mutableStateOf("") }
        var example by remember { mutableStateOf("") }
        var formula by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showTeacherUploadDialog = false },
            title = { Text("Add Verified Textbook Content") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(value = book, onValueChange = { book = it }, label = { Text("Textbook Name") })
                    OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject") })
                    OutlinedTextField(value = chapter, onValueChange = { chapter = it }, label = { Text("Chapter Name") })
                    OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Verified Passage") }, minLines = 3)
                    OutlinedTextField(value = example, onValueChange = { example = it }, label = { Text("Real-Life Example") })
                    OutlinedTextField(value = formula, onValueChange = { formula = it }, label = { Text("Formula (optional)") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    if (chapter.isNotBlank() && content.isNotBlank()) {
                        onAddTeacherPassage(book, profile.educationLevel, subject, chapter, 1, content, example, formula)
                        showTeacherUploadDialog = false
                    }
                }) { Text("Publish Offline") }
            },
            dismissButton = {
                TextButton(onClick = { showTeacherUploadDialog = false }) { Text("Cancel") }
            }
        )
    }
}
