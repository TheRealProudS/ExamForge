package de.sysitprep.app.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.sysitprep.app.ExamForgeApp
import de.sysitprep.app.data.model.QuizResult
import de.sysitprep.app.data.model.QuestionResult
import de.sysitprep.app.data.model.Session
import de.sysitprep.app.data.repository.QuestionRepository
import kotlinx.coroutines.launch

class ResultViewModel : ViewModel() {

    private val repository: QuestionRepository by lazy { ExamForgeApp.instance.repository }

    private val _quizResult = MutableLiveData<QuizResult?>()
    val quizResult: LiveData<QuizResult?> = _quizResult

    fun loadResult(sessionId: Long) {
        viewModelScope.launch {
            val session = repository.getSessionById(sessionId) ?: return@launch
            val answers = repository.getAnswersForSession(sessionId)

            val questionResults = answers.map { answer ->
                val question = repository.getQuestionById(answer.questionId)
                QuestionResult(
                    questionId = answer.questionId,
                    questionText = question?.questionText ?: "",
                    options = question?.options ?: emptyList(),
                    correctIndices = question?.correctAnswerIndices ?: emptyList(),
                    selectedIndices = answer.selectedIndices,
                    isCorrect = answer.isCorrect,
                    explanation = question?.explanation ?: ""
                )
            }

            val total = questionResults.size
            val correct = questionResults.count { it.isCorrect }
            val scorePercent = if (total > 0) (correct * 100 / total) else 0
            val passed = scorePercent >= 50
            val timeSeconds = session.durationSeconds ?: 0

            _quizResult.value = QuizResult(
                sessionId = sessionId,
                totalQuestions = total,
                correctAnswers = correct,
                wrongAnswers = total - correct,
                skippedAnswers = questionResults.count { it.selectedIndices.isEmpty() },
                scorePercent = scorePercent,
                passed = passed,
                timeSeconds = timeSeconds,
                questionResults = questionResults
            )
        }
    }
}
