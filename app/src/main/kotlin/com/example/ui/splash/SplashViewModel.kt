package com.example.ui.splash

import androidx.lifecycle.viewModelScope
import com.example.api.Status
import com.example.repository.TransformersRepository
import com.example.ui.base.BaseViewModel
import com.example.util.PreferenceHelper
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashViewModel(
    private val transformersRepository: TransformersRepository,
    private val prefs: PreferenceHelper
) : BaseViewModel() {

    enum class ContextEvent {
        NAVIGATE_TO_HOME_FRAGMENT,
    }

    val contextEventBus: PublishSubject<ContextEvent> = PublishSubject.create()

    fun navigateUser() {
        // User auth logic
        if (prefs.jwtToken.isNullOrEmpty()) {
            getAllSparkRequest()
        } else {
            contextEventBus.onNext(ContextEvent.NAVIGATE_TO_HOME_FRAGMENT)
        }
    }

    private fun getAllSparkRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = transformersRepository.getAllSpark()
            viewModelScope.launch(Dispatchers.Main) {
                when (response.status) {
                    Status.SUCCESS -> {
                            Timber.d("getAllSparkRequest response: ${response.data}")
                            prefs.jwtToken = response.data
                            contextEventBus.onNext(ContextEvent.NAVIGATE_TO_HOME_FRAGMENT)

                    }
                    Status.ERROR -> {
                        showError("getAllSparkRequest ERROR: ${response.message}")
                    }
                }
            }
        }
    }
}
