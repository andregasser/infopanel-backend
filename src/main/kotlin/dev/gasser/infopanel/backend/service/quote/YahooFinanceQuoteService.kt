package dev.gasser.infopanel.backend.service.quote

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.math.BigDecimal
import org.slf4j.LoggerFactory

@Service
class YahooFinanceQuoteService : QuoteService {

    private val log = LoggerFactory.getLogger(YahooFinanceQuoteService::class.java)
    private val QUOTE_BASE_URL = "https://finance.yahoo.com/quote/"

    override fun getQuote(symbol: String): Quote {
        val doc: Document = Jsoup.connect(QUOTE_BASE_URL + symbol).get()
        val open = getOpen(doc)
        val bid = getBid(doc)
        val ask = getAsk(doc)
        val close = getClose(doc, symbol)
        val quote = Quote(symbol, ask, bid, open, close)
        log.info("{}", quote)
        return quote
    }

    private fun getOpen(doc: Document): BigDecimal? {
        val value: String? = doc.select("td[data-test=OPEN-value]").first()?.text()
        return if (!value.isNullOrEmpty()) {
            BigDecimal(value)
        } else {
            null
        }
    }

    private fun getClose(doc: Document, symbol: String): BigDecimal? {
        val element: Element? = doc.select("fin-streamer[data-field=regularMarketPrice][data-symbol=$symbol]").first()
        return if (element != null) {
            val value: String = element.attr("value")
            BigDecimal(value)
        } else {
            null
        }
    }

    private fun getAsk(doc: Document): BigDecimal? {
        val value: String? = doc.select("td[data-test=ASK-value]").first()?.text()?.dropLast(4)
        return if (!value.isNullOrEmpty()) {
            BigDecimal(value)
        } else {
            null
        }
    }

    private fun getBid(doc: Document): BigDecimal? {
        val value: String? = doc.select("td[data-test=BID-value]").first()?.text()?.dropLast(4)
        return if (!value.isNullOrEmpty()) {
            BigDecimal(value)
        } else {
            null
        }
    }

    @Scheduled(initialDelay = 2000, fixedDelay = 5000)
    fun schedule() {
        getQuote("NESN.SW")
    }

}
