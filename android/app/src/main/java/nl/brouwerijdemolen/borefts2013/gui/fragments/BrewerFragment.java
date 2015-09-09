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
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.api.Beers;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.api.Styles;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import nl.brouwerijdemolen.borefts2013.gui.lists.BeerListAdapter;
import nl.brouwerijdemolen.borefts2013.gui.lists.BrewerHeader;

@EFragment(R.layout.fragment_list)
public class BrewerFragment extends Fragment implements Listener<Styles>, ErrorListener, OnItemClickListener {

	@FragmentArg
	@InstanceState
	protected Brewer brewer;
	private SparseArray<Style> loadedStyles;

	@Bean
	protected ApiQueue apiQueue;
	@ViewById
	protected ListView theList;
	@ViewById
	protected TextView errorText;
	@ViewById
	protected ProgressBar loadingProgress;
	@Bean
	protected BeerListAdapter beerListAdapter;

	public BrewerFragment() {
		setRetainInstance(true);
	}

	@AfterViews
	protected void init() {
		theList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		theList.setOnItemClickListener(this);
		errorText.setOnClickListener(onRetry);
		refreshScreen();
	}

	private void refreshScreen() {
		// Load the styles first, as we will need this info to display every beer's style
		apiQueue.requestStyles(this, this);
	}

	@Override
	public void onResponse(Styles styles) {
		if (getActivity() == null || !isAdded())
			return;
		// Handle the styles in a background thread
		handleStylesResult(styles.getStyles());
	}

	@Background
	protected void handleStylesResult(List<Style> styles) {
		this.loadedStyles = new SparseArray<>();
		for (Style style : styles) {
			this.loadedStyles.append(style.getId(), style);
		}
		// Now we have loaded and sorted the styles, start (via the UI thread) the loading of this brewer's beers
		startBeersRequest();
	}

	@UiThread
	protected void startBeersRequest() {
		apiQueue.requestBeers(new Listener<Beers>() {
			@Override
			public void onResponse(Beers beers) {
				if (getActivity() == null || brewer == null)
					return;
				// Beers are loaded now too; sort them and add the style and brewer objects
				List<Beer> beersList = new ArrayList<>();
				for (Beer beer : beers.getBeers()) {
					if (beer.getBrewerId() == brewer.getId()) {
						beer.setStyle(loadedStyles.get(beer.getStyleId()));
						beer.setBrewer(brewer);
						beersList.add(beer);
					}
				}
				Collections.sort(beersList);
				// Show the beers in the list view
				beerListAdapter.update(beersList, true);
				BrewerHeader.BrewerHeaderedAdapter adapter = new BrewerHeader.BrewerHeaderedAdapter(getActivity(), beerListAdapter);
				adapter.updateBrewer(brewer);
				theList.setAdapter(adapter);
				theList.setVisibility(View.VISIBLE);
				errorText.setVisibility(View.GONE);
				loadingProgress.setVisibility(View.GONE);
			}
		}, this);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		theList.setVisibility(View.GONE);
		errorText.setVisibility(View.VISIBLE);
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
