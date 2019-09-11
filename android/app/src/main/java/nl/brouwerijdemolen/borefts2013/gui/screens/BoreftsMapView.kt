package nl.brouwerijdemolen.borefts2013.gui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import androidx.annotation.ColorInt
import android.util.SparseArray
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolygonOptions
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.api.Area
import nl.brouwerijdemolen.borefts2013.api.Brewer
import nl.brouwerijdemolen.borefts2013.api.Poi
import nl.brouwerijdemolen.borefts2013.gui.components.ResourceProvider
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.io.IOException
import java.util.HashMap
import java.util.Locale

@SuppressLint("ViewConstructor") // Only to be created programmatically
class BoreftsMapView(
        context: Context,
        mapOptions: GoogleMapOptions) : MapView(context, mapOptions), KoinComponent {

    private val res: ResourceProvider by inject()

    private var poiMarkers: MutableMap<Marker, Poi> = mutableMapOf()
    private var poiIds: MutableMap<String, Marker> = mutableMapOf()
    private var brewerMarkers: MutableMap<Marker, Brewer> = mutableMapOf()
    private var brewerIds: SparseArray<Marker> = SparseArray()

    fun showBrewers(brewers: List<Brewer>, focusBrewerId: Int? = null) {
        getMapAsync { map ->
            brewerMarkers = mutableMapOf()
            brewerIds = SparseArray()
            for (brewer in brewers) {
                if (brewer.latitude != null && brewer.longitude != null && brewer.latitude.isNotEmpty() && brewer.longitude.isNotEmpty()) {
                    val brewerPin = drawBrewerMarker(brewer)
                    val bitmapToUse: BitmapDescriptor = if (brewerPin == null)
                        BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_mask)
                    else
                        BitmapDescriptorFactory.fromBitmap(brewerPin)
                    val marker = map.addMarker(
                            MarkerOptions()
                                    .position(LatLng(brewer.latitude.toDouble(), brewer.longitude.toDouble()))
                                    .title(brewer.shortName)
                                    .icon(bitmapToUse))
                    brewerMarkers[marker] = brewer
                    brewerIds.put(brewer.id, marker)
                }
            }

            // Set focus on a specific marker
            if (focusBrewerId != null) {
                brewerIds.get(focusBrewerId)?.showInfoWindow()
            }
        }
    }

    fun showAreas(areas: List<Area>) {
        getMapAsync { map ->
            for (area in areas) {
                map.addPolygon(
                        PolygonOptions()
                                .addAll(area.pointLatLngs)
                                .strokeColor(getColor(area.color))
                                .strokeWidth(5f)
                                .fillColor(getFillColor(area.color)))
            }
        }
    }

    fun showPois(pois: List<Poi>, focusPoiId: String? = null) {
        getMapAsync { map ->
            poiMarkers = HashMap()
            poiIds = HashMap()
            for (poi in pois) {
                val marker = map.addMarker(
                        MarkerOptions()
                                .position(poi.pointLatLng)
                                .title(getPoiName(poi))
                                .icon(BitmapDescriptorFactory.fromResource(getDrawable(poi.marker))))
                poiMarkers[marker] = poi
                poiIds[poi.id] = marker
            }

            // Set focus on a specific marker
            if (focusPoiId != null) {
                poiIds.get<String?, Marker>(focusPoiId)?.showInfoWindow()
            }
        }
    }

    fun handleInfoWindowClicks(onBrewerClicked: (Brewer) -> Unit) {
        getMapAsync { map ->
            map.setOnInfoWindowClickListener { marker ->
                brewerMarkers[marker]?.let(onBrewerClicked)
            }
        }
    }

    private fun drawBrewerMarker(brewer: Brewer): Bitmap? {
        try {
            val mask = BitmapFactory.decodeResource(resources, R.drawable.ic_marker_mask)
            val outline = BitmapFactory.decodeResource(resources, R.drawable.ic_marker_outline)
            val logo = BitmapFactory.decodeStream(resources.assets.open("images/" + brewer.logoUrl))
            val bmp = Bitmap.createBitmap(mask.width, mask.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bmp)
            val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG)
            canvas.drawBitmap(mask, 0f, 0f, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(logo, null, Rect(0, 0, mask.width, mask.height), paint)
            paint.xfermode = null
            canvas.drawBitmap(outline, 0f, 0f, paint)
            return bmp
        } catch (e: IOException) {
            return null // Should never happen, as the brewer logo always exists
        }

    }

    private fun getPoiName(poi: Poi): String {
        return if (Locale.getDefault().language == "nl") {
            poi.name_nl
        } else poi.name_en
    }

    private fun getDrawable(resName: String): Int {
        return resources.getIdentifier(resName, "drawable", context.packageName)
    }


    @ColorInt
    private fun getColor(resName: String): Int {
        return res.getColor(resources.getIdentifier(resName, "color", context.packageName))
    }

    @ColorInt
    private fun getFillColor(resName: String): Int {
        return getColor(resName + "_half")
    }

}
