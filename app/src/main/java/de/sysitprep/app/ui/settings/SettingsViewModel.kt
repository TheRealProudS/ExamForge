package de.sysitprep.app.ui.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.sysitprep.app.ExamForgeApp
import de.sysitprep.app.data.model.Fachrichtung
import de.sysitprep.app.data.repository.QuestionRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {

    private val repository: QuestionRepository by lazy { ExamForgeApp.instance.repository }

    private val _fachrichtung = MutableLiveData<Fachrichtung>()
    val fachrichtung: LiveData<Fachrichtung> = _fachrichtung

    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> = _isDarkMode

    private val _isSoundEnabled = MutableLiveData<Boolean>()
    val isSoundEnabled: LiveData<Boolean> = _isSoundEnabled

    fun loadSettings() {
        viewModelScope.launch {
            val prefs = repository.getPreferences().first()
            _fachrichtung.value = prefs.fachrichtung
            _isDarkMode.value = prefs.isDarkMode
            _isSoundEnabled.value = prefs.isSoundEnabled
        }
    }

    fun setFachrichtung(fachrichtung: Fachrichtung) {
        _fachrichtung.value = fachrichtung
        viewModelScope.launch { repository.saveFachrichtung(fachrichtung) }
    }

    fun setDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
        viewModelScope.launch { repository.saveDarkMode(enabled) }
        val mode = if (enabled) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        AppCompatDelegate.setDefaultNightMode(mode)
    }

    fun setSoundEnabled(enabled: Boolean) {
        _isSoundEnabled.value = enabled
        viewModelScope.launch { repository.saveSoundEnabled(enabled) }
    }

    fun resetProgress() {
        viewModelScope.launch { repository.resetAllProgress() }
    }
}
