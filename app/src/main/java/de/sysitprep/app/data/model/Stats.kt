package de.sysitprep.app.data.model


data class UserStats(
    val totalAnswered: Int = 0,
    val correctAnswers: Int = 0,
    val completedSessions: Int = 0,
    val totalStudySeconds: Int = 0,
    val streakDays: Int = 0,
    val lastStudiedAt: Long = 0L
) {
    val accuracy: Float get() = if (totalAnswered == 0) 0f else correctAnswers.toFloat() / totalAnswered.toFloat()
    val accuracyPercent: Int get() = (accuracy * 100).toInt()
}


data class LernfeldProgress(
    val lernfeld: Int,
    val title: String = "Lernfeld $lernfeld",
    val totalQuestions: Int,
    val correctAnswers: Int
) {
    val progressPercent: Int get() = if (totalQuestions == 0) 0 else (correctAnswers.toFloat() / totalQuestions * 100).toInt()
}

data class QuizResult(
    val sessionId: Long,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val wrongAnswers: Int,
    val skippedAnswers: Int,
    val scorePercent: Int,
    val passed: Boolean,
    val timeSeconds: Int,
    val questionResults: List<QuestionResult>
)

data class QuestionResult(
    val questionId: Long,
    val questionText: String,
    val options: List<String>,
    val correctIndices: List<Int>,
    val selectedIndices: List<Int>,
    val isCorrect: Boolean,
    val explanation: String
)

