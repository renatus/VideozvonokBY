package by.bpk.videozvonok;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    private WebView myWebView;

    // If "Back" button was pressed, app shouldn't close
    // page visited before should be opened rather
    @Override
    public void onBackPressed() {
        if(myWebView.canGoBack()) {
            myWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private class MyWebViewClient extends WebViewClient {
        // Allow to open all links inside WebView with "return false"
        // By default you can't do it
        // Optionally you can set rules on per-domain basis
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            // Get request URL
            String url = String.valueOf(request.getUrl());

            // Let WebView follow links with custom prefix, like Telegram links
            if(url.startsWith("tel:") ||
               url.startsWith("whatsapp:") ||
               url.startsWith("tg:") ||
               url.startsWith("viber:") ||
               url.startsWith("fb-messenger:") ||
               url.startsWith("mailto:")
            ) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
                myWebView.goBack();
                return true;
            }
            /*if ("cls-stream.com".equals(request.getUrl().getHost())) {
                // Let WebView load pages on this website
                return false;
            }
            // Do NOT let WebView follow links to all other sites
            return true;*/

            // Let WebView follow links to all other sites
            return false;
        }
    }

    // Java function to copy text to clipboard
    // You don't need additional permissions in AndroidManifest.xml for this to work.
    // You need to add to super.onCreate this to make it possible to call Java from JS:
    // myWebView.addJavascriptInterface(new WebAppInterface(), "NativeAndroid");
    // You'll call this function from JS code like this:
    // NativeAndroid.copyToClipboard("Clipboard API Test");
    // Clipboard is one of the permissions that are not implemented in Android WebView.
    // So you can't use JS navigator.clipboard.writeText inside Android WebView-based app,
    // it will silently fail.
    // However you still need to implement it, if your app is available inside web browser
    // as well as in AndroidWebView:
    // Copy link to Clipboard Buffer and notify user
    // function bpkLinkCopy(textToCopy, msgToUser){
    //   if (typeof NativeAndroid !== 'undefined') {
        // If we're	inside Anroid WebView app, copy	text with Java
    //     NativeAndroid.copyToClipboard(textToCopy);
    //   } else {
        // If we're	inside normal browser, copy text with JS
    //     navigator.clipboard.writeText(textToCopy);
    //   }
    // }
    public class WebAppInterface {
        @JavascriptInterface
        public void copyToClipboard(String text) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            // ClipData "Label" is NOT being displayed to users
            // unless app implements this functionality somehow
            ClipData clip = ClipData.newPlainText("Copied text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    // Check if multiple permissions are granted
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The request code used in ActivityCompat.requestPermissions()
        // and returned in the Activity's onRequestPermissionsResult()
        // Values are arbitrary and unique for each permission or for
        // each permission group
        int PERMISSION_ALL = 1;
        // List of permissions we need
        String[] PERMISSIONS = {
                android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.CAMERA
        };

        // If some permissions were not already granted
        if (!hasPermissions(this, PERMISSIONS)) {
            // Request multiple permissions
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        // Hide top bar with app name
        getSupportActionBar().hide();
        // Create WebView to open website
        myWebView = new WebView(MainActivity.this);
        WebSettings webSettings = myWebView.getSettings();
        // Enable JavaScript for this WebView
        webSettings.setJavaScriptEnabled(true);
        // Add interface to work with Java from JavaScript
        myWebView.addJavascriptInterface(new WebAppInterface(), "NativeAndroid");
        // To alter some WebView settings
        // Do not add code inside "new MyWebViewClient()", add it here:
        // private class MyWebViewClient extends WebViewClient {
        myWebView.setWebViewClient(new MyWebViewClient());
        // Pass granted permissions from app to WebView
        myWebView.setWebChromeClient(new WebChromeClient() {
            // Request permissions inside WebView
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
            request.grant(request.getResources());
            }
        });
        setContentView(myWebView);
        // To prevent app from reloading webpage on each screen autorotate, we need:
        // android:configChanges="orientation|screenSize">
        // in AndroidManifest.xml <activity
        // and to check if savedInstanceState == null before loading site
        // myWebView.loadUrl("https://videozvonok.by");
        // in MainActivity.java super.onCreate
        if (savedInstanceState == null) {
            // Load site
            myWebView.loadUrl("https://videozvonok.by");
            // URLs useful for testing
            //myWebView.loadUrl("https://videozvonok.contacts.cls-lms.info");
            //myWebView.loadUrl("https://html5test.com/");
            //myWebView.loadUrl("https://webcamtests.com/");
            //myWebView.loadUrl("https://mictests.com/");
        }
    }
}