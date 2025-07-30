package com.ivision.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.webkit.WebViewClient;

import com.ivision.databinding.ActivityWebViewBinding;

public class WebViewActivity extends BaseActivity {

    private ActivityWebViewBinding binding;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        context = WebViewActivity.this;

        setToolbar("Track Vehicle");

        if (getIntent() != null) {
            if (getIntent().hasExtra("title")) {
                setToolbar(getIntent().getStringExtra("title"));
            }
            if (getIntent().hasExtra("url")) {
                url = getIntent().getStringExtra("url");
                Log.e(TAG, "onCreate: " + url);
            }
        }

        init();
    }

    private void init() {
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setSupportZoom(true);
        binding.webView.setWebViewClient(new WebViewClient());

        try {
            binding.webView.loadUrl(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onResume() {
        super.onResume();
        binding.webView.onResume();
    }

    @SuppressLint("NewApi")
    @Override
    protected void onPause() {
        binding.webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (binding.webView.canGoBack()) {
                        binding.webView.goBack();
                    } else {
                        finish();
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
