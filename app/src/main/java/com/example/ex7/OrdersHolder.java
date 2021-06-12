package com.example.ex7;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class OrdersHolder {
    final int WAITING = 1;
    final int INPROGRESS = 2;
    final int READY = 3;
    final int DONE = 4;
    final int DELETED = 5;
    public Order myOrder;
    public final Context context;
    public SharedPreferences sp;
    public FirebaseApp app;
    public FirebaseFirestore db;
    public OrdersHolder(Context context) {
        this.context = context;
        this.sp = context.getSharedPreferences("local_db", Context.MODE_PRIVATE);
        initializeFromSp();
        app = FirebaseApp.initializeApp(this.context);
        db = FirebaseFirestore.getInstance();
        // todo: update myOrder from fireStore
        this.db.collection("orders").document(this.myOrder.orderId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    String customer_name = documentSnapshot.getString("customer name");
                    int pickles = Integer.parseInt(documentSnapshot.get("number of pickles").toString());
                    boolean hummus = Boolean.parseBoolean(documentSnapshot.get("hummus").toString());
                    boolean tahini = Boolean.parseBoolean(documentSnapshot.get("tahini").toString());
                    String comment = documentSnapshot.getString("comment");
                    int status = Integer.parseInt(Objects.requireNonNull(documentSnapshot.get("status")).toString());
                    myOrder = new Order(myOrder.orderId,customer_name,pickles,hummus,tahini,comment,status);
                } catch (Exception e) {
                    Log.d("1","problem with initiating holder from dataStore");
                }
            }
        });

    }
    private void initializeFromSp() {
        String orderId = this.sp.getString("orderId","noId");
        this.myOrder = new Order(orderId);
        }
    private void updateSp() {
        SharedPreferences.Editor editor = this.sp.edit();
        editor.putString("orderId",this.myOrder.orderId);
        editor.apply();
    }

    public void addNewOrder(String customer, int picklesNum, boolean isHummus, boolean isTahini, String comment)
    {

        this.myOrder.orderId = UUID.randomUUID().toString();;
        this.myOrder.customer_name = customer;
        this.myOrder.pickles = picklesNum;
        this.myOrder.hummus = isHummus;
        myOrder.tahini = isTahini;
        myOrder.comment = comment;
        myOrder.status = WAITING;

        Map<String, Object> order = new HashMap<>();
        order.put("orderId", this.myOrder.orderId);
        order.put("customer name", this.myOrder.customer_name);
        order.put("number of pickles", this.myOrder.pickles);
        order.put("hummus", this.myOrder.hummus);
        order.put("tahini", this.myOrder.tahini);
        order.put("comment", this.myOrder.comment);
        order.put("status", this.myOrder.status);

        updateSp();
// Add a new document with a generated ID
        db.collection("orders").document(this.myOrder.orderId).set(order);

    }



    public void editOrder(String id, String customer, int picklesNum, boolean isHummus, boolean isTahini, String comment, int status)
    {
        this.myOrder.orderId = id;
        this.myOrder.status = status;
        this.myOrder.customer_name = customer;
        this.myOrder.pickles = picklesNum;
        this.myOrder.hummus = isHummus;
        this.myOrder.tahini = isTahini;
        this.myOrder.comment = comment;

        Map<String, Object> order = new HashMap<>();
        order.put("orderId", this.myOrder.orderId);
        order.put("customer name", this.myOrder.customer_name);
        order.put("number of pickles", this.myOrder.pickles);
        order.put("hummus", this.myOrder.hummus);
        order.put("tahini", this.myOrder.tahini);
        order.put("comment", this.myOrder.comment);
        order.put("status", this.myOrder.status);

        updateSp();

        db.collection("orders").document(myOrder.orderId).set(order);


    }

    public void updateStatus(int status)
    {

        this.myOrder.status = status;

        updateSp();

        db.collection("orders").document(myOrder.orderId).update("status",status);

    }
    public void setOrder(Order newOrder)
    {
        this.myOrder = newOrder;
        updateSp();

    }

    public void deleteOrder(String id)
    {
        this.myOrder.status = DELETED;

        updateSp();

        db.collection("orders").document(id).delete();

    }
    public Order getCurrentOrder()
    {
        return this.myOrder;
    }
}

