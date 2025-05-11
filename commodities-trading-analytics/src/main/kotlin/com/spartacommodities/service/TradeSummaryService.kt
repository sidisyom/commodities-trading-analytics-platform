package com.spartacommodities.service

import com.spartacommodities.domain.TradeSummary
import com.spartacommodities.domain.InsightsSummary
import com.spartacommodities.domain.CommodityVolume
import com.spartacommodities.domain.CommodityPrice
import com.spartacommodities.domain.TradersVolume
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.concurrent.CopyOnWriteArrayList

class TradeSummaryService {

    private val logger = LoggerFactory.getLogger("service")

    private val scale = 1
    private val roundingMode = RoundingMode.HALF_EVEN

    private val db = CopyOnWriteArrayList<TradeSummary>()

    fun save(trades: List<TradeSummary>) {
        db.addAll(trades)

        logger.debug("Saved: ${trades}")
        logger.info("Saved ${trades.size} trade(s)")
    }

    fun get(): List<TradeSummary> = db.toList()

    fun calculateInsights(): InsightsSummary {
        data class CommodityAccumulator(
            val totalVolume: Int = 0,
            val totalPrice: BigDecimal = BigDecimal.ZERO,
            val totalItems: Int = 0,
        )

        val groupedByCommodity = db.groupBy { it.commodity }.mapValues {
            it.value.fold(CommodityAccumulator()) { acc, next ->
                CommodityAccumulator(
                    totalVolume = acc.totalVolume + next.quantity,
                    totalPrice = acc.totalPrice + next.price,
                    totalItems = acc.totalItems + 1,
                )
            }
        }

        val commodityVolumes = CommodityVolume(mutableMapOf())
        val commodityPrices = CommodityPrice(mutableMapOf())
        groupedByCommodity.forEach {
            run {
                commodityVolumes.totalVolumeByCommodity[it.key.prettyName] = it.value.totalVolume
                commodityPrices.averagePriceByCommodity[it.key.prettyName] =
                    it.value.totalPrice.divide(it.value.totalItems.toBigDecimal()).setScale(scale, roundingMode)
            }
        }

        val tradersVolume = db.groupBy { it.traderId }.mapValues {
            it.value.fold(0) { acc, next ->
                acc + next.quantity
            }
        }.map { TradersVolume(traderId = it.key, volume = it.value) }

        return InsightsSummary(commodityVolumes, commodityPrices, tradersVolume).also { logger.debug("$it") }
    }
}