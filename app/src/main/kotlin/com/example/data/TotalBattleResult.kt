package com.example.data

import com.example.data.response.Transformer

class TotalBattleResult(
    val allDestroyed: Boolean = false,
//    val winningTeam: String,
    val transformers: List<Transformer>,
    val battleResults: List<SingleBattleResult>,
    val autobotSurvivors: List<Transformer>,
    val decepticonSurvivors: List<Transformer>
)
