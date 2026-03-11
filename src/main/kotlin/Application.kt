import com.lev.routes.gameRoutes
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.CORS
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.routing
import kotlinx.serialization.json.Json

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
            swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
        }
        gameRoutes()
    }.start(wait = true)
}
