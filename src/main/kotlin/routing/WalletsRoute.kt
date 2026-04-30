package routing

import domain.model.TradeRequest
import domain.model.WalletResponse
import domain.model.StockEntry
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import service.StockWalletService

fun Route.walletsRoute(service: StockWalletService) {
    route("/wallets") {
        post("/{wallet_id}/stocks/{stock_name}")
        {
            val walletId =
                call.parameters["wallet_id"] ?: return@post call.respond(HttpStatusCode.BadRequest, "Missing wallet_id")
            val stockName = call.parameters["stock_name"] ?: return@post call.respond(
                HttpStatusCode.BadRequest,
                "Missing stock_name"
            )
            val request = try {
                call.receive<TradeRequest>()
            } catch (e: Exception){
                return@post call.respond(HttpStatusCode.BadRequest, "Invalid JSON format")
            }
            service.executeTrade(walletId, stockName, request.type)
            call.respond(HttpStatusCode.OK)
        }

        get("/{wallet_id}") {
            val walletId = call.parameters["wallet_id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing wallet_id")

            val walletState = service.getWallet(walletId)
            val stocks = walletState.map { StockEntry(it.key, it.value) }
            call.respond(HttpStatusCode.OK, WalletResponse(id = walletId, stocks = stocks))
        }

        get("/{wallet_id}/stocks/{stock_name}") {
            val walletId = call.parameters["wallet_id"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing wallet_id")
            val stockName = call.parameters["stock_name"] ?: return@get call.respond(HttpStatusCode.BadRequest, "Missing stock_name")

            val quantity = service.getWalletStockQuantity(walletId, stockName)
            // Respond with just the number per documentation
            call.respondText(quantity.toString())
        }
    }

}