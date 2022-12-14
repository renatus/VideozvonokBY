package by.bpk.videozvonok;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import android.content.Intent;
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
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            /*if ("cls-stream.com".equals(request.getUrl().getHost())) {
                // This is my website, so do not override; let my WebView load the page
                return false;
            }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, request.getUrl());
            startActivity(intent);
            return true;*/
            return false;
        }
    }


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


    // Function to check and request permission.
    /*public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }*/


    // Defining Permission codes
    // Values are arbitrary and unique for each permission or for each permission group
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int RECORD_AUDIO_PERMISSION_CODE = 120;
    private static final int MODIFY_AUDIO_SETTINGS_PERMISSION_CODE = 121;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        //checkPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE);
        //checkPermission(Manifest.permission.RECORD_AUDIO, RECORD_AUDIO_PERMISSION_CODE);
        //checkPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS, MODIFY_AUDIO_SETTINGS_PERMISSION_CODE);

        // The request code used in ActivityCompat.requestPermissions()
        // and returned in the Activity's onRequestPermissionsResult()
        int PERMISSION_ALL = 1;
        String[] PERMISSIONS = {
                //android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.RECORD_AUDIO,
                //android.Manifest.permission.MODIFY_AUDIO_SETTINGS,
                android.Manifest.permission.CAMERA
        };

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        // Hide top bar with app name
        getSupportActionBar().hide();
        WebView myWebView = new WebView(MainActivity.this);
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        myWebView.setWebViewClient(new MyWebViewClient());
        myWebView.setWebChromeClient(new WebChromeClient() {
            // Request permissions inside WebView
            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                request.grant(request.getResources());
            }
        });
        setContentView(myWebView);
        myWebView.loadUrl("https://cls-stream.com");
        //myWebView.loadUrl("https://html5test.com/");
        //myWebView.loadUrl("https://webcamtests.com/");
        //myWebView.loadUrl("https://mictests.com/");
    }
}