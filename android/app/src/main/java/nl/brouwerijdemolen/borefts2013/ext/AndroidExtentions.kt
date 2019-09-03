package nl.brouwerijdemolen.borefts2013.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Parcelable
import android.text.Html
import android.text.Spanned
import android.view.View

const val KEY_ARGS = "args"
inline fun <reified T : Parcelable> Activity.arg(key: String): T =
        requireNotNull(intent.getParcelableExtra(key)) { "No args in activity intent bundle" }

var View.isVisible
    get() = this.visibility == View.VISIBLE
    set(value) {
        this.visibility = if (value) View.VISIBLE else View.GONE
    }

fun String.asHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        @Suppress("DEPRECATION")
        Html.fromHtml(this)
    }
}

fun Context.startLink(primary: Uri, alternative: Uri? = null) {
    try {
        startActivity(Intent(Intent.ACTION_VIEW, primary).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    } catch (e: Exception) {
        if (alternative != null) {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, alternative).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
            } catch (e2: Exception) {
                // No browser installed; ignore this hypothetical case
            }

        }
    }

}
