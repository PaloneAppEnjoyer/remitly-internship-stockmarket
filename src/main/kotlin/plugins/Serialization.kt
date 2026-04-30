package plugins

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.serialization.jackson.jackson
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)

            enable(SerializationFeature.INDENT_OUTPUT)
            disable(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        }
    }
}