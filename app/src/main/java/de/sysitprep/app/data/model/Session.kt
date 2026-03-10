package de.sysitprep.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.sysitprep.app.data.database.Converters

/**
 * Tracks a user's answer to a specific question in a session.
 */
@Entity(tableName = "user_answers")
@TypeConverters(Converters::class)
data class UserAnswer(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val questionId: Long,
    val selectedIndices: List<Int>,
    val isCorrect: Boolean,
    val timeSpentSeconds: Int,
    val answeredAt: Long = System.currentTimeMillis()
)

/**
 * A learning or exam session.
 */
@Entity(tableName = "sessions")
@TypeConverters(Converters::class)
data class Session(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionType: SessionType,
    val fachrichtung: Fachrichtung,
    val examType: ExamType? = null,
    val lernfeld: Int? = null,
    val totalQuestions: Int,
    val correctAnswers: Int = 0,
    val durationSeconds: Int = 0,
    val completedAt: Long? = null,
    val startedAt: Long = System.currentTimeMillis(),
    val questionIds: List<Long> = emptyList()
)

enum class SessionType(val displayName: String) {
    LERNFELD("Lernfeld Training"),
    EXAM_SIMULATION("Prüfungssimulation"),
    QUICK_QUIZ("Schnell-Quiz"),
    BOOKMARK_REVIEW("Lesezeichen Wiederholung")
}
