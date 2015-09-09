package nl.brouwerijdemolen.borefts2013.gui.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.android.volley.RequestQueue;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EBean.Scope;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import nl.brouwerijdemolen.borefts2013.api.Beers;
import nl.brouwerijdemolen.borefts2013.api.Brewers;
import nl.brouwerijdemolen.borefts2013.api.GsonRequest;
import nl.brouwerijdemolen.borefts2013.api.Styles;

@EBean(scope = Scope.Singleton)
public class ApiQueue {

	private static final Long MAX_CACHE_AGE = 60L * 60L * 1000L; // 1 hour

	private RequestQueue requestQueue;
	private ImageLoader imageLoader;
	private Resources resources;
	private final Gson gson = new Gson();

	private Long cachedBrewersAge = null;
	private Brewers cachedBrewers = null;
	private Long cachedStylesAge = null;
	private Styles cachedStyles = null;
	private Long cachedBeersAge = null;
	private Beers cachedBeers = null;

	public ApiQueue(Context context) {

		requestQueue = Volley.newRequestQueue(context);
		resources = context.getResources();

		// Cache at most three screens worth of pixels (at 4 bytes per pixel)
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		final int maxCacheSize = dm.widthPixels * dm.heightPixels * 4 * 3;
		imageLoader = new ImageLoader(requestQueue, new LruBitmapCache(maxCacheSize));

	}

	public ImageLoader getImageLoader() {
		return imageLoader;
	}

	public void requestBrewers(final Listener<Brewers> listener, final ErrorListener errorListener) {
		if (cachedBrewers != null && cachedBrewersAge != null && cachedBrewersAge + MAX_CACHE_AGE > System.currentTimeMillis()) {
			// Directly return local cache
			listener.onResponse(cachedBrewers);
		}
		final Listener<Brewers> wrappedListener = new Listener<Brewers>() {
			@Override
			public void onResponse(Brewers brewers) {
				cachedBrewers = brewers;
				cachedBrewersAge = System.currentTimeMillis();
				listener.onResponse(cachedBrewers);
			}

			;
		};
		requestQueue.add(new GsonRequest(Brewers.BREWERS_URL, Brewers.class, null, wrappedListener, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// Error response; use the offline version instead
				try {
					InputStream in = resources.getAssets().open("json/brewers.json");
					Brewers brewers = gson.fromJson(new InputStreamReader(in, "UTF-8"), Brewers.class);
					wrappedListener.onResponse(brewers);
				} catch (IOException e) {
					errorListener.onErrorResponse(new VolleyError("Offline loading of JSON asset file failed: " + e.toString()));
				}
			}
		}));
	}

	public void requestStyles(final Listener<Styles> listener, final ErrorListener errorListener) {
		if (cachedStyles != null && cachedStylesAge != null && cachedStylesAge + MAX_CACHE_AGE > System.currentTimeMillis()) {
			// Directly return local cache
			listener.onResponse(cachedStyles);
		}
		final Listener<Styles> wrappedListener = new Listener<Styles>() {
			@Override
			public void onResponse(Styles styles) {
				cachedStyles = styles;
				cachedStylesAge = System.currentTimeMillis();
				listener.onResponse(cachedStyles);
			}

			;
		};
		requestQueue.add(new GsonRequest(Styles.STYLES_URL, Styles.class, null, wrappedListener, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// Error response; use the offline version instead
				try {
					InputStream in = resources.getAssets().open("json/styles.json");
					Styles styles = gson.fromJson(new InputStreamReader(in, "UTF-8"), Styles.class);
					wrappedListener.onResponse(styles);
				} catch (IOException e) {
					errorListener.onErrorResponse(new VolleyError("Offline loading of JSON asset file failed: " + e.toString()));
				}
			}
		}));
	}

	public void requestBeers(final Listener<Beers> listener, final ErrorListener errorListener) {
		if (cachedBeers != null && cachedBeersAge != null && cachedBeersAge + MAX_CACHE_AGE > System.currentTimeMillis()) {
			// Directly return local cache
			listener.onResponse(cachedBeers);
		}
		final Listener<Beers> wrappedListener = new Listener<Beers>() {
			@Override
			public void onResponse(Beers beers) {
				cachedBeers = beers;
				cachedBeersAge = System.currentTimeMillis();
				listener.onResponse(cachedBeers);
			}

			;
		};
		requestQueue.add(new GsonRequest<Beers>(Beers.BEERS_URL, Beers.class, null, wrappedListener, new ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError arg0) {
				// Error response; use the offline version instead
				try {
					InputStream in = resources.getAssets().open("json/beers.json");
					Beers beers = gson.fromJson(new InputStreamReader(in, "UTF-8"), Beers.class);
					wrappedListener.onResponse(beers);
				} catch (IOException e) {
					errorListener.onErrorResponse(new VolleyError("Offline loading of JSON asset file failed: " + e.toString()));
				}
			}
		}));
	}

}
