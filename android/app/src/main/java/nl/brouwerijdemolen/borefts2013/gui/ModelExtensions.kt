package nl.brouwerijdemolen.borefts2013.gui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Beer
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.Style
import nl.brouwerijdemolen.borefts2013.ext.orZero
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

val Brewer.sortFilter: String
    get() = if (id == 32) "" else sortName

val Beer.fullName: CharSequence
    get() = if (festivalBeer) "$name (9+1)" else name

fun Beer.abvText(res: ResourceProvider): String? {
    return if (hasAbv) res.getString(R.string.info_abvperc, abv) else null
}

val Beer.hasAbv: Boolean
    get() = abv >= 0

val Beer.abvIndication: Int
    get() = if (hasAbv) {
        Math.min(Math.max(Math.round(abv / 2.3).toInt(), 1), 5)
    } else style?.abv.orZero()

val Beer.hasFlavourIndication: Boolean
    get() = bitterness > 0 && sweetness > 0 && acidity > 0

val Beer.bitternessIndication: Int
    get() = if (bitterness > 0) {
        bitterness
    } else style?.bitterness.orZero()

val Beer.sweetnessIndication: Int
    get() = if (sweetness > 0) {
        sweetness
    } else style?.sweetness.orZero()

val Beer.acidityIndication: Int
    get() = if (acidity > 0) {
        acidity
    } else style?.acidity.orZero()

fun Beer.colorIndicationResource(res: ResourceProvider): Int {
    val c = if (colour > 0) colour else style?.color.orZero()
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
        1 -> res.getString(R.string.info_serving_cask)
        2 -> res.getString(R.string.info_serving_bottle)
        else -> res.getString(R.string.info_serving_keg)
    }
}

