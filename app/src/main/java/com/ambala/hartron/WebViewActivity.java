package com.ambala.hartron;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.URLUtil;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "WebViewActivity";
    private WebView myWebView;
    private String mUrl = "http://www.google.com";

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        String action_bar_title = "Hartron";

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {

                mUrl = bundle.getString("URL_STRING");
                action_bar_title = bundle.getString("TITLE");

            }
        }

        ActionBar actionBar = getSupportActionBar();      // REQUIRED TO MAKE CHANGES ON ACTION BAR
        //actionBar.setTitle(getResources().getString(R.string.app_name));
        actionBar.setTitle(""+action_bar_title);
        //actionBar.hide();


        progressBar = findViewById(R.id.webView_progress_bar);
        myWebView = findViewById(R.id.webView);

        if (Build.VERSION.SDK_INT >=24){
            //   searchWebView.setWebChromeClient(new WebChromeClient());
            myWebView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    String url = request.getUrl().toString();
                    if (URLUtil.isNetworkUrl(url)) {
                        return false;
                    }
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);

                    } catch (ActivityNotFoundException e) {
                        // Log.e("AndroiRide", e.toString());
                        // Toast.makeText(WebViewActivity.this, "No activity found", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }else {
            myWebView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (URLUtil.isNetworkUrl(url)) {
                        return false;
                    }
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);

                    } catch (ActivityNotFoundException e) {
                        // Log.e("AndroiRide", e.toString());
                        // Toast.makeText(WebViewActivity.this, "No activity found", Toast.LENGTH_LONG).show();
                    }
                    return true;
                }
            });
        }
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.loadUrl(mUrl);
    }
}
