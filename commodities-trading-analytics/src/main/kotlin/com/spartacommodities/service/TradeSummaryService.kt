package com.spartacommodities.service

import com.spartacommodities.domain.TradeSummary
import com.spartacommodities.domain.InsightsSummary
import com.spartacommodities.domain.CommodityVolume
import com.spartacommodities.domain.CommodityPrice
import org.slf4j.LoggerFactory

class TradeSummaryService {

    private val logger = LoggerFactory.getLogger("service")

    fun save(trades: List<TradeSummary>) {
    }

    fun get(): List<TradeSummary> = emptyList()

    fun calculateInsights(): InsightsSummary {

        return InsightsSummary(
            totalVolumeByCommodity = CommodityVolume(mutableMapOf()), averagePriceByCommodity = CommodityPrice(
                mutableMapOf()
            ), topTradersByVolume = emptyList()
        )
    }
}