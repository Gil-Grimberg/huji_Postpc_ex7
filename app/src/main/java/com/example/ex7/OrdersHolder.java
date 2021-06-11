package com.example.ex7;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Set;

public class OrdersHolder {
    final int WAITING = 1;
    final int INPROGRESS = 2;
    final int READY = 3;
    final int DONE = 4;
    private Order myOrder;
    private final Context context;
    private SharedPreferences sp;
//    private final MutableLiveData<ArrayList<TodoItem>> toDoItemsLiveDataMutable = new MutableLiveData<>();
//    public final LiveData<ArrayList<TodoItem>> toDoItemsLiveDataPublic = toDoItemsLiveDataMutable;

    public OrdersHolder(Context context) {
        this.context = context;
        this.sp = context.getSharedPreferences("local_db", Context.MODE_PRIVATE);
        initializeFromSp();
    }
    private void initializeFromSp() {
        String orderId = sp.getString("orderID","noId");
        String customer_name = sp.getString("customerName","noCustomer");
        int pickles = sp.getInt("pickles",-1);
        boolean hummus = sp.getBoolean("hummus",false);
        boolean tahini  = sp.getBoolean("tahini",false);
        String comment = sp.getString("comment","noComment");
        int status = sp.getInt("status",0);
        myOrder = new Order(orderId,customer_name,pickles,hummus,tahini,comment,status);

        }

}

