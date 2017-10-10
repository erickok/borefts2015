package nl.brouwerijdemolen.borefts2013.api

import android.content.res.Resources
import nl.brouwerijdemolen.borefts2013.R

data class Beer(
        val id: Int,
        val name: String,
        val brewerId: Int,
        val styleId: Int,
        val abv: Float = -1f,
        val isOakAged: Boolean = false,
        val isFestivalBeer: Boolean = false,
        val tags: String? = null,
        val untappdId: Int = -1,
        val serving: Int = 0,
        val colour: Int = 0,
        val body: Int = 0,
        val bitterness: Int = 0,
        val sweetness: Int = 0,
        val acidity: Int = 0) {

    // These are not provided by the server, but loaded manually and attached with their setters
    lateinit var brewer: Brewer
    lateinit var style: Style

    val abvIndication: Int
        get() = if (abv >= 0) {
            Math.min(Math.max(Math.round(abv / 2.3).toInt(), 1), 5)
        } else style.abv

    val bitternessIndication: Int
        get() = if (bitterness > 0) {
            bitterness
        } else style.bitterness

    val sweetnessIndication: Int
        get() = if (sweetness > 0) {
            sweetness
        } else style.sweetness

    val acidityIndication: Int
        get() = if (acidity > 0) {
            acidity
        } else style.acidity

    fun colorIndicationResource(res: Resources): Int {
        val c = if (colour > 0) colour else style.color
        return when (c) {
            1 -> res.getColor(R.color.style_1)
            2 -> res.getColor(R.color.style_2)
            3 -> res.getColor(R.color.style_3)
            4 -> res.getColor(R.color.style_4)
            5 -> res.getColor(R.color.style_5)
            else -> res.getColor(R.color.style_unknown)
        }
    }

    fun servingResource(res: Resources): String {
        return when (serving) {
            1 -> res.getString(R.string.info_serving_cask)
            2 -> res.getString(R.string.info_serving_bottle)
            else -> res.getString(R.string.info_serving_keg)
        }
    }

}
