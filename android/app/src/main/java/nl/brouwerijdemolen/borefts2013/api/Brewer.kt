package nl.brouwerijdemolen.borefts2013.api

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Brewer(
        val id: Int,
        val logoUrl: String,
        val name: String,
        val shortName: String,
        val sortName: String,
        val city: String,
        val country: String,
        val description: String? = null,
        val twitter: String? = null,
        val facebook: String? = null,
        val website: String? = null,
        val latitude: Double? = null,
        val longitude: Double? = null) : Parcelable
