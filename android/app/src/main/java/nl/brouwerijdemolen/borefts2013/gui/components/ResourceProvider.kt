package nl.brouwerijdemolen.borefts2013.gui.components

import android.content.Context
import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.util.Log
import java.io.InputStream

class ResourceProvider(private val context: Context) {

    fun getColor(@ColorRes resId: Int): Int {
        return ContextCompat.getColor(context, resId)
    }

    fun getString(@StringRes resId: Int, vararg formatArgs: Any): String {
        return context.getString(resId, *formatArgs)
    }

    fun openAsset(assetName: String): InputStream? {
        return try {
            context.resources.assets.open(assetName)
        } catch (e: Exception) {
            // Should never happen, as the brewer logo always exists locally
            Log.e("ResourceProvider", "Missing asset file", e)
            null
        }
    }

}
