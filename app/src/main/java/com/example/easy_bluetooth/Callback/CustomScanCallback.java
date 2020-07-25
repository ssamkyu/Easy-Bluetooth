package com.example.easy_bluetooth.Callback;

import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.easy_bluetooth.Listener.ScanResultListner;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class CustomScanCallback extends ScanCallback {

    private ScanResultListner resultListner;

    public CustomScanCallback(ScanResultListner resultListner) {
    }

    @Override
    public void onScanResult(int callbackType, ScanResult result) {
        super.onScanResult(callbackType, result);
        resultListner.onScanCallback(result);
    }
}
