package com.example.data.response

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.example.util.Constants.TEAM_AUTOBOT
import com.google.gson.annotations.SerializedName

@Entity(primaryKeys = ["id"], tableName = "transformers")
data class Transformer(

    @SerializedName("id")
    val id: String = "",
    val name: String = "",
    val team: String = "",
    val strength: Int = 1,
    val intelligence: Int = 1,
    val speed: Int = 1,
    val endurance: Int = 1,
    val rank: Int = 1,
    var courage: Int = 1,
    val firepower: Int = 1,
    val skill: Int = 1,
    @ColumnInfo(name = "team_icon")
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

    fun hasSpecialPowers(): Boolean {
        return name == "Optimus Prime" || name == "Predaking"
    }

}
