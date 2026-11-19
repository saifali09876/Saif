package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.*
import com.example.viewmodel.StudentProfile

@Composable
fun LibraryScreen(
    profile: StudentProfile,
    passages: List<TextbookPassage>,
    notes: List<StudyNoteEntity>,
    dictionary: List<DictionaryEntryEntity>,
    flashcards: List<FlashcardEntity>,
    formulas: List<FormulaEntity>,
    onToggleDownload: (String, Boolean) -> Unit,
    onToggleCompleted: (Int, Boolean) -> Unit,
    onSaveNote: (String, String, String) -> Unit,
    onDeleteNote: (Int) -> Unit,
    onToggleDictFav: (Int, Boolean) -> Unit,
    onToggleFlashcardMastered: (Int, Boolean) -> Unit,
    onAskAboutChapter: (String) -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Books", "Notes", "Dictionary", "Tools")

    var searchQuery by remember { mutableStateOf("") }
    var readingPassage by remember { mutableStateOf<TextbookPassage?>(null) }
    var showAddNoteDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, style = MaterialTheme.typography.labelLarge) }
                )
            }
        }

        when (selectedTab) {
            0 -> {
                // Offline Digital Library + Search Inside Books
                Column(modifier = Modifier.fillMaxSize()) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Search inside downloaded textbooks...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search books") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .testTag("library_search_input"),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )

                    val filtered = remember(passages, searchQuery) {
                        if (searchQuery.isBlank()) passages
                        else passages.filter {
                            it.chapterName.contains(searchQuery, ignoreCase = true) ||
                                it.textbookName.contains(searchQuery, ignoreCase = true) ||
                                it.content.contains(searchQuery, ignoreCase = true) ||
                                it.subject.contains(searchQuery, ignoreCase = true)
                        }
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(filtered) { p ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { readingPassage = p },
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${p.subject} • ${p.educationLevel}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.secondary,
                                            fontWeight = FontWeight.Bold
                                        )
                                        IconButton(
                                            onClick = { onToggleDownload(p.textbookName, !p.isDownloaded) }
                                        ) {
                                            Icon(
                                                imageVector = if (p.isDownloaded) Icons.Default.DownloadDone else Icons.Default.Download,
                                                contentDescription = if (p.isDownloaded) "Downloaded offline" else "Download for offline",
                                                tint = if (p.isDownloaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }
                                    Text(
                                        text = p.chapterName,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${p.textbookName} • Page ${p.pageNumber}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text(
                                        text = p.content,
                                        style = MaterialTheme.typography.bodySmall,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }

            1 -> {
                // Offline Notes & Bookmarks + Nearby Share
                Box(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (notes.isEmpty()) {
                            item {
                                Text(
                                    text = "No offline notes or bookmarks yet. Tap + to create a note or bookmark an AI Tutor answer.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        items(notes) { note ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (note.isBookmark) "BOOKMARK • ${note.subject}" else note.subject,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        Row {
                                            IconButton(onClick = {
                                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                                    type = "text/plain"
                                                    putExtra(Intent.EXTRA_SUBJECT, note.title)
                                                    putExtra(Intent.EXTRA_TEXT, "${note.title}\n\n${note.content}\n\n${note.sourceCitation}")
                                                }
                                                context.startActivity(Intent.createChooser(shareIntent, "Share Study Note Nearby"))
                                            }) {
                                                Icon(Icons.Default.Share, contentDescription = "Share note nearby")
                                            }
                                            IconButton(onClick = { onDeleteNote(note.id) }) {
                                                Icon(Icons.Default.Delete, contentDescription = "Delete note")
                                            }
                                        }
                                    }
                                    Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(text = note.content, style = MaterialTheme.typography.bodySmall)
                                    if (note.sourceCitation.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = "Source: ${note.sourceCitation}",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    FloatingActionButton(
                        onClick = { showAddNoteDialog = true },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .testTag("add_note_fab")
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add offline note")
                    }
                }
            }

            2 -> {
                // Offline Multilingual Dictionary
                var dictSearch by remember { mutableStateOf("") }
                val filteredDict = remember(dictionary, dictSearch) {
                    if (dictSearch.isBlank()) dictionary
                    else dictionary.filter {
                        it.word.contains(dictSearch, ignoreCase = true) ||
                            it.englishMeaning.contains(dictSearch, ignoreCase = true)
                    }
                }
                Column(modifier = Modifier.fillMaxSize()) {
                    OutlinedTextField(
                        value = dictSearch,
                        onValueChange = { dictSearch = it },
                        placeholder = { Text("Search scientific terms offline...") },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search dictionary") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp)
                    )
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(filteredDict) { entry ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${entry.word} (${entry.subject})",
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                        IconButton(onClick = { onToggleDictFav(entry.id, !entry.isFavorite) }) {
                                            Icon(
                                                imageVector = if (entry.isFavorite) Icons.Default.Star else Icons.Default.StarBorder,
                                                contentDescription = "Favorite word",
                                                tint = MaterialTheme.colorScheme.tertiary
                                            )
                                        }
                                    }
                                    Text(text = entry.englishMeaning, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Text("ಕನ್ನಡ: ${entry.kannadaMeaning}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                                    Text("हिन्दी: ${entry.hindiMeaning}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Text("తెలుగు: ${entry.teluguMeaning}  |  தமிழ்: ${entry.tamilMeaning}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Example: ${entry.exampleSentence}", style = MaterialTheme.typography.labelSmall)
                                }
                            }
                        }
                    }
                }
            }

            3 -> {
                // Flashcards & Formula Book
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Revision Flashcards (Tap to Flip)",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    items(flashcards) { card ->
                        var flipped by remember { mutableStateOf(false) }
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { flipped = !flipped },
                            colors = CardDefaults.cardColors(
                                containerColor = if (flipped) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${card.subject} • ${if (flipped) "ANSWER" else "QUESTION"}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    IconButton(
                                        onClick = { onToggleFlashcardMastered(card.id, !card.isMastered) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (card.isMastered) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                            contentDescription = "Mark flashcard mastered",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = if (flipped) card.back else card.front,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Offline Formula Book",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    items(formulas) { f ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Text(
                                    text = "${f.subject} • ${f.title}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Text(
                                    text = f.formula,
                                    style = MaterialTheme.typography.titleLarge,
                                    color = MaterialTheme.colorScheme.secondary
                                )
                                Text(
                                    text = f.variablesNote,
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

    // Chapter Reader Dialog
    if (readingPassage != null) {
        val p = readingPassage!!
        AlertDialog(
            onDismissRequest = { readingPassage = null },
            title = { Text(p.chapterName) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "${p.textbookName} • Page ${p.pageNumber}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(text = p.content, style = MaterialTheme.typography.bodyMedium)
                    Text(text = "Real-Life Example: ${p.realLifeExample}", style = MaterialTheme.typography.bodySmall)
                    if (p.formulaOrEquation.isNotBlank()) {
                        Text(
                            text = "Formula: ${p.formulaOrEquation}",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    onAskAboutChapter("Explain ${p.chapterName}")
                    readingPassage = null
                }) {
                    Text("Ask AI Tutor")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onToggleCompleted(p.id, !p.isCompleted)
                    readingPassage = null
                }) {
                    Text(if (p.isCompleted) "Mark Unread" else "Mark Completed")
                }
            }
        )
    }

    // Add Note Dialog
    if (showAddNoteDialog) {
        var noteTitle by remember { mutableStateOf("") }
        var noteSubject by remember { mutableStateOf("Physics") }
        var noteBody by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showAddNoteDialog = false },
            title = { Text("New Offline Study Note") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = noteTitle,
                        onValueChange = { noteTitle = it },
                        label = { Text("Title") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = noteSubject,
                        onValueChange = { noteSubject = it },
                        label = { Text("Subject") },
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = noteBody,
                        onValueChange = { noteBody = it },
                        label = { Text("Note Content") },
                        minLines = 3
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (noteTitle.isNotBlank() && noteBody.isNotBlank()) {
                            onSaveNote(noteTitle, noteSubject, noteBody)
                            showAddNoteDialog = false
                        }
                    }
                ) { Text("Save Offline") }
            },
            dismissButton = {
                TextButton(onClick = { showAddNoteDialog = false }) { Text("Cancel") }
            }
        )
    }
}
