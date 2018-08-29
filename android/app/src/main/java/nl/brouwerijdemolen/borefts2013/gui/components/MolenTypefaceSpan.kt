package nl.brouwerijdemolen.borefts2013.gui.components

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.support.annotation.StringRes
import android.support.v4.util.LruCache
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.MetricAffectingSpan
import android.text.style.RelativeSizeSpan

/**
 * Style a [Spannable] with the unique De Molen [Typeface], loaded form its .ttf.
 * Taken (slightly tweaked) from http://stackoverflow.com/a/15181195/243165
 * @author Eric Kok <eric></eric>@2312.nl>
 */
class MolenTypefaceSpan(context: Context) : MetricAffectingSpan() {

    private val mTypeface: Typeface =
            sTypefaceCache.get(typefaceName)
                    ?: Typeface.createFromAsset(context.applicationContext.assets, String.format("fonts/%s", typefaceName)).also {
                // Cache the loaded Typeface
                sTypefaceCache.put(typefaceName, it)
            }

    override fun updateMeasureState(p: TextPaint) {
        p.typeface = mTypeface

        // Note: This flag is required for proper typeface rendering
        p.flags = p.flags or Paint.SUBPIXEL_TEXT_FLAG
    }

    override fun updateDrawState(tp: TextPaint) {
        tp.typeface = mTypeface

        // Note: This flag is required for proper typeface rendering
        tp.flags = tp.flags or Paint.SUBPIXEL_TEXT_FLAG
    }

    companion object {

        private const val typefaceName = "monalson.ttf"

        /** An `LruCache` for previously loaded typefaces.  */
        private val sTypefaceCache = LruCache<String, Typeface>(1)

        fun makeMolenSpannable(context: Context, rawText: CharSequence): SpannableString {
            val s = SpannableString(rawText)
            s.setSpan(RelativeSizeSpan(1.25f), 0, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            s.setSpan(MolenTypefaceSpan(context), 0, s.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            return s
        }
    }

}

fun Context.getMolenString(text: CharSequence) = MolenTypefaceSpan.makeMolenSpannable(this, text)

fun Context.getMolenString(@StringRes resId: Int) = MolenTypefaceSpan.makeMolenSpannable(this, getString(resId))
