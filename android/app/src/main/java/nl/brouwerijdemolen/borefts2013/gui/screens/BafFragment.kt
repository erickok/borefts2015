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
import kotlinx.android.synthetic.main.fragment_baf.baf_webview
import nl.brouwerijdemolen.borefts2013.R

class BafFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_baf, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled") // Required to support Facebook
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        baf_webview.settings.javaScriptEnabled = true
        baf_webview.settings.domStorageEnabled = true
        baf_webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val isInFacebook = (request.url.host == "www.facebook.com" || request.url.host == "m.facebook.com")
                return if (isInFacebook) {
                    false // Load in the webview
                } else {
                    startActivity(Intent(Intent.ACTION_VIEW, request.url)
                            .setFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT))
                    true
                }
            }
        }
        baf_webview.loadUrl("https://www.facebook.com/events/1877911625865879/")
    }
}
