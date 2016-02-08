package com.spongyt.wimo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.spongyt.wimo.R;
import com.spongyt.wimo.service.OrderSyncService;

import timber.log.Timber;


public class OrderListActivity extends AppCompatActivity {

    private OrderSyncService networkService;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        checkPlayServices();

//        Intent intent= new Intent(this, NetworkService.class);
//        bindService(intent, mConnection,
//                Context.BIND_AUTO_CREATE);
    }
//
//    private ServiceConnection mConnection = new AbstractServiceConnection<NetworkService>() {
//        @Override
//        public void onServiceConnected(NetworkService service) {
//            networkService = service;
//        }
//    };


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Timber.i("This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
