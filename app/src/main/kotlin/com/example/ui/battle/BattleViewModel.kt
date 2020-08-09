package com.example.ui.battle

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.api.Status
import com.example.data.BattleResult
import com.example.repository.TransformersRepository
import com.example.ui.base.BaseViewModel
import com.example.ui.transformer.TransformerViewModel
import com.example.util.BattleHelper
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

    val transformers = transformersRepository.getTransformersFromDb()

    val battleResult = MutableLiveData<String>()
    val autoBotSurvivors = MutableLiveData<String>()
    val decepticonSurvivors = MutableLiveData<String>()
    val battlesText = MutableLiveData<String>()

    fun closeView() {
        contextEventBus.onNext(ContextEvent.NAVIGATE_TO_HOME_FRAGMENT)
    }

    fun getBattleResult() {
        transformers.value?.let { transformers ->

            val totalBattleResult = BattleHelper.startBattle(transformers)

            battleResult.value = totalBattleResult.winningTeam(context)
            battlesText.value = totalBattleResult.battleResults.map {
                val battle = "(A) ${it.autobot.name} vs (D) ${it.decepticon.name}\n"
                val result = when (it.battleResult) {
                    BattleResult.AUTOBOT_WINS -> "🏆 ${it.autobot.name} won\n\n"
                    BattleResult.DECEPTICON_WINS -> "🏆 ${it.decepticon.name} won \n\n"
                    BattleResult.NO_RESULT -> "Draw\n\n"
                    BattleResult.ALL_DESTROYED -> "All Destroyed\n\n"
                }
                battle + result
            }.joinToString("")

            if (totalBattleResult.autobotSurvivors.isEmpty()) {
                autoBotSurvivors.value = context.getString(R.string.none)
            } else {
                autoBotSurvivors.value =
                    totalBattleResult.autobotSurvivors.joinToString(",") { it.name }
            }

            if (totalBattleResult.decepticonSurvivors.isEmpty()) {
                decepticonSurvivors.value = context.getString(R.string.none)
            } else {
                decepticonSurvivors.value =
                    totalBattleResult.decepticonSurvivors.joinToString(",") { it.name }
            }
        }

    }

}
