package nl.brouwerijdemolen.borefts2013.gui.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_info.*
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import nl.brouwerijdemolen.borefts2013.ext.startLink
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class InfoFragment : Fragment() {

    private val viewModel: InfoViewModel by viewModel()
    private lateinit var mapView: BoreftsMapView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView = BoreftsMapView(requireActivity(), GoogleMapOptions().liteMode(true)).apply {
            onCreate(savedInstanceState)
            getMapAsync {
                it.moveCamera(CameraUpdateFactory.newCameraPosition(
                        CameraPosition.Builder()
                                .target(LatLng(52.084622, 4.740003))
                                .zoom(17f).build()))
                it.uiSettings.isMapToolbarEnabled = false
                it.setOnMarkerClickListener { _ -> viewModel.openMap().let { _ -> true } }
                it.setOnMapClickListener {_ -> startActivity(MapActivity(requireContext())) }
            }
        }
        minimap_holder.addView(mapView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
        getmore_button.setOnClickListener { viewModel.openMap("tokensale") }
        findmill_button.setOnClickListener { viewModel.openMap("mill") }
        times_button.setOnClickListener {
            // Try to start the calendar application
            val intent = Intent(Intent.ACTION_EDIT).apply {
                type = "vnd.android.cursor.item/event"
                putExtra("title", getString(R.string.app_name))
                putExtra("eventLocation", "Doortocht 4, Bodegraven, The Netherlands")
                putExtra("beginTime", GregorianCalendar(2018, 8, 21, 12, 0).timeInMillis)
                putExtra("endTime", GregorianCalendar(2018, 8, 22, 21, 45).timeInMillis)
                putExtra("rrule", "FREQ=DAILY;COUNT=2")
            }
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(activity, R.string.error_nocalendar, Toast.LENGTH_LONG).show()
            }
        }
        nstimes_button.setOnClickListener {
            requireContext().startLink(Uri.parse("http://www.ns.nl/actuele-vertrektijden/avt?station=bdg"))
        }
        taxis_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:52.084802,4.740689?z=14&q=taxi"))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }

        viewModel.state.observeNonNull(this) {
            if (it is InfoUiModel.Success) {
                mapView.showBrewers(it.brewers)
                mapView.showAreas(it.areas)
                mapView.showPois(it.pois)
            }
        }
    }

}
