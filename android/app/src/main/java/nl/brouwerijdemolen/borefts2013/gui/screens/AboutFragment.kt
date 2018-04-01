package nl.brouwerijdemolen.borefts2013.gui.screens

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import kotlinx.android.synthetic.main.dialog_about.licenses_text
import kotlinx.android.synthetic.main.dialog_about.logo_text
import kotlinx.android.synthetic.main.dialog_about.visit2312_button
import kotlinx.android.synthetic.main.dialog_about.visitdemolen_button
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.ext.asHtml
import nl.brouwerijdemolen.borefts2013.gui.components.getMolenString

class AboutFragment : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_about, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            window.requestFeature(Window.FEATURE_NO_TITLE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        logo_text.text = context?.getMolenString(R.string.app_name_short)
        licenses_text.text = getString(R.string.about_licenses).asHtml()
        licenses_text.movementMethod = LinkMovementMethod.getInstance()
        visitdemolen_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.brouwerijdemolen.nl"))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT))
        }
        visit2312_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://2312.nl"))
                    .setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT))
        }
    }

}
