package com.spartacommodities.rest

import java.math.BigDecimal

data class CommodityVolumeResponse(val totalVolumeByCommodity: MutableMap<String, Int>)
data class CommodityPriceResponse(val averagePriceByCommodity: MutableMap<String, BigDecimal>)
data class TradersVolumeResponse(
    val traderId: String,
    val volume: Int
)

data class InsightsSummaryResponse(
    val totalVolumeByCommodity: CommodityVolumeResponse,
    val averagePriceByCommodity: CommodityPriceResponse,
    val topTradersByVolume: List<TradersVolumeResponse>,
)