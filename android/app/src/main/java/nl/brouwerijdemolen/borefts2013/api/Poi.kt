package nl.brouwerijdemolen.borefts2013.api

import com.google.android.gms.maps.model.LatLng

class Poi(
        val id: String,
        val name_nl: String,
        val name_en: String,
        val marker: String,
        val point: Point) {

    val pointLatLng = LatLng(point.latitude.toDouble(), point.longitude.toDouble())

}
