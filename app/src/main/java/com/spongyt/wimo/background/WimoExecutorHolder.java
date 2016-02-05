package com.spongyt.wimo.background;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by tmichelc on 04.02.2016.
 */
public class WimoExecutorHolder {

    private static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;

    public static ScheduledExecutorService sInstance;

    public static ScheduledExecutorService getExecutor(){
        if(sInstance == null){
            sInstance = Executors.newScheduledThreadPool(CORE_POOL_SIZE);
        }

        return sInstance;
    }


}
