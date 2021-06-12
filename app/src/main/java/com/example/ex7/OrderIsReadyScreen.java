package com.example.ex7;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class OrderIsReadyScreen extends AppCompatActivity {

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
                    Intent newOrderIntent = new Intent(OrderIsReadyScreen.this, NewOrderScreen.class);
                    startActivity(newOrderIntent);
                    finish();
                } else if (status == WAITING) {
                    // if status is waiting than go to EditOrderScreen
                    Intent EditOrderIntent = new Intent(OrderIsReadyScreen.this, EditOrderScreen.class);
                    startActivity(EditOrderIntent);
                    finish();
                } else if (status == INPROGRESS) {

                    //if status is In Progress than go to OrderInMakingScreen
                    Intent orderInProgressIntent = new Intent(OrderIsReadyScreen.this, OrderInProgressScreen.class);
                    startActivity(orderInProgressIntent);
                    finish();

                }
            }
        });

        setContentView(R.layout.order_is_ready_screen);
        Button gotiT = findViewById(R.id.got_it);

        gotiT.setOnClickListener(v ->
        {
            holder.updateStatus(DONE);
            Intent newOrderIntent = new Intent(OrderIsReadyScreen.this, NewOrderScreen.class);
            startActivity(newOrderIntent);
            finish();
        });

        holder.db.collection("orders").document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e) {
                        if (e != null || value == null) {
                            Log.d("1", "something wrong...");
                        } else {
                            try {
                                status = Integer.parseInt(Objects.requireNonNull(value.get("status")).toString());
                            } catch (Exception err) {
                                status = DELETED;
                            }
                            if (status == DONE || status == DELETED) {
                                //  if status is DONE or not exists or Deleted than go to new_order_screen and etc..
                                Intent newOrderIntent = new Intent(OrderIsReadyScreen.this, NewOrderScreen.class);
                                startActivity(newOrderIntent);
                                finish();
                            } else if (status == WAITING) {
                                // if status is waiting than go to EditOrderScreen
                                Intent EditOrderIntent = new Intent(OrderIsReadyScreen.this, EditOrderScreen.class);
                                startActivity(EditOrderIntent);
                                finish();
                            } else if (status == INPROGRESS) {

                                //if status is In Progress than go to OrderInMakingScreen
                                Intent orderInProgressIntent = new Intent(OrderIsReadyScreen.this, OrderInProgressScreen.class);
                                startActivity(orderInProgressIntent);
                                finish();

                            }
                        }
                    }
                });

    }
}
