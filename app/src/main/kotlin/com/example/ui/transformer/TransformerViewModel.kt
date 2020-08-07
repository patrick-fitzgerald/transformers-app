package com.example.ui.transformer

import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.api.Status
import com.example.data.request.TransformerRequest
import com.example.repository.TransformersRepository
import com.example.ui.base.BaseViewModel
import com.example.util.extensions.default
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

    var transformerTeam = ""
    val transformerName = MutableLiveData<String>()
    val transformerStrength = MutableLiveData<Int>().default(1)
    val transformerIntelligence = MutableLiveData<Int>().default(1)
    val transformerSpeed = MutableLiveData<Int>().default(1)
    val transformerEndurance = MutableLiveData<Int>().default(1)
    val transformerRank = MutableLiveData<Int>().default(1)
    val transformerCourage = MutableLiveData<Int>().default(1)
    val transformerFirepower = MutableLiveData<Int>().default(1)
    val transformerSkill = MutableLiveData<Int>().default(1)

    fun onSaveCtaClick() {
        // Name validation
        if (transformerName.value.isNullOrEmpty()) {
            Toast.makeText(context, "Please enter a Transformer name", LENGTH_LONG).show()
        } else {
            createTransformerRequest()
        }
    }

    private fun createTransformerRequest() {

        val transformerRequest = TransformerRequest(
            name = transformerName.value,
            team = transformerTeam,
            strength = transformerStrength.value,
            intelligence = transformerIntelligence.value,
            speed = transformerSpeed.value,
            endurance = transformerEndurance.value,
            rank = transformerRank.value,
            courage = transformerCourage.value,
            firepower = transformerFirepower.value,
            skill = transformerSkill.value
        )
        Timber.d("transformerRequest: $transformerRequest")
        viewModelScope.launch(Dispatchers.IO) {
            val response = transformersRepository.postTransformer(transformerRequest)
            viewModelScope.launch(Dispatchers.Main) {
                when (response.status) {
                    Status.SUCCESS -> {
                        Timber.d("createTransformerRequest response: ${response.data}")
                        contextEventBus.onNext(ContextEvent.NAVIGATE_TO_HOME_FRAGMENT)
                        transformerName.value = ""
                    }
                    Status.ERROR -> {
                        showError("createTransformerRequest ERROR: ${response.message}")
                    }
                }
            }
        }
    }
}
