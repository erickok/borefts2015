package nl.brouwerijdemolen.borefts2013.gui.helpers;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.util.LruCache;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;
import android.text.style.RelativeSizeSpan;

/**
 * Style a {@link Spannable} with the unique De Molen {@link Typeface}, loaded form its .ttf.
 * Taken (slightly tweaked) from http://stackoverflow.com/a/15181195/243165
 * @author Eric Kok <eric@2312.nl>
 */
public class MolenTypefaceSpan extends MetricAffectingSpan {

	private static final String typefaceName = "monalson.ttf";

	/** An <code>LruCache</code> for previously loaded typefaces. */
	private static LruCache<String, Typeface> sTypefaceCache = new LruCache<>(12);

	private Typeface mTypeface;

	/**
	 * Load the {@link Typeface} and apply to a {@link Spannable}.
	 */
	public MolenTypefaceSpan(Context context) {
		mTypeface = sTypefaceCache.get(typefaceName);

		if (mTypeface == null) {
			mTypeface = Typeface.createFromAsset(context.getApplicationContext().getAssets(),
					String.format("fonts/%s", typefaceName));

			// Cache the loaded Typeface
			sTypefaceCache.put(typefaceName, mTypeface);
		}
	}

	@Override
	public void updateMeasureState(TextPaint p) {
		p.setTypeface(mTypeface);

		// Note: This flag is required for proper typeface rendering
		p.setFlags(p.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
	}

	@Override
	public void updateDrawState(TextPaint tp) {
		tp.setTypeface(mTypeface);

		// Note: This flag is required for proper typeface rendering
		tp.setFlags(tp.getFlags() | Paint.SUBPIXEL_TEXT_FLAG);
	}
	
	public static SpannableString makeMolenSpannable(Context context, CharSequence rawText) {
		SpannableString s = new SpannableString(rawText);
		s.setSpan(new RelativeSizeSpan(1.2f), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		s.setSpan(new MolenTypefaceSpan(context), 0, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return s;
	}
	
}
