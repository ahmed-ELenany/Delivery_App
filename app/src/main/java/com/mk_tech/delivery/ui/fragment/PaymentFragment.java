package com.mk_tech.delivery.ui.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.mk_tech.delivery.R;
import com.mk_tech.delivery.Utilities.UtilsClass;


public class PaymentFragment extends Fragment {

    String url;
    WebView webView;

    public PaymentFragment(String url) {
        this.url = url;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        webView = getActivity().findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.loadUrl(url);

        webView.setWebViewClient(new Mybrowser());

        if (savedInstanceState != null)
            webView.restoreState(savedInstanceState);
        else
            webView.loadUrl(url);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.webview, container, false);
        UtilsClass.init_Progress_Dialog(getActivity());
        return view;
    }

    private class Mybrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);


            return true;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            UtilsClass.Show_Progress_Dialog();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            UtilsClass.Dismiss_Progress_Dialog();
        }
    }

}