package routing

import domain.model.StockListRequest
import domain.model.StockListResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import service.StockWalletService

fun Route.stocksRoute(service: StockWalletService) {
    route("/stocks") {
        get {
            val bankState = service.getBankStocks()
            call.respond(HttpStatusCode.OK, StockListResponse(bankState))
        }
        post {
            val request = try {
                call.receive<StockListRequest>()
            } catch (e: Exception) {
                e.printStackTrace()
                return@post call.respond(HttpStatusCode.BadRequest, "Invalid JSON format")
            }
            service.setBankStocks(request.stocks)
            call.respond(HttpStatusCode.OK, StockListResponse(request.stocks))
        }
    }
}