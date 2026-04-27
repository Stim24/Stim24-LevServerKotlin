package com.lev

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import io.ktor.openapi.OpenApiInfo
import io.ktor.server.routing.openapi.OpenApiDocSource
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.*
import com.lev.routes.gameRoutes

fun main() {
    embeddedServer(Netty, port = 8080) {
        install(CORS) {
            anyHost()
            allowHeader(HttpHeaders.ContentType)
        }
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                encodeDefaults = true
                isLenient = true
            })
        }

        routing {
            swaggerUI(path = "swagger") {
                info = OpenApiInfo(
                    title = "Lev Game API",
                    version = "1.0.0",
                    description = "REST API для управления играми"
                )
                source = OpenApiDocSource.Routing(ContentType.Application.Json) {
                    routingRoot.descendants()
                }
            }
        }

        gameRoutes()
    }.start(wait = true)
}