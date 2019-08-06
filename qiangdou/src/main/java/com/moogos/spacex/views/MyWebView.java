package com.moogos.spacex.views;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.security.NoSuchAlgorithmException;

/**
 * 
 */
public class MyWebView extends WebView {


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (this.canGoBack()) {
				goBack();
				return true;
			}
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public MyWebView(Context context, AttributeSet attrs) {
		super(context,attrs);
		setFocusable(true);
		requestFocus(View.FOCUS_DOWN);

		setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);

		WebSettings settings = getSettings();
		// basic
		settings.setJavaScriptEnabled(true);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
		settings.setNeedInitialFocus(false);
		// zoom
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		// other
		// settings.setPluginsEnabled(true);
		settings.setAllowFileAccess(true);

		// API 7, LocalStorage/SessionStorage
		settings.setDomStorageEnabled(true);
		settings.setDatabasePath(context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath());
		settings.setDatabaseEnabled(true);
		// API 7， Application Storage
		settings.setAppCacheEnabled(true);
		settings.setAppCachePath(context.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath());
		settings.setAppCacheMaxSize(5 * 1024 * 1024);

		// API 5， Geolocation
		settings.setGeolocationEnabled(true);
		settings.setGeolocationDatabasePath(context.getApplicationContext().getDir("database", Context.MODE_PRIVATE)
				.getPath());

		setWebChromeClient(new WebChromeClient());

		this.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {
				super.onLoadResource(view, url);

			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if(url.indexOf("shouji.360.cn")>=0){
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    view.getContext().startActivity(i);
                    return true;
				}else{
					view.loadUrl(url);
					return true;
				}
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);
			}

			@Override
			public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
				// handler.cancel(); // 默认的处理方式，WebView变成空白页
				handler.proceed();// 接受证书
			}
		});

	}

	public static String getMD5(String txt) {
		byte[] source = txt.getBytes();

		String s = null;
		char hexDigits[] = { // to hex
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
		try {
			java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
			md5.update(source);
			byte tmp[] = md5.digest();
			char str[] = new char[16 * 2];
			int k = 0;
			for (int i = 0; i < 16; i++) {
				byte byte0 = tmp[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			s = new String(str);

		} catch (NoSuchAlgorithmException e) {
		}
		return s;
	}

}
