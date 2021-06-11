package com.example.ex7;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class OrdersApp extends Application {

    private OrdersHolder localDataBase;  //todo: change to ordersHolder

    public OrdersHolder getDataBase(){
        return localDataBase;
    }
    public static OrdersApp instance = null;

    public static OrdersApp getInstance(){
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        localDataBase = new OrdersHolder(this);
    }
}