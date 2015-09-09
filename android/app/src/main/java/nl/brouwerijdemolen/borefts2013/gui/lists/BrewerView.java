/* 
 * Copyright 2010-2013 Eric Kok et al.
 * 
 * Transdroid is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Transdroid is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Transdroid.  If not, see <http://www.gnu.org/licenses/>.
 */
package nl.brouwerijdemolen.borefts2013.gui.lists;

import java.io.IOException;

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Brewer;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * View that represents some {@link Brewer} object and displays name, origin and logo.
 * @author Eric Kok
 */
@EViewGroup(R.layout.list_item_brewer)
public class BrewerView extends RelativeLayout {

	@Bean
	protected ApiQueue apiQueue;
	@ViewById
	protected ImageView logoImage;
	@ViewById
	protected TextView nameText, originText;

	public BrewerView(Context context) {
		super(context);
	}

	public void bind(Brewer brewer) {
		// nameText.setText(MolenTypefaceSpan.makeMolenSpannable(getContext(), brewer.getShortName()));
		nameText.setText(brewer.getShortName());
		originText.setText(getResources().getString(R.string.info_origin, brewer.getCity(), brewer.getCountry()));
		try {
			logoImage.setImageBitmap(BitmapFactory.decodeStream(getResources().getAssets().open(
					"images/" + brewer.getLogoUrl())));
		} catch (IOException e) {
			// Should never happen, as the brewer logo always exists
			Log.e(BrewerHeader.class.getSimpleName(), e.toString());
		}
	}

}
