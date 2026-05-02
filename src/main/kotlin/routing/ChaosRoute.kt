package routing

import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.chaosRoute(){
    post("/chaos"){
        Thread{
            kotlin.system.exitProcess(1)
        }.start()
    }
}