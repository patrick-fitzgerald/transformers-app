package com.example.util

import com.example.data.BattleResult
import com.example.data.response.Transformer
import com.example.util.Constants.TEAM_AUTOBOT
import com.example.util.Constants.TEAM_DECEPTICON
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class BattleHelperTest {

    @Test
    fun compareCourage_similarCourage() {
        val autobot = Transformer(courage = 10)
        val decepticon = Transformer(courage = 7)
        Assert.assertEquals(
            BattleResult.NO_RESULT,
            BattleHelper.compareCourage(autobot, decepticon)
        )
    }

    @Test
    fun compareCourage_autobotHighCourage() {
        val autobot = Transformer(courage = 10)
        val decepticon = Transformer(courage = 6)
        Assert.assertEquals(
            BattleResult.AUTOBOT_WINS,
            BattleHelper.compareCourage(autobot, decepticon)
        )
    }

    @Test
    fun compareCourage_autobotLowCourage() {
        val autobot = Transformer(courage = 6)
        val decepticon = Transformer(courage = 10)
        Assert.assertEquals(
            BattleResult.DECEPTICON_WINS,
            BattleHelper.compareCourage(autobot, decepticon)
        )
    }

    @Test
    fun transformerBattle_autobotLowCourage() {
        val autobot = Transformer(courage = 6)
        val decepticon = Transformer(courage = 10)
        Assert.assertEquals(
            BattleResult.DECEPTICON_WINS,
            BattleHelper.transformerBattle(autobot, decepticon)
        )
    }

    @Test
    fun startBattle_autobotLowCourage() {
        val autobot = Transformer(courage = 6, team = TEAM_AUTOBOT)
        val decepticon = Transformer(courage = 10, team = TEAM_DECEPTICON)
        val battleResult = BattleHelper.startBattle(listOf(autobot, decepticon))

        Assert.assertEquals(
            battleResult.battleResults.size,
            1
        )

        Assert.assertEquals(
            battleResult.battleResults[0].battleResult,
            BattleResult.DECEPTICON_WINS
        )

        Assert.assertEquals(
            battleResult.autobotSurvivors.size,
            0
        )

        Assert.assertEquals(
            battleResult.decepticonSurvivors.size,
            0
        )
        Assert.assertEquals(
            battleResult.allDestroyed,
            false
        )
    }

    @Test
    fun startBattle_Predaking() {
        val autobot = Transformer(courage = 10, strength = 10, team = TEAM_AUTOBOT)
        val decepticon =
            Transformer(name = "Predaking", courage = 0, strength = 0, team = TEAM_DECEPTICON)
        val battleResult = BattleHelper.startBattle(listOf(autobot, decepticon))

        Assert.assertEquals(
            battleResult.battleResults.size,
            1
        )

        Assert.assertEquals(
            battleResult.battleResults[0].battleResult,
            BattleResult.DECEPTICON_WINS
        )

        Assert.assertEquals(
            battleResult.autobotSurvivors.size,
            0
        )

        Assert.assertEquals(
            battleResult.decepticonSurvivors.size,
            0
        )
        Assert.assertEquals(
            battleResult.allDestroyed,
            false
        )
    }

    @Test
    fun startBattle_allDestroyed() {
        val autobot = Transformer(name = "Optimus Prime", courage = 6, team = TEAM_AUTOBOT)
        val decepticon = Transformer(name = "Predaking", courage = 10, team = TEAM_DECEPTICON)
        val battleResult = BattleHelper.startBattle(listOf(autobot, decepticon))

        Assert.assertEquals(
            battleResult.battleResults.size,
            1
        )

        Assert.assertEquals(
            battleResult.battleResults[0].battleResult,
            BattleResult.ALL_DESTROYED
        )

        Assert.assertEquals(
            battleResult.autobotSurvivors.size,
            0
        )

        Assert.assertEquals(
            battleResult.decepticonSurvivors.size,
            0
        )

        Assert.assertEquals(
            battleResult.allDestroyed,
            true
        )
    }
}
