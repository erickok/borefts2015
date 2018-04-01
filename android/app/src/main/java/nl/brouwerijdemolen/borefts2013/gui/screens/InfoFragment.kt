package nl.brouwerijdemolen.borefts2013.gui.screens

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_info.findmill_button
import kotlinx.android.synthetic.main.fragment_info.getmore_button
import kotlinx.android.synthetic.main.fragment_info.nstimes_button
import kotlinx.android.synthetic.main.fragment_info.taxis_button
import kotlinx.android.synthetic.main.fragment_info.times_button
import nl.brouwerijdemolen.borefts2013.R
import java.util.GregorianCalendar


class InfoFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getmore_button.setOnClickListener {
            // TODO
        }
        findmill_button.setOnClickListener {
            // TODO
        }
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
            startActivity(
                    Intent(Intent.ACTION_VIEW, Uri.parse("http://www.ns.nl/actuele-vertrektijden/avt?station=bdg"))
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
        taxis_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("geo:52.084802,4.740689?z=14&q=taxi"))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }
}
