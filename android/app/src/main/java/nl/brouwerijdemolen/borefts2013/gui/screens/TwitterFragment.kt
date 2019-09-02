package nl.brouwerijdemolen.borefts2013.gui.screens

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.fragment_twitter.twitter_webview
import nl.brouwerijdemolen.borefts2013.R
import nl.brouwerijdemolen.borefts2013.ext.startLink
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
                    requireContext().startLink(request.url)
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
