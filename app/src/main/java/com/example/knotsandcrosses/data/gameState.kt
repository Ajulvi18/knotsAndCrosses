package com.example.knotsandcrosses.data

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class game(
    @SerializedName("players")
    var players:MutableList<String>,
    @SerializedName("gameId")
    val gameId:String,
    @SerializedName("state")
    var state:MutableList<MutableList<String>>
)


public fun fromJson(json:String):game{
    return Gson().fromJson<game>(json, game::class.java)
}

public fun toJson(example: game):String{
    return Gson().toJson(example)
}
