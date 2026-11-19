package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "textbook_passages")
data class TextbookPassage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val textbookName: String,
    val educationLevel: String, // "School (6-10)", "PUC (11-12)", "College"
    val subject: String,
    val chapterName: String,
    val pageNumber: Int,
    val content: String,
    val realLifeExample: String,
    val formulaOrEquation: String = "",
    val practiceQuestion: String = "",
    val isDownloaded: Boolean = true,
    val isCompleted: Boolean = false
)

@Entity(tableName = "quiz_questions")
data class QuizQuestionEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val chapterName: String,
    val difficulty: String, // "Easy", "Medium", "Hard"
    val question: String,
    val optionA: String,
    val optionB: String,
    val optionC: String,
    val optionD: String,
    val correctIndex: Int,
    val explanation: String
)

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val chapterName: String,
    val score: Int,
    val totalQuestions: Int,
    val durationSeconds: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "study_plans")
data class StudyPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subject: String,
    val targetDate: String,
    val isExam: Boolean = false,
    val isCompleted: Boolean = false
)

@Entity(tableName = "study_notes")
data class StudyNoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val subject: String,
    val content: String,
    val isBookmark: Boolean = false,
    val sourceCitation: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "dictionary_entries")
data class DictionaryEntryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val word: String,
    val subject: String,
    val englishMeaning: String,
    val kannadaMeaning: String,
    val hindiMeaning: String,
    val teluguMeaning: String,
    val tamilMeaning: String,
    val exampleSentence: String,
    val isFavorite: Boolean = false
)

@Entity(tableName = "flashcards")
data class FlashcardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val chapterName: String,
    val front: String,
    val back: String,
    val isMastered: Boolean = false
)

@Entity(tableName = "formulas")
data class FormulaEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val title: String,
    val formula: String,
    val variablesNote: String
)
