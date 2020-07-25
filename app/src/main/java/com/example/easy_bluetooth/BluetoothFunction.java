package com.example.easy_bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.easy_bluetooth.Callback.CustomScanCallback;
import com.example.easy_bluetooth.Listener.BluetoothDeviceListner;
import com.example.easy_bluetooth.Listener.IntListner;
import com.example.easy_bluetooth.Listener.ScanResultListner;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BluetoothFunction {

    private Context context;
    private Activity activity;
    private BluetoothAdapter adapter;
    private BluetoothStateChangedReceiver bluetoothStateChangedReceiver;
    private BluetoothDiscoverReceiver bluetoothDiscoverReceiver;
    private BluetoothDeviceReceiver bluetoothDeviceReceiver;
    private final String TAG = "BluetoothOnOff";

    private List<ScanFilter> scanFilters = new Vector<>();
    private ScanSettings.Builder mScanSettings;
    private ScanSettings scanSettings;

    private BluetoothLeScanner mBluetoothLeScanner;
    private BluetoothManager bluetoothManager;

    private ArrayList<BluetoothDevice> devices = new ArrayList<>();

    public BluetoothFunction() {

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BluetoothFunction(Activity activity, Context context, BluetoothAdapter adapter) {
        this.context = context;
        this.adapter = adapter;
        this.activity = activity;

        this.mScanSettings = new ScanSettings.Builder();
        mScanSettings.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        scanSettings = mScanSettings.build();


    }





    public void bluetoothOnOff(){

        if(bluetoothStateChangedReceiver == null){
            bluetoothStateChangedReceiver = new BluetoothStateChangedReceiver(adapter, new IntListner() {
                @Override
                public void onScanCallback(int status) {

                    switch (status){

                        case BluetoothAdapter.STATE_OFF:

                            Log.e(TAG, "bluetoothAdapter.STATE_OFF");

                            break;

                        case BluetoothAdapter.STATE_TURNING_OFF:

                            Log.e(TAG, "bluetoothAdapter.STATE_Turning_off");

                            break;

                        case BluetoothAdapter.STATE_ON:

                            Log.e(TAG, "bluetoothAdapter.STATE_On");

                            break;

                        case BluetoothAdapter.STATE_TURNING_ON:

                            Log.e(TAG, "bluetoothAdapter.STATE_Turning On");

                            break;
                    }

                }
            });
        }

        if(!adapter.isEnabled()){

            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            context.registerReceiver(bluetoothStateChangedReceiver, BTIntent);

        } else {

            adapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            context.registerReceiver(bluetoothStateChangedReceiver, BTIntent);

        }

    }

    public void bluetoothOnOff(final IntListner listner){

        if(bluetoothStateChangedReceiver == null){
            bluetoothStateChangedReceiver = new BluetoothStateChangedReceiver(adapter, new IntListner() {
                @Override
                public void onScanCallback(int status) {

                    listner.onScanCallback(status);

                }
            });
        }

        if(!adapter.isEnabled()){

            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableBTIntent);

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            context.registerReceiver(bluetoothStateChangedReceiver, BTIntent);

        } else {

            adapter.disable();

            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            context.registerReceiver(bluetoothStateChangedReceiver, BTIntent);

        }

    }

    public void bluetoothDiscoverable(){
        Log.e(TAG , "btn Enable Dsiable discoverabe");

        if(bluetoothDiscoverReceiver == null){
            bluetoothDiscoverReceiver = new BluetoothDiscoverReceiver(adapter, new IntListner() {
                @Override
                public void onScanCallback(int status) {

                    switch (status){

                        case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:

                            Log.e(TAG, "bluetoothAdapter.SCAN_MODE_CONNECTAELE_DISCOVERABLE");

                            break;

                        case BluetoothAdapter.SCAN_MODE_CONNECTABLE:

                            Log.e(TAG, "bluetoothAdapter.SCAN_MODE_CONNECTABLE");

                            break;

                        case BluetoothAdapter.SCAN_MODE_NONE:

                            Log.e(TAG, "bluetoothAdapter.SCAN_MODE_NONE");

                            break;

                        case BluetoothAdapter.STATE_CONNECTING:

                            Log.e(TAG, "bluetoothAdapter.STATE_CONNECTING");

                            break;

                        case BluetoothAdapter.STATE_CONNECTED:

                            Log.e(TAG, "bluetoothAdapter.STATE_CONNECTED");

                            break;

                    }

                }
            });
        }

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        context.startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(adapter.ACTION_SCAN_MODE_CHANGED);
        context.registerReceiver(bluetoothDiscoverReceiver, intentFilter);

    }

    public void bluetoothDiscoverable(final IntListner listner){
        Log.e(TAG , "btn Enable Dsiable discoverabe");

        if(bluetoothDiscoverReceiver == null){
            bluetoothDiscoverReceiver = new BluetoothDiscoverReceiver(adapter, new IntListner() {
                @Override
                public void onScanCallback(int status) {

                    listner.onScanCallback(status);

                }
            });
        }

        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        context.startActivity(discoverableIntent);

        IntentFilter intentFilter = new IntentFilter(adapter.ACTION_SCAN_MODE_CHANGED);
        context.registerReceiver(bluetoothDiscoverReceiver, intentFilter);

    }

    public void bluetoothDiscover(final BluetoothDeviceListner bluetoothDeviceListner){

        if(bluetoothDeviceReceiver == null){
            bluetoothDeviceReceiver = new BluetoothDeviceReceiver(new BluetoothDeviceListner() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onBluetoothDevice(BluetoothDevice device) {
                    Log.e("device", device.toString());
                    bluetoothDeviceListner.onBluetoothDevice(device);
                }
            });
        }

        Log.e(TAG, "btn_discover");
        if(adapter.isDiscovering()){
            adapter.cancelDiscovery();
            Log.e(TAG, "btnDiscover:cancel discover");

            checkBTPermissions();

            adapter.startDiscovery();
            IntentFilter discoverDeviceIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver( bluetoothDeviceReceiver, discoverDeviceIntent);
        }

        if(!adapter.isDiscovering()){

            checkBTPermissions();

            adapter.startDiscovery();
            IntentFilter discoverDeviceIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            context.registerReceiver( bluetoothDeviceReceiver, discoverDeviceIntent);

        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public List<ScanFilter> makeScanFilter(ArrayList<BluetoothDevice> devices){

        List<ScanFilter> resultList = new ArrayList<>();

        for(BluetoothDevice device : devices){

            ScanFilter.Builder scanFilter = new ScanFilter.Builder();
            scanFilter.setDeviceAddress(device.getAddress()); //ex) 00:00:00:00:00:00
            ScanFilter scan = scanFilter.build();
            resultList.add(scan);

        }

        return resultList;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startScanWithAddress(ArrayList<BluetoothDevice> devices, final ScanResultListner listner){

        scanFilters = makeScanFilter(devices);

        if(mBluetoothLeScanner == null){
            mBluetoothLeScanner = adapter.getBluetoothLeScanner();
        }

        CustomScanCallback customScanCallback = new CustomScanCallback(new ScanResultListner() {
            @Override
            public void onScanCallback(ScanResult result) {
                listner.onScanCallback(result);
            }
        });

        mBluetoothLeScanner.startScan(scanFilters, scanSettings, customScanCallback);

    }

    public void unregisterBluetoothDiscover(){
        if(bluetoothDeviceReceiver.isInitialStickyBroadcast()){
            context.unregisterReceiver(bluetoothDeviceReceiver);
        }
    }

    private void checkBTPermissions(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){
            int permissionCheck = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck = context.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                permissionCheck += context.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");
            }
            if(permissionCheck != 0){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    activity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);
                }
            }
        } else {
            Log.e(TAG, "check permission");
        }
    }



}
