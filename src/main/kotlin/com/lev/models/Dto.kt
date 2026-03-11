package com.lev.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

// getGames
@Serializable
data class GameListItem(val gameId: Int, val date: Long)

@Serializable
data class GetGamesResponse(val games: List<GameListItem>)

// newgame
@Serializable
data class NewGameRequest(val playerId: Int, val playerName: String)

@Serializable
data class NewGameResponse(val gameId: Int)

// addPlayer
@Serializable
data class AddPlayerRequest(val playerId: Int, val playerName: String, val gameId: Int)

@Serializable
data class AddPlayerResponse(val gameId: Int)

// GetPlayers
@Serializable
data class GetPlayersRequest(val gameId: Int)

@Serializable
data class GetPlayersResponse(val gameId: Int, val players: List<Player>)

// sendPole / getPole — храним как JsonObject для гибкости схемы
@Serializable
data class SendPoleRequest(
    val gameId: Int,
    val hod_player: Int,
    val economic: JsonObject,
    val listPlayer: List<JsonObject>,
    val pole: JsonObject
)

// getPole
@Serializable
data class GetPoleRequest(val gameId: Int)

// getPole response — тот же формат
@Serializable
data class GetPoleResponse(
    val gameId: Int,
    val hod_player: Int,
    val economic: JsonObject,
    val listPlayer: List<JsonObject>,
    val pole: JsonObject
)

// gameover
@Serializable
data class GameOverRequest(val gameId: Int)
