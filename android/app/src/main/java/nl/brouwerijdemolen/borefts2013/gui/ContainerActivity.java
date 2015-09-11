package nl.brouwerijdemolen.borefts2013.gui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.ViewById;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BeerFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BrewerFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.MapFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.StyleFragment_;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;

/**
 * A wrapper activity that can open details screens for brewers, beers and styles by supplying the appropriate Extra. The actual content is handled by
 * the normal fragment for whatever content was requested.
 * @author Eric Kok <eric@2312.nl>
 */
@EActivity(R.layout.activity_empty)
public class ContainerActivity extends AppCompatActivity implements NavigationManager {

	@ViewById
	protected Toolbar titleToobar;
	@ViewById
	protected TextView titleText;

	@Extra
	protected Brewer brewer = null;
	@Extra
	protected Style style = null;
	@Extra
	protected Beer beer = null;
	@Extra
	protected Integer focusId = null;

	@AfterViews
	protected void openFragment() {

		// Set up the simple action bar with up navigation
		setSupportActionBar(titleToobar);
		getSupportActionBar().setTitle(null);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		titleText.setText(MolenTypefaceSpan.makeMolenSpannable(this, getString(R.string.app_name_short)));

		// Get the fragment to open based on the supplied Extra
		Fragment fragment = null;
		if (brewer != null) {
			fragment = BrewerFragment_.builder().brewer(brewer).build();
		} else if (style != null) {
			fragment = StyleFragment_.builder().style(style).build();
		} else if (beer != null) {
			fragment = BeerFragment_.builder().beer(beer).build();
		} else if (focusId != null) {
			fragment = MapFragment_.builder().initFocusId(focusId).isMinimap(false).build();
		}
		if (fragment == null) {
			throw new IllegalArgumentException("Don't know which fragment to open, since no Extra was specified.");
		}

		// Replace the activity contents with the new fragment
		getSupportFragmentManager().beginTransaction().add(R.id.contentFrame, fragment).commit();

	}

	@SuppressLint("InlinedApi")
	@OptionsItem(android.R.id.home)
	protected void homeClicked() {
		MainActivity_.intent(this).flags(Intent.FLAG_ACTIVITY_CLEAR_TOP).start();
	}

	@Override
	public void openBrewer(Fragment baseFragment, Brewer brewer) {
		ContainerActivity_.intent(this).brewer(brewer).start();
	}

	@Override
	public void openStyle(Fragment baseFragment, Style style) {
		ContainerActivity_.intent(this).style(style).start();
	}

	@Override
	public void openBeer(Fragment baseFragment, Beer beer) {
		ContainerActivity_.intent(this).beer(beer).start();
	}

	@Override
	public void openMap(Fragment baseFragment, int focusId, Brewer brewerToOpen) {
		ContainerActivity_.intent(this).focusId(focusId).start();
	}

}
