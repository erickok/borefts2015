package nl.brouwerijdemolen.borefts2013.gui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.gui.components.ResourceProvider

fun Brewer.location(res: ResourceProvider): String? {
    return res.getString(R.string.info_origin, city, country)
}

fun Brewer.logoBitmap(res: ResourceProvider): Bitmap? {
    return BitmapFactory.decodeStream(res.openAsset("images/$logoUrl"))
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

fun Beer.abvText(res: ResourceProvider): String? {
    return if (hasAbv) null else res.getString(R.string.info_abv, abv)
}

val Beer.hasAbv: Boolean
    get() = abv >= 0

val Beer.abvIndication: Int
    get() = if (hasAbv) {
        Math.min(Math.max(Math.round(abv / 2.3).toInt(), 1), 5)
    } else style.abv

val Beer.bitternessIndication: Int
    get() = if (bitterness > 0) {
        bitterness
    } else style.bitterness

val Beer.sweetnessIndication: Int
    get() = if (sweetness > 0) {
        sweetness
    } else style.sweetness

val Beer.acidityIndication: Int
    get() = if (acidity > 0) {
        acidity
    } else style.acidity

fun Beer.colorIndicationResource(res: ResourceProvider): Int {
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

fun Beer.servingResource(res: ResourceProvider): String {
    return when (serving) {
        1 -> res.getString(R.string.info_serving_cask)
        2 -> res.getString(R.string.info_serving_bottle)
        else -> res.getString(R.string.info_serving_keg)
    }
}

