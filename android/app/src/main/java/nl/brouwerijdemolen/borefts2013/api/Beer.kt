package nl.brouwerijdemolen.borefts2013.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Beer(
        val id: Int,
        val name: String,
        val brewerId: Int,
        val styleId: Int,
        val abv: Float = -1f,
        val oakAged: Boolean = false,
        val festivalBeer: Boolean = false,
        val tags: String? = null,
        val untappdId: String? = null,
        val serving: String,
        val colour: Int = 0,
        val body: Int = 0,
        val bitterness: Int = 0,
        val sweetness: Int = 0,
        val acidity: Int = 0,
        var brewer: Brewer? = null,
        var style: Style? = null) : Parcelable
