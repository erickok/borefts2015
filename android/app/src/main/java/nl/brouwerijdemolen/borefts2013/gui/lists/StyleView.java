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

import nl.brouwerijdemolen.borefts2013.R;
import nl.brouwerijdemolen.borefts2013.api.Style;
import nl.brouwerijdemolen.borefts2013.gui.helpers.ApiQueue;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * View that represents some {@link Style} object and displays a color indication.
 * @author Eric Kok
 */
@EViewGroup(R.layout.list_item_style)
public class StyleView extends RelativeLayout {

	@Bean
	protected ApiQueue apiQueue;
	@ViewById
	protected View colorView;
	@ViewById
	protected TextView nameText;

	public StyleView(Context context) {
		super(context);
	}

	public void bind(Style style) {
		//nameText.setText(MolenTypefaceSpan.makeMolenSpannable(getContext(), style.getName()));
		nameText.setText(style.getName());
		colorView.setBackgroundColor(style.getColorResource(getContext().getResources()));
	}

}
