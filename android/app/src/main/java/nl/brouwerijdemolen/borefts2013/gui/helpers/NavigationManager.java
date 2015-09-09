package nl.brouwerijdemolen.borefts2013.gui.helpers;

import android.support.v4.app.Fragment;

import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Style;

/**
 * Interface for activities that manage the opening and closing of fragments, that is, the phone and tablet main activities.
 * @author Eric Kok <eric@2312.nl>
 */
public interface NavigationManager {

	/**
	 * Open a brewer details (and beer list) fragment for the given id.
	 * @param baseFragment The fragment that requests the opening of a new brewer fragment
	 * @param brewer The brewer to show details (and beer list) for
	 */
	void openBrewer(Fragment baseFragment, Brewer brewer);

	/**
	 * Open a style details (and beer list) fragment for the given id.
	 * @param baseFragment The fragment that requests the opening of a new brewer fragment
	 * @param style The style to show details (and beer list) for
	 */
	void openStyle(Fragment baseFragment, Style style);

	/**
	 * Open a beer details fragment for the given id.
	 * @param baseFragment The fragment that requests the opening of a new beer fragment
	 * @param beer The beer to show details and links for
	 */
	void openBeer(Fragment baseFragment, Beer beer);

	/**
	 * Open a map fragment that focuses on some specific element.
	 * @param baseFragment The fragment that requests the opening of a new map fragment
	 * @param focusId The id of the element to focus on
	 * @param brewerToOpen Optionally a brewer object so this can be shows along with the map marker to focus
	 */
	void openMap(Fragment baseFragment, int focusId, Brewer brewerToOpen);

}
