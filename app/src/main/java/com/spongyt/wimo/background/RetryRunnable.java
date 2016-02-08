package com.spongyt.wimo.background;

import timber.log.Timber;

/**
 * Created by tmichelc on 05.02.2016.
 */
public abstract class RetryRunnable implements Runnable{

    private int retries;
    private long delay;

    public RetryRunnable(int retries, long delay) {
        this.retries = retries;
        this.delay = delay;
    }

    @Override
    public final void run() {
        boolean success = false;
        try {
            tryOnce();
            success = true;
        } catch (Exception e) {
            Timber.d(e, "Retry failed");
        }

        while (!success && retries > 0){
            retries--;
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Timber.e(e, "Retry task was interrupted");
            }

            try {
                tryOnce();
                success = true;
            } catch (Exception e) {
                Timber.d(e, "Retry failed");
            }
        }
    }

    public abstract void tryOnce() throws Exception;

}
