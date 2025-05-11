package com.spartacommodities.rest

import com.spartacommodities.domain.Commodity
import java.math.BigDecimal
import java.time.LocalDateTime

data class TradeSummaryResponse(
    val commodity: Commodity,
    val traderId: String,
    val price: BigDecimal,
    val quantity: Int,
    val timestamp: LocalDateTime,
)
