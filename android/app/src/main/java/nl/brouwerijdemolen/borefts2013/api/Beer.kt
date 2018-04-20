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
        val isOakAged: Boolean = false,
        val isFestivalBeer: Boolean = false,
        val tags: String? = null,
        val untappdId: Int = -1,
        val serving: Int = 0,
        val colour: Int = 0,
        val body: Int = 0,
        val bitterness: Int = 0,
        val sweetness: Int = 0,
        val acidity: Int = 0,
        var brewer: Brewer? = null,
        var style: Style? = null) : Parcelable
