package nl.brouwerijdemolen.borefts2013.gui

import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.ext.orZero
import nl.brouwerijdemolen.borefts2013.gui.components.ResourceProvider
import kotlin.math.roundToInt

fun Brewer.location(res: ResourceProvider): String? {
    return res.getString(R.string.info_origin, city, country)
}

fun Style.getColorResource(res: ResourceProvider): Int {
    return when (color) {
        1 -> res.getColor(R.color.style_1)
        2 -> res.getColor(R.color.style_2)
        3 -> res.getColor(R.color.style_3)
        4 -> res.getColor(R.color.style_4)
        5 -> res.getColor(R.color.style_5)
        else -> res.getColor(R.color.style_unknown)
    }
}

val Brewer.sortFilter: String
    get() = if (id == 32) "" else sortName

val Beer.fullName: CharSequence
    get() = if (festivalBeer) "$name (Borefts Special)" else name

fun Beer.abvText(res: ResourceProvider): String? {
    return if (hasAbv) res.getString(R.string.info_abvperc, abv) else null
}

val Beer.hasAbv: Boolean
    get() = abv >= 0

val Beer.abvIndication: Int
    get() = if (hasAbv) {
        (abv / 2.3).roundToInt().coerceIn(1, 5)
    } else style?.abv.orZero()

val Beer.hasFlavourIndication: Boolean
    get() = bitter > 0 && sweet > 0 && sour > 0

val Beer.bitternessIndication: Int
    get() = if (bitter > 0) {
        bitter
    } else style?.bitterness.orZero()

val Beer.sweetnessIndication: Int
    get() = if (sweet > 0) {
        sweet
    } else style?.sweetness.orZero()

val Beer.acidityIndication: Int
    get() = if (sour > 0) {
        sour
    } else style?.acidity.orZero()

val Beer.colorInt: Int
    get() = try {
        color?.toInt().orZero()
    } catch (e: NumberFormatException) {
        0
    }

fun Beer.colorIndicationResource(res: ResourceProvider): Int {
    val bc = colorInt
    val c = if (bc > 0) bc else style?.color.orZero()
    return when (c) {
        1 -> res.getColor(R.color.style_1)
        2 -> res.getColor(R.color.style_2)
        3 -> res.getColor(R.color.style_3)
        4 -> res.getColor(R.color.style_4)
        5 -> res.getColor(R.color.style_5)
        else -> res.getColor(R.color.style_unknown)
    }
}

fun Beer.servingText(res: ResourceProvider): String {
    return when (serving) {
        "CASK" -> res.getString(R.string.info_serving_cask)
        "BOTTLE" -> res.getString(R.string.info_serving_bottle)
        else -> res.getString(R.string.info_serving_keg)
    }
}

