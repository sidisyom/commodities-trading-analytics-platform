package com.spartacommodities.rest

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.spartacommodities.domain.Commodity
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import com.spartacommodities.configuration.configurePlugins
import com.spartacommodities.service.TradeSummaryService
import java.math.BigDecimal
import java.time.LocalDateTime

class TradesApiTest {

    private val objectMapper = jacksonObjectMapper().apply {
        registerModules(JavaTimeModule())
    }

    @Test
    fun `saves well-formed trades`() = testApplication {
        // Arrange
        application {
            configurePlugins()
            configureRouting(TradeSummaryService())
        }
        // Does not work for some reason hence some scenarios can't be tested
//        val client = createClient {
//            install(ContentNegotiation) {
//                jackson {
//                    enable(SerializationFeature.INDENT_OUTPUT)
//                }
//            }
//        }

        // Act
        val response = client.post("/trades") {
            val trades = listOf(
                TradeSummaryRequest(
                    commodity = Commodity.GOLD,
                    traderId = "T123",
                    price = BigDecimal("2023.5"),
                    quantity = 10,
                    timestamp = LocalDateTime.now(),
                ),
                TradeSummaryResponse(
                    commodity = Commodity.OIL,
                    traderId = "T456",
                    price = BigDecimal("85.2"),
                    quantity = 10,
                    timestamp = LocalDateTime.now().minusSeconds(2),
                ),
            )
            setBody(objectMapper.writeValueAsString(trades))
            header("Content-Type", "application/json")
        }

        // Assert
        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
        assertThat(response.bodyAsText()).isEqualTo("Saved 2 trades")
    }

    @Test
    fun `rejects malformed trades`() = testApplication {
        // Arrange
        application {
            configurePlugins()
            configureRouting(TradeSummaryService())
        }

        // Act
        val response = client.post("/trades") {
            val trades = listOf(
                TradeSummaryRequest(
                    commodity = Commodity.OIL,
                    traderId = "",
                    price = BigDecimal("2023.5"),
                    quantity = 10,
                    timestamp = LocalDateTime.now(),
                ),
                TradeSummaryResponse(
                    commodity = Commodity.GOLD,
                    traderId = "T456",
                    price = BigDecimal.ZERO,
                    quantity = 10,
                    timestamp = LocalDateTime.now().minusSeconds(2),
                ),
            )
            setBody(objectMapper.writeValueAsString(trades))
            header("Content-Type", "application/json")
        }

        // Assert
        assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
    }
}