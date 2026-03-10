package de.sysitprep.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.sysitprep.app.data.database.Converters

/**
 * Represents a single exam question (Prüfungsfrage).
 */
@Entity(tableName = "questions")
@TypeConverters(Converters::class)
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    /** Unique JSON-key to prevent re-import duplicates */
    val questionKey: String,

    /** Short display title */
    val title: String,

    /** Full question text */
    val questionText: String,

    /** Answer options A–E (max 5) */
    val options: List<String>,

    /** Indices of correct answers (0-based, supports multiple-choice) */
    val correctAnswerIndices: List<Int>,

    /** Explanation shown after answering */
    val explanation: String = "",

    /** Lernfeld number (1–14 for SI, 1–14 for AE) */
    val lernfeld: Int,

    /** Fachrichtung: SI = Systemintegration, AE = Anwendungsentwicklung, GEMEINSAM = both */
    val fachrichtung: Fachrichtung,

    /** Exam type: AP1 or AP2 */
    val examType: ExamType,

    /** Year the question appeared (e.g. 2023) */
    val year: Int = 0,

    /** Difficulty level 1–3 */
    val difficulty: Int = 1,

    /** Whether this question is bookmarked */
    val isBookmarked: Boolean = false,

    /** Timestamp of creation */
    val createdAt: Long = System.currentTimeMillis()
)

enum class Fachrichtung(val displayName: String) {
    SI("Systemintegration"),
    AE("Anwendungsentwicklung"),
    GEMEINSAM("Gemeinsam (SI + AE)")
}

enum class ExamType(val displayName: String) {
    AP1("AP1 – Abschlussprüfung Teil 1"),
    AP2("AP2 – Abschlussprüfung Teil 2")
}
