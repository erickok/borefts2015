package nl.brouwerijdemolen.borefts2013.ext

import android.app.Activity
import android.os.Build
import android.os.Parcelable
import android.text.Html
import android.text.Spanned
import android.view.View

inline fun <reified T : Parcelable> Activity.arg(key: String): T = intent.getParcelableExtra(key)

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

