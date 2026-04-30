package routing

import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.chaosRoute(){
    post("/chaos"){
        kotlin.system.exitProcess(1)
    }
}