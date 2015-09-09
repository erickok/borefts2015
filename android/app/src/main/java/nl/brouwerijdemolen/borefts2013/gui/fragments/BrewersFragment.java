package nl.brouwerijdemolen.borefts2013.gui.fragments;

import android.support.v4.app.Fragment;
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
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.Comparator;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.api.Brewers;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import nl.brouwerijdemolen.borefts2013.gui.lists.BrewerListAdapter;

@EFragment(R.layout.fragment_list)
public class BrewersFragment extends Fragment implements Listener<Brewers>, ErrorListener, OnItemClickListener {

	@Bean
	protected ApiQueue apiQueue;
	@Bean
	protected BrewerListAdapter brewerListAdapter;
	@ViewById
	protected ListView theList;
	@ViewById
	protected TextView errorText;
	@ViewById
	protected ProgressBar loadingProgress;

	public BrewersFragment() {
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
		apiQueue.requestBrewers(this, this);
	}

	@Override
	public void onResponse(Brewers brewers) {
		if (getActivity() == null || !isAdded())
			return;
		Collections.sort(brewers.getBrewers(), brewersComparator);
		brewerListAdapter.update(brewers.getBrewers());
		theList.setAdapter(brewerListAdapter);
		theList.setVisibility(View.VISIBLE);
		errorText.setVisibility(View.GONE);
		loadingProgress.setVisibility(View.GONE);
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		theList.setVisibility(View.GONE);
		errorText.setVisibility(View.VISIBLE);
		loadingProgress.setVisibility(View.GONE);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		((NavigationManager) getActivity()).openBrewer(this, brewerListAdapter.getItem(position));
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

	private final Comparator<Brewer> brewersComparator = new Comparator<Brewer>() {
		@Override
		public int compare(Brewer lhs, Brewer rhs) {
			// De Molen (ID 0) itself is always first in the list, the rest is handled by the Brewer objects comparable 
			// implementation (on the brewer's sort name)
			if (lhs.getId() == 0)
				return -1;
			if (rhs.getId() == 0)
				return 1;
			return lhs.compareTo(rhs);
		}

	};

}
