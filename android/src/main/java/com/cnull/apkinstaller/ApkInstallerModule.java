package com.cnull.apkinstaller;

import android.os.Build;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.net.Uri;

import java.io.File;

public class ApkInstallerModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext _context = null;

    public ApkInstallerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        _context = reactContext;
    }

    @Override
    public String getName() {
        return "ApkInstaller";
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        return constants;
    }

    @ReactMethod
    // public void show(String message, int duration) {
    public void test(String message) {
        Toast.makeText(getReactApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @ReactMethod
    public void install(String path) {
        String cmd = "chmod 777 " + path;
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        File file = new File(path);

        // android 7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(_context, "com.starup.fileProvider", file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        _context.startActivity(intent);
    }
}
