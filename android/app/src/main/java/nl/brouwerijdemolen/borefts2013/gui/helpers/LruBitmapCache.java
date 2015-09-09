package nl.brouwerijdemolen.borefts2013.gui.helpers;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;

/**
 * A simple LRU cache for bitmaps; taken from https://gist.github.com/ficusk/5614325
 */
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {

	public LruBitmapCache(int maxSize) {
		super(maxSize);
	}

	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}

	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}

	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}

}