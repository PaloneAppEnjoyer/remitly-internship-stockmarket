package routing

import io.ktor.server.routing.Route
import org.koin.ktor.ext.inject
import service.StockWalletService

fun Route.routes() {
    val stockWalletService by inject<StockWalletService>()
    walletsRoute(stockWalletService)
    stocksRoute(stockWalletService)
    logRoute(stockWalletService)
    chaosRoute()
}