package plugins

import domain.exception.BankOutOfStockException
import domain.exception.InvalidTradeTypeException
import domain.exception.StockNotFoundException
import domain.exception.WalletMissingStockException
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

fun Application.configureStatusPages() {
    install(StatusPages) {
        //404
        exception<StockNotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, mapOf("error" to cause.message))
        }

        //400
        exception<BankOutOfStockException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to cause.message))
        }

        exception<WalletMissingStockException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to cause.message))
        }

        exception<InvalidTradeTypeException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to cause.message))
        }
        //500
        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Unexpected error occurred", cause)
            call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Internal server error."))
        }
    }
}
