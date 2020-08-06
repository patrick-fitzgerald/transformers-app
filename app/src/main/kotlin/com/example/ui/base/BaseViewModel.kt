package com.example.ui.base

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

abstract class BaseViewModel : ViewModel(), KoinComponent {

    val context: Context by inject()

    fun showError(error: String) {
        Timber.e(error)
        Toast.makeText(context, error, Toast.LENGTH_LONG)
    }
}
