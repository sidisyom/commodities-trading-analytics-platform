package com.spartacommodities

import com.spartacommodities.configuration.configurePlugins
import com.spartacommodities.rest.configureRouting
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import com.spartacommodities.service.TradeSummaryService

fun main() {
    embeddedServer(Netty, port = 8080) {
        configurePlugins()
        configureRouting(TradeSummaryService())
    }.start(wait = true)
}