package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GramVidyaDao {
    // Textbooks & Passages
    @Query("SELECT * FROM textbook_passages ORDER BY subject, chapterName, pageNumber")
    fun getAllPassages(): Flow<List<TextbookPassage>>

    @Query("SELECT * FROM textbook_passages WHERE isDownloaded = 1")
    suspend fun getDownloadedPassagesSync(): List<TextbookPassage>

    @Query("SELECT COUNT(*) FROM textbook_passages")
    suspend fun getPassageCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassages(passages: List<TextbookPassage>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassage(passage: TextbookPassage)

    @Query("UPDATE textbook_passages SET isDownloaded = :downloaded WHERE textbookName = :textbookName")
    suspend fun setTextbookDownloaded(textbookName: String, downloaded: Boolean)

    @Query("UPDATE textbook_passages SET isCompleted = :completed WHERE id = :id")
    suspend fun setPassageCompleted(id: Int, completed: Boolean)

    // Quizzes
    @Query("SELECT * FROM quiz_questions ORDER BY subject, difficulty")
    fun getAllQuizQuestions(): Flow<List<QuizQuestionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestions(questions: List<QuizQuestionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestion(question: QuizQuestionEntity)

    // Quiz Attempts & Weakness Tracking
    @Query("SELECT * FROM quiz_attempts ORDER BY timestamp DESC")
    fun getAllQuizAttempts(): Flow<List<QuizAttemptEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizAttempt(attempt: QuizAttemptEntity)

    // Study Plans
    @Query("SELECT * FROM study_plans ORDER BY isCompleted ASC, id DESC")
    fun getAllStudyPlans(): Flow<List<StudyPlanEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyPlan(plan: StudyPlanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudyPlans(plans: List<StudyPlanEntity>)

    @Query("UPDATE study_plans SET isCompleted = :completed WHERE id = :id")
    suspend fun setStudyPlanCompleted(id: Int, completed: Boolean)

    @Query("DELETE FROM study_plans WHERE id = :id")
    suspend fun deleteStudyPlan(id: Int)

    // Notes & Bookmarks
    @Query("SELECT * FROM study_notes ORDER BY createdAt DESC")
    fun getAllNotes(): Flow<List<StudyNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: StudyNoteEntity)

    @Query("DELETE FROM study_notes WHERE id = :id")
    suspend fun deleteNote(id: Int)

    // Dictionary
    @Query("SELECT * FROM dictionary_entries ORDER BY word ASC")
    fun getAllDictionaryEntries(): Flow<List<DictionaryEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDictionaryEntries(entries: List<DictionaryEntryEntity>)

    @Query("UPDATE dictionary_entries SET isFavorite = :favorite WHERE id = :id")
    suspend fun setDictionaryFavorite(id: Int, favorite: Boolean)

    // Flashcards
    @Query("SELECT * FROM flashcards ORDER BY subject, id")
    fun getAllFlashcards(): Flow<List<FlashcardEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlashcards(cards: List<FlashcardEntity>)

    @Query("UPDATE flashcards SET isMastered = :mastered WHERE id = :id")
    suspend fun setFlashcardMastered(id: Int, mastered: Boolean)

    // Formulas
    @Query("SELECT * FROM formulas ORDER BY subject, title")
    fun getAllFormulas(): Flow<List<FormulaEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFormulas(formulas: List<FormulaEntity>)
}
