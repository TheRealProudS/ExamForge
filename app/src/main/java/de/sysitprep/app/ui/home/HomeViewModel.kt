package de.sysitprep.app.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.sysitprep.app.ExamForgeApp
import de.sysitprep.app.data.model.UserStats
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repo = (application as ExamForgeApp).repository

    private val _stats = MutableLiveData<UserStats>()
    val stats: LiveData<UserStats> = _stats

    private val _bookmarkCount = MutableLiveData<Int>()
    val bookmarkCount: LiveData<Int> = _bookmarkCount

    init {
        loadStats()
        observeBookmarks()
    }

    private fun loadStats() {
        viewModelScope.launch {
            val stats = repo.getUserStats()
            _stats.value = stats
        }
    }

    private fun observeBookmarks() {
        viewModelScope.launch {
            repo.getBookmarkedQuestions().collect { bookmarks ->
                _bookmarkCount.value = bookmarks.size
            }
        }
    }

    fun refreshStats() = loadStats()
}
