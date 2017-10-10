package nl.brouwerijdemolen.borefts2013.api

data class Beers(
        val beers: List<Beer>,
        val revision: Int = 0)
