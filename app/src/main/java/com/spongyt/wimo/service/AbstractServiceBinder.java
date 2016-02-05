package com.spongyt.wimo.service;

import android.os.Binder;

/**
 * Created by tmichelc on 04.02.2016.
 */
public abstract class AbstractServiceBinder <T> extends Binder {

    public abstract T getService();
}
