package com.skoushan.nyt.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class SingleArticleActivity extends Activity {

    private static String INTENT_URL = "INTENT_URL", INTENT_NAME = "INTENT_NAME";

    public static void newInstance(Context c, String url, String name) {
        c.startActivity(new Intent(c, SingleArticleActivity.class)
                .putExtra(INTENT_URL, url)
                .putExtra(INTENT_NAME, name));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String url = getIntent().getStringExtra(INTENT_URL);
        WebView webview = new WebView(this);

        setTitle(getIntent().getStringExtra(INTENT_NAME));
        // Let's display the progress in the activity title bar, like the
        // browser app does.
        getWindow().requestFeature(Window.FEATURE_PROGRESS);

        webview.getSettings().setJavaScriptEnabled(true);

        final Activity activity = this;
        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                // Activities and WebViews measure progress with different scales.
                // The progress meter will automatically disappear when we reach 100%
                activity.setProgress(progress * 1000);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(activity, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        webview.loadUrl(url);
        setContentView(webview);
    }
}
