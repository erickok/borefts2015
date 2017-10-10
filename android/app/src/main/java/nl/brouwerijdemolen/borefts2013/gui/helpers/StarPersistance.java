package nl.brouwerijdemolen.borefts2013.gui.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.HashSet;

public class StarPersistance {

	private SharedPreferences prefs;
	private HashSet<Integer> starCache = null;

	protected StarPersistance(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
	}

	/**
	 * Returns a set with beer ids for all the beers that the user starred.
	 * @return
	 */
	public HashSet<Integer> getAllStars() {
		ensureStarCache();
		return starCache;
	}

	/**
	 * Return whether the user has starred any beer so far.
	 * @return True if no beers are starred yet, false otherwise
	 */
	public boolean isEmpty() {
		ensureStarCache();
		return starCache.isEmpty();
	}

	/**
	 * Returns true iff the beer is currently starred by the user.
	 * @param beerId The id for the beer to look up
	 * @return True of the user starred the beer, false otherwise
	 */
	public boolean isStarred(int beerId) {
		return getAllStars().contains(beerId);
	}

	/**
	 * Add to the cache and save the cache to the shared preferences.
	 * @param beerId The id of the beer that the user starred
	 */
	public void addStar(int beerId) {
		ensureStarCache();
		starCache.add(beerId);
		persistStarCache();

	}

	/**
	 * Remove from the cache and save the cache to the shared preferences.
	 * @param beerId The id of the beer that the user wants removed from its starred beers
	 */
	public void removeStar(int beerId) {
		ensureStarCache();
		starCache.remove(beerId);
		persistStarCache();

	}

	/**
	 * Loads, if needed, the star cache from the single persisted shared preference string.
	 */
	private void ensureStarCache() {
		if (starCache != null)
			return;
		String rawPref = prefs.getString("stars", "");
		if (rawPref.equals("")) {
			starCache = new HashSet<Integer>();
			return;
		}
		String[] rawStars = rawPref.split("\\|");
		starCache = new HashSet<Integer>(rawStars.length);
		for (String beerId : rawStars) {
			if (!beerId.equals(""))
				starCache.add(Integer.parseInt(beerId));
		}
	}

	/**
	 * Persists the already loaded star cache to a single shared preference.
	 */
	private void persistStarCache() {
		if (starCache == null)
			throw new RuntimeException("starCache should be loaded by now; internal coding error");
		StringBuilder stars = new StringBuilder(starCache.size());
		boolean isFirst = true;
		for (Integer beerId : starCache) {
			if (!isFirst) {
				stars.append("|");
			} else {
				isFirst = false;
			}
			stars.append(beerId);
		}
		prefs.edit().putString("stars", stars.toString()).commit();
	}

}
