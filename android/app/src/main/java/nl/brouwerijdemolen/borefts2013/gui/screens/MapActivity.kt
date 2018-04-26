package nl.brouwerijdemolen.borefts2013.gui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_map.*
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.ext.KEY_ARGS
import nl.brouwerijdemolen.borefts2013.ext.arg
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import nl.brouwerijdemolen.borefts2013.gui.components.ResourceProvider
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.inject

class MapActivity : AppCompatActivity() {

    private val mapViewModel: MapViewModel by viewModel(parameters = { mapOf(KEY_ARGS to arg(KEY_ARGS)) })
    private val res: ResourceProvider by inject()

    private lateinit var mapView: BoreftsMapView

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        title_toolbar.setNavigationIcon(R.drawable.ic_back)
        title_toolbar.setNavigationOnClickListener { finish() }

        // If we have no location access, ask for the runtime permission (unless marked as never ask again)
        if (!hasLocationPermission()) {
            val snackbar = Snackbar.make(map_holder, R.string.general_location_permission, Snackbar.LENGTH_INDEFINITE)
            snackbar.setActionTextColor(res.getColor(R.color.yellow))
            snackbar.setAction(R.string.general_location_ok) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION)
            }
            snackbar.show()
        }

        mapView = BoreftsMapView(this, GoogleMapOptions()).apply {
            onActivityCreated(savedInstanceState)
            getMapAsync {
                // Pad map controls below action bar
                val actionBarValue = TypedValue()
                theme.resolveAttribute(R.attr.actionBarSize, actionBarValue, true)
                it.setPadding(0, TypedValue.complexToDimensionPixelSize(actionBarValue.data, resources.displayMetrics), 0, 0)

                // Start camera on festival terrain
                it.moveCamera(CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder()
                                .target(LatLng(52.085114, 4.740697))
                                .zoom(17.1f)
                                .bearing(6f).build()))

                // Show location permission request if we do not have it
                if (hasLocationPermission()) {
                    it.isMyLocationEnabled = true
                }

                // Navigation
                it.uiSettings.isCompassEnabled = true
                handleInfoWindowClicks { startActivity(BrewerActivity(context, it)) }

                // Schedule zooming to festival terrain
                Handler().postDelayed({
                    it.animateCamera(CameraUpdateFactory.newCameraPosition(
                            CameraPosition.Builder().target(LatLng(52.084515, 4.739977)).zoom(18f).bearing(3.3f).build()))
                }, 1500)
            }
        }
        map_holder.addView(mapView)
        mapViewModel.state.observeNonNull(this) {
            if (it is MapUiModel.Success) {
                mapView.showBrewers(it.brewers, it.focusBrewerId)
                mapView.showAreas(it.areas)
                mapView.showPois(it.pois, it.focusPoiId)
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_PERMISSION = 0

        operator fun invoke(context: Context, focusBrewerId: Int? = null, focusPoiId: String? = null): Intent =
                Intent(context, MapActivity::class.java).putExtra(KEY_ARGS, Args(focusBrewerId, focusPoiId))
    }

}
