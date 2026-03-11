package com.lev.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

enum class GameStatus {
    NEW,
    INGAME,
    GAMEOVER
}

@Serializable
data class Player(
    val playerId: Int,
    val playerName: String
)

@Serializable
data class Game(
    val gameId: Int,
    val status: GameStatus,
    val players: MutableList<Player>,
    val createdAt: Long,
    var poleData: JsonObject? = null
)
