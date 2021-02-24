package com.example.dailynews.webview;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.dailynews.R;
import com.example.dailynews.connectivity.InternetConnectivity;
import com.example.dailynews.views.HomeActivity;

public class WebViewActivity extends AppCompatActivity {
    WebView webview;
    AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webview = findViewById(R.id.webView);
        Intent intent = getIntent();
        if(intent!=null)
        {
            String link = intent.getStringExtra("link");
            webview.getSettings().setJavaScriptEnabled(true);
            webview.setWebViewClient(new Browser());
            if(InternetConnectivity.isNetworkConnected(getApplicationContext()))
            webview.loadUrl(link);
            else
            {
                internetStopDialog();
            }
        }
    }

    class Browser extends WebViewClient
    {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public void internetStopDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
        builder.setMessage("Internet connectivity is required for app functioning ! ");
        builder.setPositiveButton("Wifi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent("android.settings.WIFI_SETTINGS");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                dialog.dismiss();
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Mobile Data", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(Build.VERSION.SDK_INT > 15)
                {
                    Intent intent = new Intent("android.settings.NETWORK_OPERATOR_SETTINGS");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialog.dismiss();
                    startActivity(intent);
                }
                else
                {
                    Intent intent=new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                    ComponentName cName = new ComponentName("com.android.phone","com.android.phone.Settings");
                    intent.setComponent(cName);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    dialog.dismiss();
                    startActivity(intent);
                }
            }
        });
        builder.setNeutralButton("Exit",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                finish();
            }
        });
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }
}