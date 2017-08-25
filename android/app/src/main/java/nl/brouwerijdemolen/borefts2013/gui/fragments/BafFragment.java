package nl.brouwerijdemolen.borefts2013.gui.fragments;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import nl.brouwerijdemolen.borefts2013.R;

@EFragment(R.layout.fragment_baf)
public class BafFragment extends Fragment {

	@ViewById
	protected WebView bafWebview;

	public BafFragment() {
		setRetainInstance(true);
	}

	@SuppressLint("SetJavaScriptEnabled")
	@AfterViews
	public void init() {
		bafWebview.getSettings().setJavaScriptEnabled(true);
		bafWebview.getSettings().setDomStorageEnabled(true);
		bafWebview.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("https://www.facebook.com") || url.startsWith("https://m.facebook.com/"))
					return false;
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)).setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET));
				return true;
			}
		});
		bafWebview.loadUrl("https://www.facebook.com/events/1877911625865879/");
	}

}
