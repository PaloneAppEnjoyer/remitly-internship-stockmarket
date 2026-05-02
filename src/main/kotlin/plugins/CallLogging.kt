package plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.CallLogging

fun Application.configureCallLogging() {
    install(CallLogging)
}
