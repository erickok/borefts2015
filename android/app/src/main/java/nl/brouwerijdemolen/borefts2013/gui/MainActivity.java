package nl.brouwerijdemolen.borefts2013.gui;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.gui.fragments.AboutDialog_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BafFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BafFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BrewersFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BrewersFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.InfoFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.InfoFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.StarredFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.StarredFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.StylesFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.StylesFragment_;
import nl.brouwerijdemolen.borefts2013.gui.fragments.TwitterFragment;
import nl.brouwerijdemolen.borefts2013.gui.fragments.TwitterFragment_;
import nl.brouwerijdemolen.borefts2013.gui.helpers.AppRater;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;

@EActivity(R.layout.activity_main)
@OptionsMenu(R.menu.activity_start)
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener, NavigationManager {

	@ViewById
	protected Toolbar titleToobar;
	@ViewById
	protected TextView titleText;
	@ViewById
	protected TabLayout tabLayout;
	@ViewById
	protected ViewPager tabViewPager;

	private InfoFragment infoFragment = null;
	private BrewersFragment brewersFragment = null;
	private StylesFragment stylesFragment = null;
	private TwitterFragment twitterFragment = null;
	private StarredFragment starredFragment = null;
	private BafFragment bafFragment = null;

	@Bean
	protected AppRater appRater;

	@AfterViews
	protected void init() {

		setSupportActionBar(titleToobar);
		getSupportActionBar().setTitle(null);
		titleText.setText(MolenTypefaceSpan.makeMolenSpannable(this, getString(R.string.app_name_short)));

		// Bind tabs
		tabViewPager.setAdapter(new TabPageAdapter(getSupportFragmentManager()));
		tabViewPager.addOnPageChangeListener(this);
		tabLayout.setupWithViewPager(tabViewPager);

		appRater.hit();
		if (appRater.shouldShow()) {
			Snackbar snackbar = Snackbar.make(tabViewPager, R.string.rate_title, Snackbar.LENGTH_LONG);
			snackbar.setActionTextColor(getResources().getColor(R.color.yellow));
			snackbar.setAction(R.string.rate_confirm, new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					appRater.block();
					try {
						startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("market://details?id=nl.brouwerijdemolen.borefts2013")));
					} catch (Exception e) {
						// No Play Store installed: ignore
					}
				}
			});
			snackbar.show();
		}
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.action_refresh).setVisible(tabViewPager.getCurrentItem() == 3);
		return true;
	}

	@OptionsItem
	protected void actionRefresh() {
		if (twitterFragment != null)
			twitterFragment.refreshTwitterFeed();
	}

	@OptionsItem
	protected void actionSendcorrection() {
		Intent startEmail = new Intent(Intent.ACTION_SEND);
		startEmail.setType("message/rfc822");
		startEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{"borefts2017@2312.nl"});
		startEmail.putExtra(Intent.EXTRA_SUBJECT, "Borefts 2017 Android app correction");
		try {
			startActivity(startEmail);
		} catch (ActivityNotFoundException e) {
			// Ignore; normal devices always have an app to send emails, but at least do not crash
		}
	}

	@OptionsItem
	protected void actionAbout() {
		AboutDialog_.builder().build().show(getSupportFragmentManager(), "about");
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
	public void openMap(Fragment baseFragment) {
		ContainerActivity_.intent(this).start();
	}

	@Override
	public void openMap(Fragment baseFragment, int brewerToOpen) {
		ContainerActivity_.intent(this).focusBrewerId(brewerToOpen).start();
	}

	@Override
	public void openMap(Fragment baseFragment, String poiToOpen) {
		ContainerActivity_.intent(this).focusPoiId(poiToOpen).start();
	}

	@Override
	public void onPageSelected(int position) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
		// Ignore
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// Ignore
	}

	private class TabPageAdapter extends FragmentPagerAdapter {

		private final int[] TITLES =
				{R.string.action_info, R.string.action_brewers, R.string.action_styles, R.string.info_stars, R.string.action_twitter,
						R.string.info_baf};

		public TabPageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
				case 0:
					if (infoFragment == null)
						infoFragment = InfoFragment_.builder().build();
					return infoFragment;
				case 1:
					if (brewersFragment == null)
						brewersFragment = BrewersFragment_.builder().build();
					return brewersFragment;
				case 2:
					if (stylesFragment == null)
						stylesFragment = StylesFragment_.builder().build();
					return stylesFragment;
				case 3:
					if (starredFragment == null)
						starredFragment = StarredFragment_.builder().build();
					return starredFragment;
				case 4:
					if (twitterFragment == null)
						twitterFragment = TwitterFragment_.builder().build();
					return twitterFragment;
				case 5:
					if (bafFragment == null)
						bafFragment = BafFragment_.builder().build();
					return bafFragment;
			}
			return null;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return getString(TITLES[position]);
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

	}

}
