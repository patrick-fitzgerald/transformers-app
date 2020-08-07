package com.example.data.response

import com.google.gson.annotations.SerializedName

data class TransformerResponse(

    val id: String? = null,
    val name: String = "",
    val team: String = "",
    val strength: Int,
    val intelligence: Int,
    val speed: Int,
    val endurance: Int,
    val rank: Int,
    val courage: Int,
    val firepower: Int,
    val skill: Int,
    @SerializedName("team_icon")
    val teamIcon: String = ""

) {

    /**
     * The overall rating of a Transformer is the following formula:
     * (Strength + Intelligence + Speed + Endurance + Firepower).
     */
    fun overallRating(): String {
        val rating = strength + intelligence + speed + endurance + firepower
        return "Overall rating: $rating"
    }
}
