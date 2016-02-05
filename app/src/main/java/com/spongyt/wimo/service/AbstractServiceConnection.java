package com.spongyt.wimo.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import timber.log.Timber;

/**
 * Created by tmichelc on 04.02.2016.
 */
public abstract class AbstractServiceConnection<T> implements ServiceConnection {

    private T service;

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {

        AbstractServiceBinder<T> serviceBinder;
        try{
            serviceBinder = (AbstractServiceBinder<T>) binder;
        }catch (ClassCastException e){
            throw new IllegalStateException(String.format("Service binder '%s' does not implement '%s.class'", binder.getClass().getName(), AbstractServiceBinder.class.getName()), e);
        }


        this.service = serviceBinder.getService();

        Timber.d("Service %s is connected", binder.getClass().getSimpleName());

        onServiceConnected(this.service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        // mostly do nothing
        Timber.e("Service %s unexpectedly disconnected", service.getClass().getSimpleName());
    }

    public abstract void onServiceConnected(T service);
}
