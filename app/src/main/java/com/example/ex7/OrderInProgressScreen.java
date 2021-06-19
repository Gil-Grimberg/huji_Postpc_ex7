package com.example.ex7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class OrderInProgressScreen extends AppCompatActivity {

    public OrdersHolder holder = null;
    final String WAITING = "waiting";
    final String INPROGRESS = "in-progress";
    final String READY = "ready";
    final String DONE = "done";
    private String status;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
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
                    status = Objects.requireNonNull(documentSnapshot.get("status")).toString();
                } catch (Exception e) {
                    status = DONE;
                }

                if (status.equals(DONE)) {
                    //  if status is DONE or not exists or Deleted than go to new_order_screen and etc..
                    Intent newOrderIntent = new Intent(OrderInProgressScreen.this, NewOrderScreen.class);
                    startActivity(newOrderIntent);
                    finish();
                }
                else if (status.equals(WAITING)) {
                    // if status is waiting than go to EditOrderScreen
                    Intent EditOrderIntent = new Intent(OrderInProgressScreen.this, EditOrderScreen.class);
                    startActivity(EditOrderIntent);
                    finish();
                }
                else if (status.equals(READY)) {
                    // if status is READY than go to OrderIsReadyScreen
                    Intent orderIsReadyIntent = new Intent(OrderInProgressScreen.this, OrderIsReadyScreen.class);
                    startActivity(orderIsReadyIntent);
                    finish();
                }

            }
        });

        setContentView(R.layout.order_in_making_screen);
        TextView userName = findViewById(R.id.userName);
        TextView pickles = findViewById(R.id.ordered_pickles);
        TextView hummus = findViewById(R.id.ordered_hummus);
        TextView tahini = findViewById(R.id.ordered_tahini);
        TextView comments = findViewById(R.id.ordered_comment);


        Order currentOrder = holder.getCurrentOrder();
        String name = currentOrder.customer_name;
        String numberPickles = String.valueOf(currentOrder.pickles);
        boolean hummusBool = currentOrder.hummus;
        boolean tahiniBool = currentOrder.tahini;
        String notes = currentOrder.comment;

        // set text views

        userName.setText(name);
        pickles.setText(numberPickles);
        if (hummusBool)
            hummus.setText("Yes");
        else
            hummus.setText("No");
        if (tahiniBool)
            tahini.setText("Yes");
        else
            tahini.setText("No");
        comments.setText(notes);

        holder.db.collection("orders").document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException e)
                    {
                        if (e != null || value == null)
                        {
                            Log.d("1","something wrong...");
                        }
                        else
                        {
                            try {
                                status = Objects.requireNonNull(value.get("status")).toString();
                            } catch (Exception err) {
                                status = DONE;
                            }
                            if (status.equals(DONE)) {
                                //  if status is DONE or not exists or Deleted than go to new_order_screen and etc..
                                Intent newOrderIntent = new Intent(OrderInProgressScreen.this, NewOrderScreen.class);
                                startActivity(newOrderIntent);
                                finish();
                            }  else if (status.equals(WAITING)) {
                                // if status is waiting than go to EditOrderScreen
                                Intent EditOrderIntent = new Intent(OrderInProgressScreen.this, EditOrderScreen.class);
                                startActivity(EditOrderIntent);
                                finish();
                            } else if (status.equals(READY)) {
                                // if status is READY than go to OrderIsReadyScreen
                                Intent orderIsReadyIntent = new Intent(OrderInProgressScreen.this, OrderIsReadyScreen.class);
                                startActivity(orderIsReadyIntent);
                                finish();
                            }
                        }
                    }
                });

    }
}
