package com.lev.routes

import com.lev.models.AddPlayerRequest
import com.lev.models.AddPlayerResponse
import com.lev.models.GameListItem
import com.lev.models.GetGamesResponse
import com.lev.models.GetPlayersRequest
import com.lev.models.GetPlayersResponse
import com.lev.models.GetPoleRequest
import com.lev.models.GetPoleResponse
import com.lev.models.GameOverRequest
import com.lev.models.MultiplicationRequest
import com.lev.models.MultiplicationResponse
import com.lev.models.Myclass
import com.lev.models.NewGameRequest
import com.lev.models.NewGameResponse
import com.lev.models.Result
import com.lev.models.SendPoleRequest
import com.lev.repository.GameRepository
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

fun io.ktor.server.application.Application.gameRoutes() {
    routing {
        route("v1/game") {

            post ("addMass"){
                //val req = call.receive<Int>()
                GameRepository.addMass("11")
                call.respond("В список mass добавлен 1 элемент")
            }
            get("getMass"){
                val mass = GameRepository.getMass()
                call.respond(mass)
            }

            post("myClass") {
                val req = call.receive<Myclass>()
                val result = Result(req.a1 + req.b1)
                call.respond(result)
            }
            post ("multiplicationNumbers"){
                val req = call.receive<MultiplicationRequest>()
                val multiplication = MultiplicationResponse((req.a/req.b*req.c+req.d).toDouble())
                call.respond(multiplication)
            }

            get("getGames") {
                val games = GameRepository.getAllGames().map { g ->
                    GameListItem(gameId = g.gameId, date = g.createdAt)
                }
                call.respond(GetGamesResponse(games = games))
            }

            post("newgame") {
                val req = call.receive<NewGameRequest>()
                val game = GameRepository.createGame(req.playerId, req.playerName)
                call.respond(NewGameResponse(gameId = game.gameId))
            }

            post("addPlayer") {
                val req = call.receive<AddPlayerRequest>()
                val game = GameRepository.addPlayer(req.gameId, req.playerId, req.playerName)
                    ?: run {
                        call.respond(io.ktor.http.HttpStatusCode.NotFound, "Game not found")
                        return@post
                    }
                call.respond(AddPlayerResponse(gameId = game.gameId))
            }

            post("GetPlayers") {
                val req = call.receive<GetPlayersRequest>()
                val game = GameRepository.getGame(req.gameId)
                    ?: run {
                        call.respond(io.ktor.http.HttpStatusCode.NotFound, "Game not found")
                        return@post
                    }
                call.respond(GetPlayersResponse(gameId = game.gameId, players = game.players))
            }

            post("sendPole") {
                val req = call.receive<SendPoleRequest>()
                val ok = GameRepository.setPoleData(
                    req.gameId,
                    req.hod_player,
                    req.economic,
                    req.listPlayer,
                    req.pole
                )
                if (!ok) {
                    call.respond(io.ktor.http.HttpStatusCode.NotFound, "Game not found")
                    return@post
                }
                call.respond(io.ktor.http.HttpStatusCode.OK)
            }

            post("getPole") {
                val req = call.receive<GetPoleRequest>()
                val game = GameRepository.getGame(req.gameId)
                    ?: run {
                        call.respond(io.ktor.http.HttpStatusCode.NotFound, "Game not found")
                        return@post
                    }
                val poleData = game.poleData
                    ?: run {
                        call.respond(io.ktor.http.HttpStatusCode.NotFound, "Pole data not set")
                        return@post
                    }
                val hodPlayer = poleData["hod_player"]!!.jsonPrimitive.int
                val economic = poleData["economic"]!!.jsonObject
                val listPlayer = poleData["listPlayer"]!!.jsonArray.map { it.jsonObject }
                val pole = poleData["pole"]!!.jsonObject
                call.respond(
                    GetPoleResponse(
                        gameId = game.gameId,
                        hod_player = hodPlayer,
                        economic = economic,
                        listPlayer = listPlayer,
                        pole = pole
                    )
                )
            }

            post("gameover") {
                val req = call.receive<GameOverRequest>()
                val ok = GameRepository.setGameOver(req.gameId)
                if (!ok) {
                    call.respond(io.ktor.http.HttpStatusCode.NotFound, "Game not found")
                    return@post
                }
                call.respond(io.ktor.http.HttpStatusCode.OK)
            }
        }
    }
}
