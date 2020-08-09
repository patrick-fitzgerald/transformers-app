package com.example.ui.transformer

import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.api.Resource
import com.example.api.Status
import com.example.data.request.CreateTransformerRequest
import com.example.data.request.UpdateTransformerRequest
import com.example.data.response.Transformer
import com.example.data.response.TransformerListResponse
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
    val isLoading = MutableLiveData<Boolean>().default(false)
    val viewTitle = MutableLiveData<String>()
    var transformerViewType: TransformerViewType = TransformerViewType.CREATE

    var transformerTeam = ""
    var transformerId = ""
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
            return
        }

        when (transformerViewType) {

            TransformerViewType.CREATE -> {
                createTransformerRequest()
            }
            TransformerViewType.VIEW -> {
                updateTransformerRequest()
            }

        }
    }

    fun onRandomValuesCtaClick() {
        transformerStrength.value = (1..10).random()
        transformerIntelligence.value = (1..10).random()
        transformerSpeed.value = (1..10).random()
        transformerEndurance.value = (1..10).random()
        transformerRank.value = (1..10).random()
        transformerCourage.value = (1..10).random()
        transformerFirepower.value = (1..10).random()
        transformerSkill.value = (1..10).random()
    }

    fun onDeleteCtaClick() {
        deleteTransformerRequest(transformerId)
    }

    fun isCreateMode(): Boolean {
        return transformerViewType == TransformerViewType.CREATE
    }

    fun getTransformerRequest(transformerId: String) {
        Timber.d("lookupTransformer with id: $transformerId")
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val response = transformersRepository.getTransformer(transformerId)
            isLoading.postValue(false)
            viewModelScope.launch(Dispatchers.Main) {
                when (response.status) {
                    Status.SUCCESS -> {
                        Timber.d("getTransformerRequest response: ${response.data}")
                        parseTransformer(response.data)
                    }
                    Status.ERROR -> {
                        showError("getTransformerRequest ERROR: ${response.message}")
                    }
                }
            }
        }
    }


    private fun deleteTransformerRequest(transformerId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val response = transformersRepository.deleteTransformer(transformerId)
            isLoading.postValue(false)
            viewModelScope.launch(Dispatchers.Main) {
                when (response.status) {
                    Status.SUCCESS -> {
                        Timber.d("deleteTransformerRequest response: ${response.data}")
                        closeView()
                    }
                    Status.ERROR -> {
                        showError("deleteTransformerRequest ERROR: ${response.message}")
                    }
                }
            }
        }
    }


    private fun updateTransformerRequest() {
        val updateTransformerRequest = UpdateTransformerRequest(
            name = transformerName.value,
            id = transformerId,
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

        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val response = transformersRepository.putTransformer(updateTransformerRequest)
            isLoading.postValue(false)
            viewModelScope.launch(Dispatchers.Main) {
                when (response.status) {
                    Status.SUCCESS -> {
                        Timber.d("updateTransformerRequest response: ${response.data}")
                        parseTransformer(response.data)
                        closeView()
                    }
                    Status.ERROR -> {
                        showError("updateTransformerRequest ERROR: ${response.message}")
                    }
                }
            }
        }
    }

    private fun parseTransformer(transformer: Transformer?) {

        // Update MutableLiveData values

        transformer?.let {
            // Update view title
            when (it.team) {
                "A" -> viewTitle.value = context.getString(R.string.update_autobot)
                "D" -> viewTitle.value = context.getString(R.string.update_decepticon)
                else -> Unit
            }

            transformerId = it.id
            transformerTeam = it.team
            transformerName.value = it.name
            transformerStrength.value = it.strength
            transformerIntelligence.value = it.intelligence
            transformerSpeed.value = it.speed
            transformerEndurance.value = it.endurance
            transformerRank.value = it.rank
            transformerCourage.value = it.courage
            transformerFirepower.value = it.firepower
            transformerSkill.value = it.skill
        }
    }

    private fun createTransformerRequest() {

        val createTransformerRequest = CreateTransformerRequest(
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
        Timber.d("createTransformerRequest: $createTransformerRequest")
        viewModelScope.launch(Dispatchers.IO) {
            isLoading.postValue(true)
            val response = transformersRepository.postTransformer(createTransformerRequest)
            isLoading.postValue(false)
            viewModelScope.launch(Dispatchers.Main) {
                when (response.status) {
                    Status.SUCCESS -> {
                        Timber.d("createTransformerRequest response: ${response.data}")
                        closeView()
                    }
                    Status.ERROR -> {
                        showError("createTransformerRequest ERROR: ${response.message}")
                    }
                }
            }
        }
    }

    fun closeView() {
        resetValues()
        contextEventBus.onNext(ContextEvent.NAVIGATE_TO_HOME_FRAGMENT)
    }

    private fun resetValues() {
        transformerName.value = ""
        transformerStrength.value = 1
        transformerIntelligence.value =  1
        transformerSpeed.value =  1
        transformerEndurance.value =  1
        transformerRank.value =  1
        transformerCourage.value =  1
        transformerFirepower.value =  1
        transformerSkill.value =  1
    }
}
