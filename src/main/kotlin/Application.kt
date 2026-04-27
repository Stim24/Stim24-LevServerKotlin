package com.lev

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.http.*
import kotlinx.serialization.json.Json
import io.ktor.server.plugins.swagger.swaggerUI  // ✅ Только swaggerUI
import io.ktor.server.routing.routing
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
            // Статический Swagger - читает файл документации
            swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
        }

        gameRoutes()
    }.start(wait = true)
}