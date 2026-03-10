package de.sysitprep.app.data.database

import androidx.room.*
import de.sysitprep.app.data.model.ExamType
import de.sysitprep.app.data.model.Fachrichtung
import de.sysitprep.app.data.model.Question
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(questions: List<Question>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(question: Question): Long

    @Update
    suspend fun update(question: Question)

    @Query("SELECT * FROM questions WHERE id = :id")
    suspend fun getById(id: Long): Question?

    @Query("SELECT * FROM questions WHERE questionKey = :key LIMIT 1")
    suspend fun getByKey(key: String): Question?

    @Query("SELECT * FROM questions WHERE lernfeld = :lernfeld AND (fachrichtung = :fachrichtung OR fachrichtung = 'GEMEINSAM') ORDER BY RANDOM() LIMIT :limit")
    suspend fun getByLernfeldAndFachrichtung(lernfeld: Int, fachrichtung: Fachrichtung, limit: Int): List<Question>

    @Query("SELECT * FROM questions WHERE examType = :examType AND (fachrichtung = :fachrichtung OR fachrichtung = 'GEMEINSAM') ORDER BY RANDOM() LIMIT :limit")
    suspend fun getByExamType(examType: ExamType, fachrichtung: Fachrichtung, limit: Int): List<Question>

    @Query("SELECT * FROM questions WHERE isBookmarked = 1 ORDER BY createdAt DESC")
    fun getBookmarked(): Flow<List<Question>>

    @Query("SELECT * FROM questions WHERE (fachrichtung = :fachrichtung OR fachrichtung = 'GEMEINSAM') ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomByFachrichtung(fachrichtung: Fachrichtung, limit: Int): List<Question>

    @Query("SELECT COUNT(*) FROM questions WHERE lernfeld = :lernfeld AND (fachrichtung = :fachrichtung OR fachrichtung = 'GEMEINSAM')")
    suspend fun countByLernfeld(lernfeld: Int, fachrichtung: Fachrichtung): Int

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun countAll(): Int

    @Query("DELETE FROM questions")
    suspend fun deleteAll()

    @Query("UPDATE questions SET isBookmarked = :bookmarked WHERE id = :id")
    suspend fun setBookmark(id: Long, bookmarked: Boolean)

    @Query("SELECT * FROM questions WHERE difficulty = :difficulty AND (fachrichtung = :fachrichtung OR fachrichtung = 'GEMEINSAM') ORDER BY RANDOM() LIMIT :limit")
    suspend fun getByDifficulty(difficulty: Int, fachrichtung: Fachrichtung, limit: Int): List<Question>
}
