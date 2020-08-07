package com.example.ui.transformer

import androidx.lifecycle.viewModelScope
import com.example.api.Status
import com.example.repository.TransformersRepository
import com.example.ui.base.BaseViewModel
import com.example.util.PreferenceHelper
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class TransformerViewModel(
    private val transformersRepository: TransformersRepository
) : BaseViewModel() {

    enum class ContextEvent {
        NAVIGATE_TO_HOME_FRAGMENT,
    }

    val contextEventBus: PublishSubject<ContextEvent> = PublishSubject.create()

//    private fun getAllSparkRequest() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val response = transformersRepository.getAllSpark()
//            viewModelScope.launch(Dispatchers.Main) {
//                when (response.status) {
//                    Status.SUCCESS -> {
//                        if (response.data != null) {
//                            Timber.d("getAllSparkRequest response: ${response.data}")
//                            prefs.jwtToken = response.data
//                            contextEventBus.onNext(ContextEvent.NAVIGATE_TO_HOME_FRAGMENT)
//                        } else {
//                            showError("getAllSparkRequest ERROR: ${response.data}")
//                        }
//                    }
//                    Status.ERROR -> {
//                        showError("getAllSparkRequest ERROR: ${response.message}")
//                    }
//                }
//            }
//        }
//    }
}
