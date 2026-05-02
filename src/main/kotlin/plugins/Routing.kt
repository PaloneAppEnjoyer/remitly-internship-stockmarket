package plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import routing.routes

fun Application.configureRouting() {
    routing {
        routes()
    }
}