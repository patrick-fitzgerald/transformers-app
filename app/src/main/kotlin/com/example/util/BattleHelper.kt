package com.example.util

import com.example.data.BattleResult
import com.example.data.SingleBattleResult
import com.example.data.TotalBattleResult
import com.example.data.response.Transformer

class BattleHelper {

    companion object {
        fun startBattle(transformers: List<Transformer>): TotalBattleResult {

            val battleResults = mutableListOf<SingleBattleResult>()
            var allDestroyed = false

            // autobots sorted by rank
            val autobots = transformers
                .filter { it.team == Constants.TEAM_AUTOBOT }
                .sortedBy { it.rank }

            // decepticons sorted by rank
            val decepticons = transformers
                .filter { it.team == Constants.TEAM_DECEPTICON }
                .sortedBy { it.rank }

            val autobotsIterator = autobots.iterator()
            val decepticonsIterator = decepticons.iterator()

            // autobots vs decepticons battle
            while (autobotsIterator.hasNext() && decepticonsIterator.hasNext()) {
                val autobot = autobotsIterator.next()
                val decepticon = decepticonsIterator.next()

                val battleResult = transformerBattle(autobot, decepticon)
                val singleBattleResult = SingleBattleResult(
                    autobot = autobot,
                    decepticon = decepticon,
                    battleResult = battleResult
                )
                battleResults.add(singleBattleResult)
                if (singleBattleResult.battleResult == BattleResult.ALL_DESTROYED) {
                    allDestroyed = true
                }
            }

            // autobots survivors
            val autobotSurvivors = mutableListOf<Transformer>()

            // decepticons survivors
            val decepticonSurvivors = mutableListOf<Transformer>()

            if (!allDestroyed) {
                while (autobotsIterator.hasNext()) {
                    autobotSurvivors.add(autobotsIterator.next())
                }
                while (decepticonsIterator.hasNext()) {
                    decepticonSurvivors.add(decepticonsIterator.next())
                }
            }

            // battle result summary
            return TotalBattleResult(
                transformers = transformers,
                allDestroyed = allDestroyed,
                battleResults = battleResults,
                autobotSurvivors = autobotSurvivors,
                decepticonSurvivors = decepticonSurvivors
            )
        }

        fun transformerBattle(autobot: Transformer, decepticon: Transformer): BattleResult {

            val compareSpecialCasesResult = compareSpecialCases(autobot, decepticon)
            if (compareSpecialCasesResult != BattleResult.NO_RESULT) return compareSpecialCasesResult

            val compareCourageResult = compareCourage(autobot, decepticon)
            if (compareCourageResult != BattleResult.NO_RESULT) return compareCourageResult

            val compareStrengthResult = compareStrength(autobot, decepticon)
            if (compareStrengthResult != BattleResult.NO_RESULT) return compareStrengthResult

            val compareSkillResult = compareSkill(autobot, decepticon)
            if (compareSkillResult != BattleResult.NO_RESULT) return compareSkillResult

            val compareOverallRatingResult = compareOverallRating(autobot, decepticon)
            if (compareOverallRatingResult != BattleResult.NO_RESULT) return compareOverallRatingResult

            return BattleResult.NO_RESULT
        }

        fun compareCourage(autobot: Transformer, decepticon: Transformer): BattleResult {
            return when {
                autobot.courage - decepticon.courage >= 4 -> {
                    BattleResult.AUTOBOT_WINS
                }
                decepticon.courage - autobot.courage >= 3 -> {
                    BattleResult.DECEPTICON_WINS
                }
                else -> {
                    BattleResult.NO_RESULT
                }
            }
        }

        fun compareStrength(autobot: Transformer, decepticon: Transformer): BattleResult {
            return when {
                autobot.strength - decepticon.strength >= 3 -> {
                    BattleResult.AUTOBOT_WINS
                }
                decepticon.strength - autobot.strength >= 3 -> {
                    BattleResult.DECEPTICON_WINS
                }
                else -> {
                    BattleResult.NO_RESULT
                }
            }
        }

        fun compareSkill(autobot: Transformer, decepticon: Transformer): BattleResult {
            return when {
                autobot.skill - decepticon.skill >= 3 -> {
                    BattleResult.AUTOBOT_WINS
                }
                decepticon.skill - autobot.skill >= 3 -> {
                    BattleResult.DECEPTICON_WINS
                }
                else -> {
                    BattleResult.NO_RESULT
                }
            }
        }

        fun compareOverallRating(autobot: Transformer, decepticon: Transformer): BattleResult {
            return when {
                autobot.overallRating() > decepticon.overallRating() -> {
                    BattleResult.AUTOBOT_WINS
                }
                decepticon.overallRating() > autobot.overallRating() -> {
                    BattleResult.DECEPTICON_WINS
                }
                else -> {
                    BattleResult.NO_RESULT
                }
            }
        }

        fun compareSpecialCases(autobot: Transformer, decepticon: Transformer): BattleResult {
            return when {

                decepticon.hasSpecialPowers() && autobot.hasSpecialPowers() -> {
                    BattleResult.ALL_DESTROYED
                }
                autobot.hasSpecialPowers() -> {
                    BattleResult.AUTOBOT_WINS
                }
                decepticon.hasSpecialPowers() -> {
                    BattleResult.DECEPTICON_WINS
                }
                else -> {
                    BattleResult.NO_RESULT
                }
            }
        }
    }
}
