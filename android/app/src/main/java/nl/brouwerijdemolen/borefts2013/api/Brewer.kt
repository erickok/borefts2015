package nl.brouwerijdemolen.borefts2013.api

data class Brewer(
        val id: Int,
        val logoUrl: String,
        val name: String,
        val shortName: String,
        val sortName: String,
        val city: String,
        val country: String,
        val description: String? = null,
        val website: String? = null,
        val latitude: Float,
        val longitude: Float)
