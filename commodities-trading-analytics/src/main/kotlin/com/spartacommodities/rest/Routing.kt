package com.spartacommodities.rest

import com.spartacommodities.domain.InsightsSummary
import com.spartacommodities.domain.TradeSummary
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.routing
import io.ktor.server.routing.post
import io.ktor.server.routing.get
import org.slf4j.LoggerFactory
import com.spartacommodities.service.TradeSummaryService
import java.math.BigDecimal

fun Application.configureRouting(tradeSummaryService: TradeSummaryService) {

    val logger = LoggerFactory.getLogger("routing")

    routing {
        post("/trades") {
            val trades = call.receive<List<TradeSummaryRequest>>()
            trades.validate()
            tradeSummaryService.save(trades.toTradeSummaryList())

            logger.debug("Saved $trades")

            call.respond(HttpStatusCode.Created, "Saved ${trades.size} trades")
        }

        get("/trades") {
            val trades = tradeSummaryService.get()

            call.respond(HttpStatusCode.OK, trades.toTradeSummaryResponseList())
        }

        get("/insights") {
            val insights = tradeSummaryService.calculateInsights()

            call.respond(HttpStatusCode.OK, insights.toInsightsSummaryResponse())
        }
    }
}

fun List<TradeSummaryRequest>.validate() = this.forEach { tsr ->
    run {
        require(tsr.traderId.isNotBlank()) { "A valid treaderId is required" }
        require(tsr.price > BigDecimal.ZERO) { "Price must be positive" }
        require(tsr.quantity > 0) { "Quantity must be positive" }
    }
}

fun List<TradeSummaryRequest>.toTradeSummaryList() = this.map {
    TradeSummary(
        commodity = it.commodity,
        traderId = it.traderId,
        price = it.price,
        quantity = it.quantity,
        timestamp = it.timestamp,
    )
}

fun List<TradeSummary>.toTradeSummaryResponseList() = this.map {
    TradeSummaryResponse(
        commodity = it.commodity,
        traderId = it.traderId,
        price = it.price,
        quantity = it.quantity,
        timestamp = it.timestamp,
    )
}

fun InsightsSummary.toInsightsSummaryResponse() = InsightsSummaryResponse(
    totalVolumeByCommodity = CommodityVolumeResponse(this.totalVolumeByCommodity.totalVolumeByCommodity),
    averagePriceByCommodity = CommodityPriceResponse(this.averagePriceByCommodity.averagePriceByCommodity),
    topTradersByVolume = this.topTradersByVolume.map {
        TradersVolumeResponse(
            traderId = it.traderId,
            volume = it.volume
        )
    }
)