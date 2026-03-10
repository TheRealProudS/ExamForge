package de.sysitprep.app.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import de.sysitprep.app.data.database.AppDatabase
import de.sysitprep.app.data.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "examforge_prefs")

data class AppPreferences(
    val fachrichtung: Fachrichtung = Fachrichtung.SI,
    val isDarkMode: Boolean = false,
    val isSoundEnabled: Boolean = true
)

class QuestionRepository(private val context: Context) {

    private val db = AppDatabase.getInstance(context)
    private val questionDao = db.questionDao()
    private val sessionDao = db.sessionDao()

    suspend fun getQuestionsForLernfeld(lernfeld: Int, fachrichtung: Fachrichtung, limit: Int = 20): List<Question> =
        withContext(Dispatchers.IO) { questionDao.getByLernfeldAndFachrichtung(lernfeld, fachrichtung, limit) }

    suspend fun getQuestionsForExam(examType: ExamType, fachrichtung: Fachrichtung, limit: Int = 40): List<Question> =
        withContext(Dispatchers.IO) { questionDao.getByExamType(examType, fachrichtung, limit) }

    suspend fun getRandomQuestions(fachrichtung: Fachrichtung, limit: Int = 10): List<Question> =
        withContext(Dispatchers.IO) { questionDao.getRandomByFachrichtung(fachrichtung, limit) }

    fun getBookmarkedQuestions(): Flow<List<Question>> = questionDao.getBookmarked()

    suspend fun setBookmark(id: Long, bookmarked: Boolean) =
        withContext(Dispatchers.IO) { questionDao.setBookmark(id, bookmarked) }

    suspend fun getTotalQuestionCount(): Int =
        withContext(Dispatchers.IO) { questionDao.countAll() }

    suspend fun startSession(session: Session): Long =
        withContext(Dispatchers.IO) { sessionDao.insertSession(session) }

    suspend fun finishSession(session: Session) =
        withContext(Dispatchers.IO) { sessionDao.updateSession(session) }

    suspend fun saveAnswers(answers: List<UserAnswer>) =
        withContext(Dispatchers.IO) { sessionDao.insertAnswers(answers) }

    fun getRecentSessions(limit: Int = 20): Flow<List<Session>> = sessionDao.getRecentSessions()

    suspend fun getRecentSessionsList(limit: Int = 20): List<Session> =
        withContext(Dispatchers.IO) { sessionDao.getRecentSessionsList(limit) }

    suspend fun getCompletedSessionCount(): Int =
        withContext(Dispatchers.IO) { sessionDao.countCompletedSessions() }

    suspend fun getTotalStudyTimeSeconds(): Int =
        withContext(Dispatchers.IO) { sessionDao.getTotalStudyTimeSeconds() ?: 0 }

    suspend fun getSessionById(id: Long): Session? =
        withContext(Dispatchers.IO) { sessionDao.getSessionById(id) }

    suspend fun getAnswersForSession(sessionId: Long): List<UserAnswer> =
        withContext(Dispatchers.IO) { sessionDao.getAnswersForSession(sessionId) }

    suspend fun getQuestionById(id: Long): Question? =
        withContext(Dispatchers.IO) { questionDao.getById(id) }

    suspend fun getUserStats(): UserStats = withContext(Dispatchers.IO) {
        val totalAnswered = sessionDao.countTotalAnswered() ?: 0
        val correctAnswers = sessionDao.countTotalCorrect() ?: 0
        val completedSessions = sessionDao.countCompletedSessions()
        val totalStudySeconds = sessionDao.getTotalStudyTimeSeconds() ?: 0
        UserStats(
            totalAnswered = totalAnswered,
            correctAnswers = correctAnswers,
            completedSessions = completedSessions,
            totalStudySeconds = totalStudySeconds
        )
    }

    suspend fun getLernfeldProgress(): List<LernfeldProgress> = withContext(Dispatchers.IO) {
        (1..12).mapNotNull { lf ->
            val rows = sessionDao.getProgressForLernfeld(lf)
            if (rows.isEmpty()) return@mapNotNull null
            val total = rows.sumOf { it.totalAnswered }
            val correct = rows.sumOf { it.correctCount }
            LernfeldProgress(
                lernfeld = lf,
                title = "Lernfeld $lf",
                totalQuestions = total,
                correctAnswers = correct
            )
        }
    }

    suspend fun resetAllProgress() = withContext(Dispatchers.IO) {
        sessionDao.deleteAllSessions()
    }


    private object PrefKeys {
        val FACHRICHTUNG = stringPreferencesKey("fachrichtung")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val SEED_VERSION = stringPreferencesKey("seed_version")
    }

    fun getPreferences(): Flow<AppPreferences> = context.dataStore.data.map { prefs ->
        AppPreferences(
            fachrichtung = Fachrichtung.valueOf(prefs[PrefKeys.FACHRICHTUNG] ?: Fachrichtung.SI.name),
            isDarkMode = prefs[PrefKeys.DARK_MODE] ?: false,
            isSoundEnabled = prefs[PrefKeys.SOUND_ENABLED] ?: true
        )
    }

    suspend fun saveFachrichtung(fachrichtung: Fachrichtung) {
        context.dataStore.edit { it[PrefKeys.FACHRICHTUNG] = fachrichtung.name }
    }

    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[PrefKeys.DARK_MODE] = enabled }
    }

    suspend fun saveSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PrefKeys.SOUND_ENABLED] = enabled }
    }


    suspend fun seedIfEmpty() = withContext(Dispatchers.IO) {
        val storedVersion = context.dataStore.data.first()[PrefKeys.SEED_VERSION]
        val count = questionDao.countAll()
        if (count == 0 || storedVersion != SEED_VERSION) {
            if (count > 0) questionDao.deleteAll()
            val questions = loadQuestionsFromAssets()
            questionDao.insertAll(questions)
            context.dataStore.edit { it[PrefKeys.SEED_VERSION] = SEED_VERSION }
        }
    }

    companion object {
        private const val SEED_VERSION = "4"
    }

    private fun loadQuestionsFromAssets(): List<Question> {
        val result = mutableListOf<Question>()
        val gson = Gson()
        val assetManager = context.assets
        val files = assetManager.list("questions") ?: return emptyList()
        for (file in files) {
            if (!file.endsWith(".json")) continue
            try {
                val json = assetManager.open("questions/$file").bufferedReader().readText()
                val type = object : TypeToken<List<QuestionJson>>() {}.type
                val items: List<QuestionJson> = gson.fromJson(json, type) ?: continue
                items.forEach { item ->
                    try {
                        result.add(item.toQuestion())
                    } catch (_: Exception) { /* skip single malformed question */ }
                }
            } catch (e: Exception) {
            }
        }
        return result
    }
}

data class QuestionJson(
    val key: String?,
    val title: String?,
    val questionText: String?,
    val options: List<String>?,
    val correctAnswerIndices: List<Int>?,
    val explanation: String? = "",
    val lernfeld: Int,
    val fachrichtung: String,
    val examType: String,
    val year: Int = 0,
    val difficulty: Int = 1
) {
    fun toQuestion() = Question(
        questionKey = key.orEmpty(),
        title = title.orEmpty(),
        questionText = questionText.orEmpty(),
        options = options ?: emptyList(),
        correctAnswerIndices = correctAnswerIndices ?: emptyList(),
        explanation = explanation.orEmpty(),
        lernfeld = lernfeld,
        fachrichtung = Fachrichtung.valueOf(fachrichtung.uppercase()),
        examType = ExamType.valueOf(examType.uppercase()),
        year = year,
        difficulty = difficulty
    )
}
