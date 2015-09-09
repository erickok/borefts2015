package nl.brouwerijdemolen.borefts2013.gui.lists;

import java.io.IOException;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;
import nl.brouwerijdemolen.borefts2013.gui.helpers.MolenTypefaceSpan;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@EViewGroup(R.layout.list_header_brewer)
public class BrewerHeader extends RelativeLayout {

	@Bean
	protected ApiQueue apiQueue;
	@ViewById
	protected ImageView logoImage;
	@ViewById
	protected TextView nameText, originText, weblinkText;

	/**
	 * Constructor for this header view. Only for internal use; use the {@link BrewerHeaderedAdapter} class instead.
	 * @param context The activity context to inflate this view into
	 */
	protected BrewerHeader(Context context) {
		super(context);
	}

	protected void update(Brewer brewer) {
		nameText.setText(MolenTypefaceSpan.makeMolenSpannable(getContext(), brewer.getName()));
		originText.setText(getResources().getString(R.string.info_origin, brewer.getCity(), brewer.getCountry()));
		weblinkText.setText(brewer.getWebsite());
		try {
			logoImage.setImageBitmap(BitmapFactory.decodeStream(getResources().getAssets().open(
					"images/" + brewer.getLogoUrl())));
		} catch (IOException e) {
			// Should never happen, as the brewer logo always exists
			Log.e(BrewerHeader.class.getSimpleName(), e.toString());
		}
	}

	/**
	 * An adapter that shows a header with {@link Brewer} info and a list of the brewer's beers.
	 */
	public static class BrewerHeaderedAdapter extends MergeAdapter {

		public BrewerHeaderedAdapter(Context context, BeerListAdapter beerListAdapter) {
			super();
			addAdapter(new BrewerHeaderAdapter(BrewerHeader_.build(context)));
			addAdapter(beerListAdapter);
		}

		/**
		 * Updates the header of this view with brewer name, etc.
		 * @param brewer The brewer to show the header data for
		 */
		public void updateBrewer(Brewer brewer) {
			((BrewerHeader) ((BrewerHeaderAdapter) getAdapter(0)).getItem(0)).update(brewer);
		}

	}

	/**
	 * A wrapper adapter that only has contains a BrewerHeader view. Only for internal use.
	 */
	private static class BrewerHeaderAdapter extends ViewHolderAdapter {

		public BrewerHeaderAdapter(BrewerHeader view) {
			super(view);
			setViewEnabled(false);
		}

	}

}
