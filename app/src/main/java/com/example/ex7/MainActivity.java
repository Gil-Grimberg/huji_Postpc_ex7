package com.example.ex7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public OrdersHolder holder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (holder == null) {
            holder = OrdersApp.getInstance().getDataBase();
        }
        // todo: if status is DONE or not exists than go to new_order_screen and etc..
        Intent newOrderIntent = new Intent(MainActivity.this, NewOrderScreen.class);
        startActivity(newOrderIntent);
        // todo: if status is waiting than go to EditOrderScreen

        //todo: if status is In Progress than go to OrderInMakingScreen
        Intent orderInProgressIntent = new Intent(MainActivity.this, OrderInProgressScreen.class);
        startActivity(orderInProgressIntent);
        //todo: if status is Ready than go to OrderIsReadyScreen

//        setContentView(R.layout.new_order_screen);

    }

}


