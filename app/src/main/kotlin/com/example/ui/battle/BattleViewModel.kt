package com.example.ui.battle

import androidx.lifecycle.viewModelScope
import com.example.api.Status
import com.example.repository.TransformersRepository
import com.example.ui.base.BaseViewModel
import com.example.ui.transformer.TransformerViewModel
import com.example.util.PreferenceHelper
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class BattleViewModel(
    private val transformersRepository: TransformersRepository
) : BaseViewModel() {

    enum class ContextEvent {
        NAVIGATE_TO_HOME_FRAGMENT,
    }

    val contextEventBus: PublishSubject<ContextEvent> = PublishSubject.create()

    fun closeView() {
        contextEventBus.onNext(ContextEvent.NAVIGATE_TO_HOME_FRAGMENT)
    }

}
