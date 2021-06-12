package com.example.ex7;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public OrdersHolder holder = null;
    final int WAITING = 1;
    final int INPROGRESS = 2;
    final int READY = 3;
    final int DONE = 4;
    final int DELETED = 5;
    private int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (holder == null) {
            holder = OrdersApp.getInstance().getDataBase();
        }
        String id = holder.myOrder.orderId;
        //todo: set listener to the fireStore, so i can get the current status of the order
        holder.db.collection("orders").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                try {
                    status = Integer.parseInt(Objects.requireNonNull(documentSnapshot.get("status")).toString());
                } catch (Exception e) {
                    status = DELETED;
                }

                if (status == DONE || status == DELETED) {
                    //  if status is DONE or not exists or Deleted than go to new_order_screen and etc..
                    Intent newOrderIntent = new Intent(MainActivity.this, NewOrderScreen.class);
                    startActivity(newOrderIntent);
                    finish();
                } else if (status == WAITING) {
                    // if status is waiting than go to EditOrderScreen
                    Intent EditOrderIntent = new Intent(MainActivity.this, EditOrderScreen.class);
                    startActivity(EditOrderIntent);
                    finish();
                } else if (status == INPROGRESS) {

                    //if status is In Progress than go to OrderInMakingScreen
                    Intent orderInProgressIntent = new Intent(MainActivity.this, OrderInProgressScreen.class);
                    startActivity(orderInProgressIntent);
                    finish();
                } else if (status == READY) {
                    // if status is READY than go to OrderIsReadyScreen
                    Intent orderIsReadyIntent = new Intent(MainActivity.this, OrderIsReadyScreen.class);
                    startActivity(orderIsReadyIntent);
                    finish();
                }


            }
        });
    }
}

