package nl.brouwerijdemolen.borefts2013.gui.lists;

import java.util.List;

import nl.brouwerijdemolen.borefts2013.api.Style;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

@EBean
public class StyleListAdapter extends BaseAdapter {

	private List<Style> styles = null;
	
	@RootContext
	protected Context context;

	/**
	 * Allows updating the full internal list of styles at once, replacing the old list
	 * @param styles The new list of style objects
	 */
	public void update(List<Style> styles) {
		this.styles = styles;
		notifyDataSetChanged();
	}
	
	@Override
	public boolean hasStableIds() {
		return true;
	}
	
	@Override
	public int getCount() {
		if (styles == null)
			return 0;
		return styles.size();
	}

	@Override
	public Style getItem(int position) {
		if (styles == null)
			return null;
		return styles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StyleView styleView;
		if (convertView == null) {
			styleView = StyleView_.build(context);
		} else {
			styleView = (StyleView) convertView;
		}
		styleView.bind(getItem(position));
		return styleView;
	}

}
