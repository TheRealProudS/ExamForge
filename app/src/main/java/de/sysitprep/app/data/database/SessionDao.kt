package de.sysitprep.app.data.database

import androidx.room.*
import de.sysitprep.app.data.model.Session
import de.sysitprep.app.data.model.UserAnswer
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {

    @Insert
    suspend fun insertSession(session: Session): Long

    @Update
    suspend fun updateSession(session: Session)

    @Query("SELECT * FROM sessions WHERE id = :id")
    suspend fun getSessionById(id: Long): Session?

    @Query("SELECT * FROM sessions ORDER BY startedAt DESC LIMIT :limit")
    fun getRecentSessions(limit: Int = 20): Flow<List<Session>>

    @Query("SELECT * FROM sessions ORDER BY startedAt DESC LIMIT :limit")
    suspend fun getRecentSessionsList(limit: Int): List<Session>

    @Query("SELECT COUNT(*) FROM sessions WHERE completedAt IS NOT NULL")
    suspend fun countCompletedSessions(): Int

    @Query("SELECT SUM(durationSeconds) FROM sessions WHERE completedAt IS NOT NULL")
    suspend fun getTotalStudyTimeSeconds(): Int?

    @Insert
    suspend fun insertAnswer(answer: UserAnswer): Long

    @Insert
    suspend fun insertAnswers(answers: List<UserAnswer>)

    @Query("SELECT * FROM user_answers WHERE sessionId = :sessionId")
    suspend fun getAnswersForSession(sessionId: Long): List<UserAnswer>

    @Query("SELECT COUNT(*) FROM user_answers WHERE questionId = :questionId AND isCorrect = 1")
    suspend fun countCorrectForQuestion(questionId: Long): Int

    @Query("SELECT COUNT(*) FROM user_answers WHERE questionId = :questionId")
    suspend fun countAnsweredForQuestion(questionId: Long): Int

    @Query("SELECT SUM(isCorrect) FROM user_answers WHERE sessionId = :sessionId")
    suspend fun countCorrectInSession(sessionId: Long): Int?

    @Query("""
        SELECT ua.questionId, COUNT(*) as totalAnswered, SUM(ua.isCorrect) as correctCount
        FROM user_answers ua
        JOIN questions q ON ua.questionId = q.id
        WHERE q.lernfeld = :lernfeld
        GROUP BY ua.questionId
    """)
    suspend fun getProgressForLernfeld(lernfeld: Int): List<LernfeldProgressRaw>

    @Query("SELECT * FROM sessions ORDER BY startedAt DESC LIMIT 1")
    suspend fun getLastSession(): Session?

    @Query("SELECT COUNT(*) FROM user_answers")
    suspend fun countTotalAnswered(): Int?

    @Query("SELECT SUM(isCorrect) FROM user_answers")
    suspend fun countTotalCorrect(): Int?

    @Delete(entity = Session::class)
    suspend fun deleteSessionEntity(session: Session)

    @Query("DELETE FROM sessions")
    suspend fun deleteAllSessions()

    @Query("DELETE FROM user_answers")
    suspend fun deleteAllAnswers()
}

data class LernfeldProgressRaw(
    val questionId: Long,
    val totalAnswered: Int,
    val correctCount: Int
)
