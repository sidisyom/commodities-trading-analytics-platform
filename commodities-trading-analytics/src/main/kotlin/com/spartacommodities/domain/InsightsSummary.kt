package com.spartacommodities.domain

import java.math.BigDecimal

data class CommodityVolume(val totalVolumeByCommodity: MutableMap<String, Int>)
data class CommodityPrice(val averagePriceByCommodity: MutableMap<String, BigDecimal>)
data class TradersVolume(
    val traderId: String,
    val volume: Int
)

data class InsightsSummary(
    val totalVolumeByCommodity: CommodityVolume,
    val averagePriceByCommodity: CommodityPrice,
    val topTradersByVolume: List<TradersVolume>,
)