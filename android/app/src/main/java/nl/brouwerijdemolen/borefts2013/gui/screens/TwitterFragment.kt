package nl.brouwerijdemolen.borefts2013.gui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_twitter.twitter_webview
import nl.brouwerijdemolen.borefts2013.R
import java.util.Locale

class TwitterFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_twitter, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled") // Required to support Facebook
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        twitter_webview.settings.javaScriptEnabled = true
        twitter_webview.settings.domStorageEnabled = true
        twitter_webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val isInTwitter = (request.url.host == "twitter.com" || request.url.host == "mobile.twitter.com")
                return if (isInTwitter) {
                    false // Load in the webview
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, request.url)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT))
                    true
                }
            }
        }
        refreshFeed()
    }

    fun refreshFeed() {
        twitter_webview.loadUrl("https://twitter.com/search?q=borefts&lang=" + Locale.getDefault().country)
    }

}
