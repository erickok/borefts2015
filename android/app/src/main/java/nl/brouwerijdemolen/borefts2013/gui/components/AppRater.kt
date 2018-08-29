package nl.brouwerijdemolen.borefts2013.gui.components

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class AppRater(context: Context) {

    private val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun hit() {
        val now = System.currentTimeMillis()
        val lastHit = prefs.getLong("apprater_lasthit", 0)
        if (now - lastHit > TEN_MINUTES) {
            val starts = prefs.getInt("apprater_starts", 0)
            prefs.edit()
                    .putLong("apprater_lasthit", now)
                    .putInt("apprater_starts", starts + 1).apply()
        }
    }

    fun block() {
        prefs.edit().putBoolean("apprater_blocked", true).apply()
    }

    fun shouldShow(): Boolean {
        val shouldShow = !prefs.getBoolean("apprater_blocked", false) && prefs.getInt("apprater_starts", 0) >= STARTS_TRIGGER
        if (shouldShow) {
            prefs.edit().putInt("apprater_starts", 0).apply()
        }
        return shouldShow
    }

    companion object {

        private const val TEN_MINUTES = 6000L
        private const val STARTS_TRIGGER = 3

    }

}
