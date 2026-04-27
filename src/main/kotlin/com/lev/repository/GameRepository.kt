package com.lev.repository

import com.lev.models.Game
import com.lev.models.GameStatus
import com.lev.models.Player
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive  // ← ДОБАВЬТЕ ЭТОТ ИМПОРТ
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import java.util.concurrent.atomic.AtomicInteger

object GameRepository {
    private val games = mutableMapOf<Int, Game>()
    private val nextGameId = AtomicInteger(1)

    fun getAllGames(): List<Game> = games.values.filter { it.status == GameStatus.NEW }.toList()

    fun getGame(gameId: Int): Game? = games[gameId]

    fun createGame(playerId: Int, playerName: String): Game {
        val gameId = nextGameId.getAndIncrement()
        val game = Game(
            gameId = gameId,
            status = GameStatus.NEW,
            players = mutableListOf(Player(playerId, playerName)),
            createdAt = System.currentTimeMillis()
        )
        games[gameId] = game
        return game
    }

    fun addPlayer(gameId: Int, playerId: Int, playerName: String): Game? {
        val game = games[gameId] ?: return null
        if (game.players.any { it.playerId == playerId }) return game
        game.players.add(Player(playerId, playerName))
        return game
    }

    fun setPoleData(gameId: Int, hodPlayer: Int, economic: JsonObject, listPlayer: List<JsonObject>, pole: JsonObject): Boolean {
        val game = games[gameId] ?: return false
        game.poleData = buildJsonObject {
            put("hod_player", JsonPrimitive(hodPlayer))  // ← ИСПРАВЛЕНО: обернули в JsonPrimitive
            put("economic", economic)
            put("listPlayer", buildJsonArray { listPlayer.forEach { add(it) } })
            put("pole", pole)
        }
        return true
    }

    fun getPoleData(gameId: Int): JsonObject? = games[gameId]?.poleData

    fun setGameOver(gameId: Int): Boolean {
        val game = games[gameId] ?: return false
        games[gameId] = game.copy(status = GameStatus.GAMEOVER)
        return true
    }

    fun setStatus(gameId: Int, status: GameStatus): Boolean {
        val game = games[gameId] ?: return false
        games[gameId] = game.copy(status = status)
        return true
    }
}