package by.bpk.videozvonok;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    private class MyWebViewClient extends WebViewClient {
        // Allow to open all links inside WebView with "return false"
        // By default you can't do it
        // Optionally you can set rules on per-domain basis
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            /*if ("cls-stream.com".equals(request.getUrl().getHost())) {
                // Let WebView load pages on this website
                return false;
            }
            // Do NOT let WebView follow links to all other sites
            return true;*/
            return false;
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
        WebView myWebView = new WebView(MainActivity.this);
        WebSettings webSettings = myWebView.getSettings();
        // Enable JavaScript for this WebView
        webSettings.setJavaScriptEnabled(true);
        // To alter some WebView settings
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
        // Load site
        myWebView.loadUrl("https://cls-stream.com");
        // URLs useful for testing
        //myWebView.loadUrl("https://html5test.com/");
        //myWebView.loadUrl("https://webcamtests.com/");
        //myWebView.loadUrl("https://mictests.com/");
    }
}