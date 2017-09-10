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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Beer;
import nl.brouwerijdemolen.borefts2013.api.Beers;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.api.Styles;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.NavigationManager;
import nl.brouwerijdemolen.borefts2013.gui.lists.StyleListAdapter;

@EFragment(R.layout.fragment_list)
public class StylesFragment extends Fragment implements ErrorListener, OnItemClickListener {

	@Bean
	protected ApiQueue apiQueue;
	@Bean
	protected StyleListAdapter styleListAdapter;
	@ViewById
	protected ListView theList;
	@ViewById
	protected TextView errorText;
	@ViewById
	protected ProgressBar loadingProgress;

	public StylesFragment() {
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
		apiQueue.requestBeers(new Listener<Beers>() {
			@Override
			public void onResponse(final Beers beers) {
				if (getActivity() == null || !isAdded())
					return;
				apiQueue.requestStyles(new Listener<Styles>() {
					@Override
					public void onResponse(final Styles styles) {
						if (getActivity() == null || !isAdded())
							return;
						showStyles(styles, beers);
					}
				}, StylesFragment.this);
			}
		}, this);
	}

	private void showStyles(Styles styles, Beers beers) {
		// HACK Filter out styles from code if there are no beers in the style
		Iterator<Style> styleIter = styles.getStyles().iterator();
		while (styleIter.hasNext()) {
			Style testStyle = styleIter.next();
			boolean isUsed = false;
			for (Beer beer : beers.getBeers()) {
				if (beer.getStyleId() == testStyle.getId()) {
					isUsed = true;
					break;
				}
			}
			if (!isUsed)
				styleIter.remove();
		}
		Collections.sort(styles.getStyles());
		styleListAdapter.update(styles.getStyles());
		theList.setAdapter(styleListAdapter);
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
		((NavigationManager) getActivity()).openStyle(this, styleListAdapter.getItem(position));
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
