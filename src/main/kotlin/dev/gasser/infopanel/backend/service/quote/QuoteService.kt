package dev.gasser.infopanel.backend.service.quote

interface QuoteService {

    fun getQuote(symbol: String) : Quote

}
