package com.example.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.api.Status
import com.example.repository.TransformersRepository
import com.example.ui.base.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashViewModel(
    private val transformersRepository: TransformersRepository
) : BaseViewModel(){

    val jwtToken = MutableLiveData<String>()

    fun getAllSparkRequest() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = transformersRepository.getAllSpark()
            viewModelScope.launch(Dispatchers.Main) {
                when (response.status) {
                    Status.SUCCESS -> {
                        if (response.data != null) {
                            Timber.d("getAllSparkRequest response: ${response.data}")
                            jwtToken.value = response.data
                        } else {
                            Timber.e("getAllSparkRequest ERROR: ${response.data}")
                        }
                    }
                    Status.ERROR -> {
                        Timber.e("getAllSparkRequest ERROR: ${response.message}")
                    }
                }
            }
        }
    }



}
