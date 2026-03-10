package de.sysitprep.app.ui.lernfelder

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.sysitprep.app.ExamForgeApp
import de.sysitprep.app.data.model.Fachrichtung
import de.sysitprep.app.data.model.Lernfeld
import de.sysitprep.app.data.model.LernfelderData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LernfelderViewModel(application: Application) : AndroidViewModel(application) {

    private val app = application as ExamForgeApp
    private val repo = app.repository

    private val _lernfelder = MutableLiveData<List<LernfeldUiItem>>()
    val lernfelder: LiveData<List<LernfeldUiItem>> = _lernfelder

    private var currentFachrichtung = Fachrichtung.GEMEINSAM

    init {
        viewModelScope.launch {
            app.seedingComplete.first { it }
            loadLernfelder(currentFachrichtung)
        }
    }

    fun loadLernfelder(fachrichtung: Fachrichtung) {
        currentFachrichtung = fachrichtung
        viewModelScope.launch {
            val items = LernfelderData.getLernfelderForFachrichtung(fachrichtung).map { lf ->
                val questionCount = repo.getQuestionsForLernfeld(lf.number, fachrichtung, 200).size
                LernfeldUiItem(
                    lernfeld = lf,
                    questionCount = questionCount,
                    progress = 0,
                    fachrichtung = fachrichtung
                )
            }
            _lernfelder.postValue(items)
        }
    }

    fun refreshCurrentTab() {
        viewModelScope.launch {
            app.seedingComplete.first { it }
            loadLernfelder(currentFachrichtung)
        }
    }
}

data class LernfeldUiItem(
    val lernfeld: Lernfeld,
    val questionCount: Int,
    val progress: Int,
    val fachrichtung: Fachrichtung
)
