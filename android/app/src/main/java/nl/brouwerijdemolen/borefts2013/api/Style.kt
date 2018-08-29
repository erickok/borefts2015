package nl.brouwerijdemolen.borefts2013.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Style(
        val id: Int,
        val name: String,
        val color: Int,
        val abv: Int,
        val body: Int,
        val bitterness: Int,
        val sweetness: Int,
        val acidity: Int) : Parcelable
