package de.sysitprep.app.data.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.sysitprep.app.data.model.ExamType
import de.sysitprep.app.data.model.Fachrichtung
import de.sysitprep.app.data.model.SessionType

class Converters {
    private val gson = Gson()

    @TypeConverter fun fromIntList(value: List<Int>): String = gson.toJson(value)
    @TypeConverter fun toIntList(value: String): List<Int> =
        gson.fromJson(value, object : TypeToken<List<Int>>() {}.type) ?: emptyList()

    @TypeConverter fun fromStringList(value: List<String>): String = gson.toJson(value)
    @TypeConverter fun toStringList(value: String): List<String> =
        gson.fromJson(value, object : TypeToken<List<String>>() {}.type) ?: emptyList()

    @TypeConverter fun fromLongList(value: List<Long>): String = gson.toJson(value)
    @TypeConverter fun toLongList(value: String): List<Long> =
        gson.fromJson(value, object : TypeToken<List<Long>>() {}.type) ?: emptyList()

    @TypeConverter fun fromFachrichtung(value: Fachrichtung): String = value.name
    @TypeConverter fun toFachrichtung(value: String): Fachrichtung = Fachrichtung.valueOf(value)

    @TypeConverter fun fromExamType(value: ExamType?): String? = value?.name
    @TypeConverter fun toExamType(value: String?): ExamType? = value?.let { ExamType.valueOf(it) }

    @TypeConverter fun fromSessionType(value: SessionType): String = value.name
    @TypeConverter fun toSessionType(value: String): SessionType = SessionType.valueOf(value)
}
