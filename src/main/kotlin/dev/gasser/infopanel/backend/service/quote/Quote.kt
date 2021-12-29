package dev.gasser.infopanel.backend.service.quote

import java.math.BigDecimal

data class Quote(val symbol: String, val ask: BigDecimal?, val bid: BigDecimal?, val open: BigDecimal?, val close: BigDecimal?)
