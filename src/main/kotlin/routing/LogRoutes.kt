package routing

import domain.model.AuditLogResponse
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import service.StockWalletService

fun Route.logRoute(service: StockWalletService){
    get("/log") {
        val entries = service.getAuditLogs()
        call.respond(HttpStatusCode.OK, AuditLogResponse(log = entries))
    }
}