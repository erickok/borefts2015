package nl.brouwerijdemolen.borefts2013.gui.fragments;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import nl.brouwerijdemolen.borefts2013.R;

@EFragment(R.layout.fragment_twitter)
public class TwitterFragment extends Fragment {

	@ViewById
	protected WebView twitterWebview;
	private int height;

	public TwitterFragment() {
		setRetainInstance(true);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@AfterViews
	public void init() {
		twitterWebview.getSettings().setJavaScriptEnabled(true);
		twitterWebview.getSettings().setDomStorageEnabled(true);
		twitterWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)).setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET));
				return true;
			}
		});
		ViewTreeObserver vto = twitterWebview.getViewTreeObserver();
		if (vto.isAlive()) {
			vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
				@SuppressWarnings("deprecation")
				@Override
				public void onGlobalLayout() {
					twitterWebview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
					height = (int) ((twitterWebview.getHeight() - 13) / getResources().getDisplayMetrics().density);
					refreshTwitterFeed();
				}
			});
		}
	}

	/**
	 * Loads the twitter embedded timeline into the WebView. Can be called from the parent activity when the user requests a manual refresh.
	 */
	public void refreshTwitterFeed() {
		if (height == 0)
			return; // Layout not yet initialized
		twitterWebview.loadDataWithBaseURL("https://twitter.com",
				"<html></body><a class=\"twitter-timeline\" data-dnt=\"true\" href=\"https://twitter.com/search" +
						"?q=%23borefts\" data-widget-id=\"375665309723541504\" height=\"" + Integer.toString(height) +
						"\" data-chrome=\"noheader noborders transparent\" data-related=\"molenbier," +
						"molennieuws\">Loading tweets about \"#borefts\"...</a><script>!function(d,s," +
						"id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':" +
						"'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+\"://" +
						"platform.twitter.com/widgets.js\";fjs.parentNode.insertBefore(js,fjs);}}(document," +
						"\"script\",\"twitter-wjs\");</script></body></html>", "text/html", "UTF-8", null);
	}

}
