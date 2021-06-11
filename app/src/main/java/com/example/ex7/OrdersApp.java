package com.example.ex7;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class OrdersApp extends Application {

    private OrdersHolder dataBase;  //todo: change to ordersHolder

    public OrdersHolder getDataBase(){
        return dataBase;
    }
    public static OrdersApp instance = null;

    public static OrdersApp getInstance(){
        return instance;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dataBase = new OrdersHolder(this);
    }
}