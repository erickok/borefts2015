package nl.brouwerijdemolen.borefts2013.gui.fragments;

import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.api.Beers;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Brewers;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.api.Styles;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import nl.brouwerijdemolen.borefts2013.gui.helpers.StarPersistance;
import nl.brouwerijdemolen.borefts2013.gui.lists.BeerListAdapter;

@EFragment(R.layout.fragment_list)
public class StarredFragment extends Fragment implements ErrorListener, OnItemClickListener {

	@Bean
	protected StarPersistance stars;
	private SparseArray<Brewer> loadedBrewers;
	private SparseArray<Style> loadedStyles;

	@Bean
	protected ApiQueue apiQueue;
	@ViewById
	protected ListView theList;
	@ViewById
	protected TextView errorText;
	@ViewById
	protected ProgressBar loadingProgress;
	@ViewById
	protected TextView emptyText;
	@Bean
	protected BeerListAdapter beerListAdapter;

	public StarredFragment() {
		setRetainInstance(true);
	}

	@AfterViews
	protected void init() {
		theList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		theList.setOnItemClickListener(this);
		errorText.setOnClickListener(onRetry);
		emptyText.setText(R.string.info_nostars);
		refreshScreen();
	}

	private void refreshScreen() {
		if (stars.isEmpty()) {
			updateErrorViews(false, true);
			return;
		}
		// Load the brewers first, as we will need this info to display every beer's brewer
		apiQueue.requestBrewers(new Listener<Brewers>() {
			@Override
			public void onResponse(Brewers brewers) {
				if (getActivity() == null || !isAdded())
					return;
				handleBrewersResult(brewers.getBrewers());
			}
		}, this);
	}

	@Background
	protected void handleBrewersResult(List<Brewer> brewers) {
		this.loadedBrewers = new SparseArray<Brewer>();
		for (Brewer brewer : brewers) {
			this.loadedBrewers.append(brewer.getId(), brewer);
		}
		// Now we have loaded and sorted the brewers, start (via the UI thread) the loading of the styles
		apiQueue.requestStyles(new Listener<Styles>() {
			@Override
			public void onResponse(Styles styles) {
				if (getActivity() == null || !isAdded())
					return;
				handleStylesResult(styles.getStyles());
			}
		}, this);
	}

	@Background
	protected void handleStylesResult(List<Style> styles) {
		this.loadedStyles = new SparseArray<Style>();
		for (Style style : styles) {
			this.loadedStyles.append(style.getId(), style);
		}
		// Now we have loaded and sorted the styles, start (via the UI thread) the loading of the beers
		startBeersRequest();
	}

	@UiThread
	protected void startBeersRequest() {
		apiQueue.requestBeers(new Listener<Beers>() {
			@Override
			public void onResponse(Beers beers) {
				if (getActivity() == null || !isAdded())
					return;
				// Beers are loaded now too; search for those that are starred and bind the beer brewer/style
				List<Beer> beersList = new ArrayList<Beer>();
				for (Beer beer : beers.getBeers()) {
					if (stars.isStarred(beer.getId())) {
						beer.setBrewer(loadedBrewers.get(beer.getBrewerId()));
						beer.setStyle(loadedStyles.get(beer.getStyleId()));
						beersList.add(beer);
					}
				}
				Collections.sort(beersList);
				// Show the beers in the list view
				beerListAdapter.update(beersList, false);
				theList.setAdapter(beerListAdapter);
				updateErrorViews(false, false);
			}
		}, this);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		updateErrorViews(true, false);
	}

	private void updateErrorViews(boolean showAsError, boolean showAsEmpty) {
		theList.setVisibility(!showAsError && !showAsEmpty ? View.VISIBLE : View.GONE);
		errorText.setVisibility(showAsError ? View.VISIBLE : View.GONE);
		emptyText.setVisibility(!showAsError && showAsEmpty ? View.VISIBLE : View.GONE);
		loadingProgress.setVisibility(View.GONE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		((NavigationManager) getActivity()).openBeer(this, ((Beer) theList.getItemAtPosition(position)));
	}

	private OnClickListener onRetry = new OnClickListener() {
		@Override
		public void onClick(View v) {
			theList.setVisibility(View.GONE);
			errorText.setVisibility(View.GONE);
			loadingProgress.setVisibility(View.VISIBLE);
			refreshScreen();
		}
	};

}
