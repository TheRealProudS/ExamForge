package de.sysitprep.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import de.sysitprep.app.data.database.Converters


@Entity(tableName = "questions")
@TypeConverters(Converters::class)
data class Question(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val questionKey: String,
    val title: String,
    val questionText: String,
    val options: List<String>,
    val correctAnswerIndices: List<Int>,
    val explanation: String = "",
    val lernfeld: Int,
    val fachrichtung: Fachrichtung,
    val examType: ExamType,
    val year: Int = 0,
    val difficulty: Int = 1,
    val isBookmarked: Boolean = false,
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
