package de.sysitprep.app

import android.app.Application
import de.sysitprep.app.data.repository.QuestionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExamForgeApp : Application() {

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    val repository: QuestionRepository by lazy {
        QuestionRepository(this)
    }

    private val _seedingComplete = MutableStateFlow(false)
    val seedingComplete: StateFlow<Boolean> = _seedingComplete

    override fun onCreate() {
        super.onCreate()
        instance = this
        applicationScope.launch(Dispatchers.IO) {
            repository.seedIfEmpty()
            _seedingComplete.value = true
        }
    }

    companion object {
        lateinit var instance: ExamForgeApp
            private set
    }
}
