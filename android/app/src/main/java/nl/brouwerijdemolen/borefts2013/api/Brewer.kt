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
        // TODO Change back to Float when data is refreshed
        val latitude: String? = null,
        val longitude: String? = null) : Parcelable
