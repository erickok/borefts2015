package nl.brouwerijdemolen.borefts2013.gui.lists;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.gui.fragments.BarView;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;
import nl.brouwerijdemolen.borefts2013.gui.lists.BrewerHeader.BrewerHeaderedAdapter;

@EViewGroup(R.layout.list_header_style)
public class StyleHeader extends RelativeLayout {

	@Bean
	protected ApiQueue apiQueue;
	@ViewById
	protected TextView nameText;
	@ViewById
	protected View colorView;
	@ViewById
	protected BarView abvView, bitternessView, sweetnessView, acidityView;

	/**
	 * Constructor for this header view. Only for internal use; use the {@link BrewerHeaderedAdapter} class instead.
	 * @param context The activity context to inflate this view into
	 */
	protected StyleHeader(Context context) {
		super(context);
	}

	protected void update(Style style) {
		nameText.setText(MolenTypefaceSpan.makeMolenSpannable(getContext(), style.getName()));
		colorView.setBackgroundColor(style.getColorResource(getResources()));
		abvView.setValue(style.getAbv());
		bitternessView.setValue(style.getBitterness());
		sweetnessView.setValue(style.getSweetness());
		acidityView.setValue(style.getAcidity());
	}

	/**
	 * An adapter that shows a header with {@link Style} info and a list of beers in the style.
	 */
	public static class StyleHeaderedAdapter extends MergeAdapter {

		public StyleHeaderedAdapter(Context context, BeerListAdapter beerListAdapter) {
			super();
			addAdapter(new StyleHeaderAdapter(StyleHeader_.build(context)));
			addAdapter(beerListAdapter);
		}

		/**
		 * Updates the header of this view with style name, etc.
		 * @param style The style to show the header data for
		 */
		public void updateStyle(Style style) {
			((StyleHeader) ((StyleHeaderAdapter) getAdapter(0)).getItem(0)).update(style);
		}

	}

	/**
	 * A wrapper adapter that only has contains a StyleHeader view. Only for internal use.
	 */
	private static class StyleHeaderAdapter extends ViewHolderAdapter {

		public StyleHeaderAdapter(StyleHeader view) {
			super(view);
			setViewEnabled(false);
		}

	}

}
