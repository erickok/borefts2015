package nl.brouwerijdemolen.borefts2013.gui.components

import android.util.TypedValue
import android.view.View
import nl.brouwerijdemolen.borefts2013.R

fun View.actionBarSize(): Int {
    val tv = TypedValue()
    if (context.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
        return TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
    }
    return 0
}
