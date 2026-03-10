package de.sysitprep.app.ui.quiz

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.sysitprep.app.ExamForgeApp
import de.sysitprep.app.data.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as ExamForgeApp).repository

    // ── State ─────────────────────────────────────────────────────────────────

    private val _questions = MutableLiveData<List<Question>>()
    val questions: LiveData<List<Question>> = _questions

    private val _currentIndex = MutableLiveData(0)
    val currentIndex: LiveData<Int> = _currentIndex

    private val _currentQuestion = MutableLiveData<Question?>()
    val currentQuestion: LiveData<Question?> = _currentQuestion

    private val _selectedIndices = MutableLiveData<Set<Int>>(emptySet())
    val selectedIndices: LiveData<Set<Int>> = _selectedIndices

    private val _isAnswerChecked = MutableLiveData(false)
    val isAnswerChecked: LiveData<Boolean> = _isAnswerChecked

    private val _isCorrect = MutableLiveData(false)
    val isCorrect: LiveData<Boolean> = _isCorrect

    private val _quizFinished = MutableLiveData(false)
    val quizFinished: LiveData<Boolean> = _quizFinished

    private val _sessionId = MutableLiveData<Long>(-1)
    val sessionId: LiveData<Long> = _sessionId

    // For exam simulation: countdown timer
    private val _timerSeconds = MutableLiveData<Int>()
    val timerSeconds: LiveData<Int> = _timerSeconds

    private val _timeExpired = MutableLiveData(false)
    val timeExpired: LiveData<Boolean> = _timeExpired

    private var timerJob: Job? = null
    private var questionStartTime = System.currentTimeMillis()
    private val collectedAnswers = mutableListOf<UserAnswer>()
    private var sessionType = SessionType.QUICK_QUIZ
    private var examTimeLimitSeconds = 0
    private var activeSessionId = -1L

    // ── Setup ─────────────────────────────────────────────────────────────────

    fun init(
        type: SessionType,
        lernfeld: Int,
        fachrichtung: Fachrichtung,
        examType: ExamType,
        isExamMode: Boolean = false
    ) {
        sessionType = type
        viewModelScope.launch {
            // Wait for seeding to complete before querying
            (getApplication<ExamForgeApp>()).seedingComplete.first { it }
            val questionList = when (type) {
                SessionType.LERNFELD ->
                    repo.getQuestionsForLernfeld(lernfeld, fachrichtung, 20).shuffled()
                SessionType.EXAM_SIMULATION ->
                    repo.getQuestionsForExam(examType, fachrichtung, 40).shuffled()
                SessionType.QUICK_QUIZ ->
                    repo.getRandomQuestions(fachrichtung, 10).shuffled()
                SessionType.BOOKMARK_REVIEW ->
                    emptyList() // populated via bookmark flow
            }

            if (questionList.isEmpty()) return@launch

            _questions.postValue(questionList)
            _currentIndex.postValue(0)
            _currentQuestion.postValue(questionList.firstOrNull())
            questionStartTime = System.currentTimeMillis()

            // Create session
            val session = Session(
                sessionType = type,
                fachrichtung = fachrichtung,
                examType = if (type == SessionType.EXAM_SIMULATION) examType else null,
                lernfeld = if (type == SessionType.LERNFELD) lernfeld else null,
                totalQuestions = questionList.size
            )
            activeSessionId = repo.startSession(session)
            _sessionId.postValue(activeSessionId)

            // Start exam timer if needed
            if (type == SessionType.EXAM_SIMULATION) {
                examTimeLimitSeconds = 90 * 60 // 90 minutes
                startTimer()
            }
        }
    }

    // ── Answer Actions ────────────────────────────────────────────────────────

    fun toggleOption(index: Int) {
        if (_isAnswerChecked.value == true) return
        val current = _selectedIndices.value?.toMutableSet() ?: mutableSetOf()
        val question = _currentQuestion.value ?: return
        if (question.correctAnswerIndices.size == 1) {
            // Single choice: only one can be selected
            current.clear()
            current.add(index)
        } else {
            // Multiple choice: toggle
            if (current.contains(index)) current.remove(index) else current.add(index)
        }
        _selectedIndices.value = current
    }

    fun checkAnswer() {
        val question = _currentQuestion.value ?: return
        val selected = _selectedIndices.value ?: return
        val correctSet = question.correctAnswerIndices.toSet()
        val isCorrect = selected == correctSet
        _isCorrect.value = isCorrect
        _isAnswerChecked.value = true

        val timeSpent = ((System.currentTimeMillis() - questionStartTime) / 1000).toInt()
        val answer = UserAnswer(
            sessionId = activeSessionId,
            questionId = question.id,
            selectedIndices = selected.toList(),
            isCorrect = isCorrect,
            timeSpentSeconds = timeSpent
        )
        collectedAnswers.add(answer)
    }

    fun nextQuestion() {
        val list = _questions.value ?: return
        val nextIndex = (_currentIndex.value ?: 0) + 1
        if (nextIndex >= list.size) {
            finishQuiz()
        } else {
            _currentIndex.value = nextIndex
            _currentQuestion.value = list[nextIndex]
            _selectedIndices.value = emptySet()
            _isAnswerChecked.value = false
            questionStartTime = System.currentTimeMillis()
        }
    }

    fun skipQuestion() {
        val question = _currentQuestion.value ?: return
        val timeSpent = ((System.currentTimeMillis() - questionStartTime) / 1000).toInt()
        collectedAnswers.add(
            UserAnswer(
                sessionId = activeSessionId,
                questionId = question.id,
                selectedIndices = emptyList(),
                isCorrect = false,
                timeSpentSeconds = timeSpent
            )
        )
        nextQuestion()
    }

    fun toggleBookmark() {
        val question = _currentQuestion.value ?: return
        viewModelScope.launch {
            repo.setBookmark(question.id, !question.isBookmarked)
        }
    }

    // ── Finish ────────────────────────────────────────────────────────────────

    private fun finishQuiz() {
        timerJob?.cancel()
        viewModelScope.launch {
            repo.saveAnswers(collectedAnswers)
            val correctCount = collectedAnswers.count { it.isCorrect }
            val totalDuration = collectedAnswers.sumOf { it.timeSpentSeconds }
            repo.finishSession(
                Session(
                    id = activeSessionId,
                    sessionType = sessionType,
                    fachrichtung = Fachrichtung.SI, // replace with actual
                    totalQuestions = _questions.value?.size ?: 0,
                    correctAnswers = correctCount,
                    durationSeconds = totalDuration,
                    completedAt = System.currentTimeMillis()
                )
            )
            _quizFinished.postValue(true)
        }
    }

    fun getResult(): QuizResult {
        val total = _questions.value?.size ?: 0
        val correct = collectedAnswers.count { it.isCorrect }
        val wrong = collectedAnswers.count { !it.isCorrect && it.selectedIndices.isNotEmpty() }
        val skipped = collectedAnswers.count { it.selectedIndices.isEmpty() }
        val duration = collectedAnswers.sumOf { it.timeSpentSeconds }
        val score = if (total == 0) 0 else (correct * 100 / total)
        val questionsList = _questions.value ?: emptyList()
        return QuizResult(
            sessionId = activeSessionId,
            totalQuestions = total,
            correctAnswers = correct,
            wrongAnswers = wrong,
            skippedAnswers = skipped,
            timeSeconds = duration,
            scorePercent = score,
            passed = score >= 50,
            questionResults = collectedAnswers.mapIndexed { i, a ->
                val q = questionsList.getOrNull(i) ?: return@mapIndexed null
                QuestionResult(
                    questionId = q.id,
                    questionText = q.questionText,
                    options = q.options,
                    correctIndices = q.correctAnswerIndices,
                    selectedIndices = a.selectedIndices,
                    isCorrect = a.isCorrect,
                    explanation = q.explanation
                )
            }.filterNotNull()
        )
    }

    // ── Timer (Exam Mode) ─────────────────────────────────────────────────────

    private fun startTimer() {
        _timerSeconds.value = examTimeLimitSeconds
        timerJob = viewModelScope.launch {
            var remaining = examTimeLimitSeconds
            while (remaining > 0) {
                delay(1000)
                remaining--
                _timerSeconds.postValue(remaining)
            }
            _timeExpired.postValue(true)
            finishQuiz()
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
