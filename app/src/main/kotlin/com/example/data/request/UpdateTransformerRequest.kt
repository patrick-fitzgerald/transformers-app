package com.example.data.request

data class UpdateTransformerRequest (

    val id: String? = "",
    val name: String? = "",
    val team: String? = "",
    val strength: Int?,
    val intelligence: Int?,
    val speed: Int?,
    val endurance: Int?,
    val rank: Int?,
    val courage: Int?,
    val firepower: Int?,
    val skill: Int?
)