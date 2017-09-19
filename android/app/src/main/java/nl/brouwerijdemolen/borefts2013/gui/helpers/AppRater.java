package nl.brouwerijdemolen.borefts2013.gui.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class AppRater {

	private static final long TEN_MINUTES = 600_0;
	private static final int STARTS_TRIGGER = 3;

	private SharedPreferences prefs;

	AppRater(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	public void hit() {
		long now = System.currentTimeMillis();
		long lastHit = prefs.getLong("apprater_lasthit", 0);
		if (now - lastHit > TEN_MINUTES) {
			int starts = prefs.getInt("apprater_starts", 0);
			prefs.edit()
					.putLong("apprater_lasthit", now)
					.putInt("apprater_starts", starts + 1).apply();
		}
	}

	public void block() {
		prefs.edit().putBoolean("apprater_blocked", true).apply();
	}

	public boolean shouldShow() {
		boolean shouldShow = !prefs.getBoolean("apprater_blocked", false)
				&& prefs.getInt("apprater_starts", 0) >= STARTS_TRIGGER;
		if (shouldShow) {
			prefs.edit().putInt("apprater_starts", 0).apply();
		}
		return shouldShow;
	}

}
