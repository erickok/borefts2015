package nl.brouwerijdemolen.borefts2013.gui.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AlertDialog
import android.text.Spannable
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.fragment_info.findmill_button
import kotlinx.android.synthetic.main.fragment_info.getmore_button
import kotlinx.android.synthetic.main.fragment_info.minimap_holder
import kotlinx.android.synthetic.main.fragment_info.nstimes_button
import kotlinx.android.synthetic.main.fragment_info.taxis_button
import kotlinx.android.synthetic.main.fragment_info.times_button
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.ext.observeNonNull
import nl.brouwerijdemolen.borefts2013.ext.startLink
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.GregorianCalendar

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
                it.setOnMapClickListener { _ -> startActivity(MapActivity(requireContext())) }
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
                putExtra("beginTime", GregorianCalendar(2019, 8, 27, 12, 0).timeInMillis)
                putExtra("endTime", GregorianCalendar(2019, 8, 28, 21, 45).timeInMillis)
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
        taxis_button.setOnClickListener { _ ->
            showTaxiSuggestionsDialog()
        }

        viewModel.state.observeNonNull(this) {
            if (it is InfoUiModel.Success) {
                mapView.showBrewers(it.brewers)
                mapView.showAreas(it.areas)
                mapView.showPois(it.pois)
            }
        }
    }

    private fun showTaxiSuggestionsDialog() {
        val dialogBuilder = AlertDialog.Builder(requireContext())
        val numbers = SpannableString.valueOf(getString(R.string.info_taxis_recommended))

        // Make phone numbers clickable
        "(\\+[\\d ()-]+)".toRegex().findAll(numbers).forEach { result ->
            numbers.setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:${result.value.replace("(0) ", "")}")))
                }
            }, result.range.start, result.range.endInclusive + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }

        // Dialog with a textview that is set up to have clickable links
        val numbersText = TextView(dialogBuilder.context).also {
            it.movementMethod = LinkMovementMethod.getInstance()
            it.text = numbers
            resources.getDimension(R.dimen.margin_default).toInt().let { padding ->
                it.setPadding(padding, padding, padding, padding)
            }
        }
        dialogBuilder
                .setView(numbersText)
                .setPositiveButton(android.R.string.ok, null)
                .show()
    }

}
