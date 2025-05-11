package com.spartacommodities.service

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.spartacommodities.domain.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.math.BigDecimal
import java.time.LocalDateTime

class TradeSummaryServiceTest {

    private val tradeSummaryService = TradeSummaryService()

    @Test
    fun `saves and gets trade summaries`() {
        // Arrange
        val trades = listOf(
            TradeSummary(
                commodity = Commodity.GOLD,
                traderId = "T123",
                price = BigDecimal("2023.5"),
                quantity = 10,
                timestamp = LocalDateTime.now(),
            ),
            TradeSummary(
                commodity = Commodity.OIL,
                traderId = "T456",
                price = BigDecimal("85.2"),
                quantity = 10,
                timestamp = LocalDateTime.now().minusSeconds(2),
            ),
        )

        // Act
        tradeSummaryService.save(trades)
        val actual = tradeSummaryService.get()

        // Assert
        assertThat(actual).isEqualTo(trades)
    }

    @ParameterizedTest
    @MethodSource("insightsTestData")
    fun `Calculates insights`(trades: List<TradeSummary>, expected: InsightsSummary) {
        // Arrange
        tradeSummaryService.save(trades)

        // Act
        val actual = tradeSummaryService.calculateInsights()

        // Assert
        assertThat(actual).isEqualTo(expected)
    }

    companion object {
        @JvmStatic
        fun insightsTestData(): List<Arguments> {

            val trades = listOf(
                TradeSummary(
                    commodity = Commodity.GOLD,
                    traderId = "T123",
                    price = BigDecimal("2023.5"),
                    quantity = 10,
                    timestamp = LocalDateTime.now(),
                ),
                TradeSummary(
                    commodity = Commodity.OIL,
                    traderId = "T123",
                    price = BigDecimal("100.302"),
                    quantity = 10,
                    timestamp = LocalDateTime.now(),
                ),
                TradeSummary(
                    commodity = Commodity.OIL,
                    traderId = "T456",
                    price = BigDecimal("85.2"),
                    quantity = 5,
                    timestamp = LocalDateTime.now().minusSeconds(2),
                ),
                TradeSummary(
                    commodity = Commodity.GOLD,
                    traderId = "T456",
                    price = BigDecimal("1998.121"),
                    quantity = 8,
                    timestamp = LocalDateTime.now().minusSeconds(2),
                ),
            )

            val expected = InsightsSummary(
                totalVolumeByCommodity = CommodityVolume(mutableMapOf("Gold" to 18, "Oil" to 15)),
                averagePriceByCommodity = CommodityPrice(
                    mutableMapOf(
                        "Gold" to BigDecimal("2010.8"),
                        "Oil" to BigDecimal("92.8")
                    )
                ),
                topTradersByVolume = listOf(
                    TradersVolume(traderId = "T123", volume = 20),
                    TradersVolume(traderId = "T456", volume = 13)
                ),
            )

            return listOf(
                Arguments.of(
                    trades, expected
                ),
            )
        }
    }
}