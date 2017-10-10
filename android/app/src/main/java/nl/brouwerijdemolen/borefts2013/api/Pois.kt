package nl.brouwerijdemolen.borefts2013.api

data class Pois(
        val areas: List<Area>,
        val pois: List<Poi>,
        val revision: Int = 0)
