package com.example.data

import android.content.Context
import com.example.R
import com.example.data.response.Transformer

class TotalBattleResult(
    val allDestroyed: Boolean = false,
    val transformers: List<Transformer>,
    val battleResults: List<SingleBattleResult>,
    val autobotSurvivors: List<Transformer>,
    val decepticonSurvivors: List<Transformer>
) {

    fun winningTeam(context: Context): String {
        if (allDestroyed) return context.getString(R.string.all_destroyed)
        val autobotWins = battleResults.count { it.battleResult == BattleResult.AUTOBOT_WINS }
        val decepticonWins = battleResults.count { it.battleResult == BattleResult.DECEPTICON_WINS }

        return when {
            autobotWins > decepticonWins -> context.getString(R.string.autobots_won)
            decepticonWins > autobotWins -> context.getString(R.string.decepticons_won)
            else -> context.getString(R.string.draw)
        }
    }
}
