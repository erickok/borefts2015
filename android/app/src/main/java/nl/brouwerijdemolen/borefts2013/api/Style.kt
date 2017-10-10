package nl.brouwerijdemolen.borefts2013.api

import android.content.res.Resources
import nl.brouwerijdemolen.borefts2013.R

data class Style(
        val id: Int,
        val name: String,
        val color: Int,
        val abv: Int,
        val body: Int,
        val bitterness: Int,
        val sweetness: Int,
        val acidity: Int) {

    fun colorResource(res: Resources): Int {
        return when (color) {
            1 -> res.getColor(R.color.style_1)
            2 -> res.getColor(R.color.style_2)
            3 -> res.getColor(R.color.style_3)
            4 -> res.getColor(R.color.style_4)
            5 -> res.getColor(R.color.style_5)
            else -> res.getColor(R.color.style_unknown)
        }
    }

}
