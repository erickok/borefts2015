package nl.brouwerijdemolen.borefts2013.api

import com.google.android.gms.maps.model.LatLng

data class Area(
        val id: String,
        val name_nl: String,
        val name_en: String,
        val color: String,
        val points: List<Point>) {

    val pointLatLngs = points.map { LatLng(it.latitude.toDouble(), it.longitude.toDouble()) }

}
