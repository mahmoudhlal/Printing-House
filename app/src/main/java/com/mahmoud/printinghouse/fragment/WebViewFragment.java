package com.mahmoud.printinghouse.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mahmoud.printinghouse.MainActivity;
import com.mahmoud.printinghouse.R;
import com.mahmoud.printinghouse.Utils.ParentClass;
import com.mahmoud.printinghouse.databinding.FragmentWebViewBinding;

import im.delight.android.webview.AdvancedWebView;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment implements AdvancedWebView.Listener {
    private String ShowOrHideWebViewInitialUse = "show";
    private String stringURL;
    private FragmentWebViewBinding mBinding ;
    private NavController navController ;
    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ParentClass.showWaiting(getContext());
        assert getArguments() != null;
        stringURL=getArguments().getString("URL");
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_web_view,container,false);
        return mBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        mBinding.toolbar.title.setText("Shield");
        mBinding.toolbar.back.setOnClickListener(x->navController.navigateUp());
        mBinding.webview.getSettings().setJavaScriptEnabled(true);
        mBinding.webview.getSettings().setDomStorageEnabled(true);
        mBinding.webview.setGeolocationEnabled(false);
        mBinding.webview.setMixedContentAllowed(true);
        mBinding.webview.setCookiesEnabled(true);
        mBinding.webview.setThirdPartyCookiesEnabled(true);
        mBinding.webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        mBinding.webview.loadUrl(stringURL);
        mBinding.webview.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        mBinding.webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                ParentClass.dismissDialog();
            }
        });

    }

    @Override
    public void onPageStarted(String url, Bitmap favicon) {
        ParentClass.showWaiting(getContext());
    }

    @Override
    public void onPageFinished(String url) {
        ParentClass.dismissDialog();
    }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) {
        ParentClass.dismissDialog();
    }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType,
                                    long contentLength, String contentDisposition, String userAgent) {

    }

    @Override
    public void onExternalPageRequest(String url) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mBinding.webview.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBinding.webview.onResume();
    }

    @Override
    public void onPause() {
        mBinding.webview.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mBinding.webview.onDestroy();
        super.onDestroy();
    }

}
