package com.example.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.api.Resource
import com.example.api.Status
import com.example.data.response.TransformerListResponse
import com.example.data.response.TransformerResponse
import com.example.repository.TransformersRepository
import com.example.ui.base.BaseViewModel
import com.example.util.extensions.default
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class HomeViewModel(
    private val transformersRepository: TransformersRepository
) : BaseViewModel() {

    val isLoading = MutableLiveData<Boolean>().default(true)
    val transformers = MutableLiveData<List<TransformerResponse>>()

    enum class ContextEvent {
        NAVIGATE_TO_TRANSFORMER_FRAGMENT,
    }

    val contextEventBus: PublishSubject<ContextEvent> = PublishSubject.create()

    fun onAddAutobotBtnClick() {
        contextEventBus.onNext(ContextEvent.NAVIGATE_TO_TRANSFORMER_FRAGMENT)
    }

    fun onAddDecepticonBtnClick() {
        contextEventBus.onNext(ContextEvent.NAVIGATE_TO_TRANSFORMER_FRAGMENT)
    }

    fun getTransformersRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val response: Resource<TransformerListResponse> =
                transformersRepository.getTransformers()
            isLoading.postValue(false)
            viewModelScope.launch(Dispatchers.Main) {
                when (response.status) {
                    Status.SUCCESS -> {
                        if (response.data != null) {
                            Timber.d("getTransformersRequest response: ${response.data}")
                            transformers.value = response.data.transformers
                        } else {
                            showError("getTransformersRequest ERROR: ${response.data}")
                        }
                    }
                    Status.ERROR -> {
                        showError("getTransformersRequest ERROR: ${response.message}")
                    }
                }
            }
        }
    }
}
