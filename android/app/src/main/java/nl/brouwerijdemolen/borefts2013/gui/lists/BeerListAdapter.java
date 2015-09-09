package nl.brouwerijdemolen.borefts2013.gui.lists;

import java.util.List;

import nl.brouwerijdemolen.borefts2013.api.Beer;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class BeerListAdapter extends BaseAdapter {

	private List<Beer> beers = null;
	private boolean showStyleName;
	
	@RootContext
	protected Context context;

	/**
	 * Allows updating the full internal list of beers at once, replacing the old list
	 * @param beers The new list of beer objects
	 * @param showStyleName Whether to show the style name in the UI instead of the brewer name
	 */
	public void update(List<Beer> beers, boolean showStyleName) {
		this.beers = beers;
		this.showStyleName = showStyleName;
		notifyDataSetChanged();
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public int getCount() {
		if (beers == null)
			return 0;
		return beers.size();
	}

	@Override
	public Beer getItem(int position) {
		if (beers == null)
			return null;
		return beers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BeerView brewerView;
		if (convertView == null) {
			brewerView = BeerView_.build(context);
		} else {
			brewerView = (BeerView) convertView;
		}
		brewerView.bind(getItem(position), showStyleName);
		return brewerView;
	}

}
