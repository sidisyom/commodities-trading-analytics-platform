package com.spartacommodities.configuration

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import org.slf4j.LoggerFactory

fun Application.configurePlugins() {

    val logger = LoggerFactory.getLogger("application")

    install(ContentNegotiation) {
        jackson {
            registerModule(JavaTimeModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            enable(SerializationFeature.INDENT_OUTPUT)
            println("Boom!")
        }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            logger.error(cause.localizedMessage)
            call.respond(HttpStatusCode.InternalServerError, "Internal server error: ${cause.localizedMessage}")
        }
        exception<IllegalArgumentException> { call, cause ->
            logger.info(cause.localizedMessage)
            call.respond(HttpStatusCode.BadRequest, cause.localizedMessage)
        }
    }
}