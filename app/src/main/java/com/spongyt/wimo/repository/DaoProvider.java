package com.spongyt.wimo.repository;

import android.database.sqlite.SQLiteDatabase;

import com.spongyt.wimo.WimoApplication;

/**
 * Created by tmichelc on 04.02.2016.
 */
public class DaoProvider {

    private final static String DB_NAME = "wimo.db";


    public static OrderDao getOrderDao(){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(WimoApplication.getInstance(), DB_NAME, null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession.getOrderDao();
    }
}
