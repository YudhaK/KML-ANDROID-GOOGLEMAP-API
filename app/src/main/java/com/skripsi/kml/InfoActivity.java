package com.skripsi.kml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class InfoActivity extends AppCompatActivity {

    String content;
    WebView webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        Bundle extras = getIntent().getExtras();
        content = extras.getString("content");
        webview = (WebView)findViewById(R.id.webview);
        webview.loadData(content, "text/html; charset=utf-8", "UTF-8");
    }
    public void onResume(){
        super.onResume();
        getSupportActionBar().setTitle("Info");
    }
}
