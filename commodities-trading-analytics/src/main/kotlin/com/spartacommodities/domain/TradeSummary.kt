package com.spartacommodities.domain

import java.math.BigDecimal
import java.time.LocalDateTime

enum class Commodity(val prettyName: String) {
    GOLD("Gold"), OIL("Oil"),
}

data class TradeSummary(
    val commodity: Commodity,
    val traderId: String,
    val price: BigDecimal,
    val quantity: Int,
    val timestamp: LocalDateTime
)
