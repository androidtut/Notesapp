package com.example.noteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class ShowWebsite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_website);
        WebView myWebView = findViewById(R.id.showwebsite);
        myWebView.loadUrl("https://coderkharal.blogspot.com/");
        myWebView.setWebViewClient(new WebViewController());
    }
}