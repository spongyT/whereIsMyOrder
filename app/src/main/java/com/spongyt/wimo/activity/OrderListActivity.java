package com.spongyt.wimo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.spongyt.wimo.R;
import com.spongyt.wimo.service.OrderSyncService;


public class OrderListActivity extends AppCompatActivity {

    private OrderSyncService networkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

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
}
