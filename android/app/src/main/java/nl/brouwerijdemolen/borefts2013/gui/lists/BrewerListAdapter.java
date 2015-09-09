package nl.brouwerijdemolen.borefts2013.gui.lists;

import java.util.List;

import nl.brouwerijdemolen.borefts2013.api.Brewer;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class BrewerListAdapter extends BaseAdapter {

	private List<Brewer> brewers = null;
	
	@RootContext
	protected Context context;

	/**
	 * Allows updating the full internal list of brewers at once, replacing the old list
	 * @param brewers The new list of brewer objects
	 */
	public void update(List<Brewer> brewers) {
		this.brewers = brewers;
		notifyDataSetChanged();
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public int getCount() {
		if (brewers == null)
			return 0;
		return brewers.size();
	}

	@Override
	public Brewer getItem(int position) {
		if (brewers == null)
			return null;
		return brewers.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BrewerView brewerView;
		if (convertView == null) {
			brewerView = BrewerView_.build(context);
		} else {
			brewerView = (BrewerView) convertView;
		}
		brewerView.bind(getItem(position));
		return brewerView;
	}

}
