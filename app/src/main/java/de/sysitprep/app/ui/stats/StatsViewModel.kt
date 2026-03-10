package de.sysitprep.app.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.sysitprep.app.ExamForgeApp
import de.sysitprep.app.data.model.LernfeldProgress
import de.sysitprep.app.data.model.Session
import de.sysitprep.app.data.model.UserStats
import de.sysitprep.app.data.repository.QuestionRepository
import kotlinx.coroutines.launch

class StatsViewModel : ViewModel() {

    private val repository: QuestionRepository by lazy { ExamForgeApp.instance.repository }

    private val _userStats = MutableLiveData<UserStats?>()
    val userStats: LiveData<UserStats?> = _userStats

    private val _lernfeldProgress = MutableLiveData<List<LernfeldProgress>>()
    val lernfeldProgress: LiveData<List<LernfeldProgress>> = _lernfeldProgress

    private val _recentSessions = MutableLiveData<List<Session>>()
    val recentSessions: LiveData<List<Session>> = _recentSessions

    fun loadStats() {
        viewModelScope.launch {
            val stats = repository.getUserStats()
            _userStats.value = stats
            _lernfeldProgress.value = repository.getLernfeldProgress()
            _recentSessions.value = repository.getRecentSessionsList(10)
        }
    }
}
