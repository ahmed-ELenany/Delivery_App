package com.mk_tech.delivery.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.GlobalConstants;
import com.mk_tech.delivery.Utilities.LocaleHelper;
import com.mk_tech.delivery.Utilities.UtilsClass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Activity_webView extends AppCompatActivity {
    String url;
    WebView webView;
    ImageView ivBack;
String paymentStatusUrl;
 ProgressBar progressBar;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base, "ar"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        UtilsClass.init_Progress_Dialog(Activity_webView.this);
 
        Intent i = getIntent();
        url = i.getStringExtra("url");
        webView = findViewById(R.id.webview);
        ivBack = findViewById(R.id.ivBack);
         ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);

        webView.setWebViewClient(new MyBrowser());

        if (savedInstanceState != null)
            webView.restoreState(savedInstanceState);
        else
            webView.loadUrl(url);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            if (url.contains(GlobalConstants.main_url)) {
                paymentStatusUrl=url;
                 new getPaymentStatus().execute();
            }
            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Activity_webView.this);
            builder.setMessage("Error ssl certificate invalid");
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap facIcon) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    UtilsClass.Show_Progress_Dialog();
                }
            });
        }

        @Override
        public void onPageFinished(WebView view, String url) {
                 UtilsClass.Dismiss_Progress_Dialog();

         }
    }

    class getPaymentStatus extends AsyncTask<String,Void,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            StringBuilder sb=null;
            BufferedReader reader=null;
            Boolean status=false;
            try {

                URL url = new URL(paymentStatusUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setConnectTimeout(5000);
                connection.setRequestMethod("GET");
                connection.connect();
                int statusCode = connection.getResponseCode();
                //Log.e("statusCode", "" + statusCode);
                if (statusCode == 200) {
                    sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                }

                connection.disconnect();
                if (sb.toString().contains("false")){
                    status=false;
                }else{
                    status=true;
                }
             } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return status;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            super.onPostExecute(status);
             if (status) {
                startActivity(new Intent(Activity_webView.this, Activity_payment_status.class).putExtra("status", "suc"));
                finish();
            } else {
                startActivity(new Intent(Activity_webView.this, Activity_payment_status.class).putExtra("status", "fail"));
                finish();

            }
        }
    }
}
