package com.example.imapp.activities;

import android.util.Log;

import com.king.zxing.CaptureActivity;

public class ZXingScanActivity extends CaptureActivity {
    @Override
    public boolean onResultCallback(String result) {
        Log.d("onActivityResult", "onResultCallback: "+result);
        return super.onResultCallback(result);
    }
}
