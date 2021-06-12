package com.example.ex7;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class OrderIsReadyScreen extends AppCompatActivity {

    public OrdersHolder holder = null;
    final int WAITING = 1;
    final int INPROGRESS = 2;
    final int READY = 3;
    final int DONE = 4;
    final int DELETED = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (holder == null) {
            holder = OrdersApp.getInstance().getDataBase();
        }

        holder.db.collection("orders").addSnapshotListener(new EventListener<QuerySnapshot>()
        {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){ }
                else if  (value==null)
                {
                    // delete order
                    holder.deleteOrder(holder.getCurrentOrder().orderId);
                }
                else
                {
                    List<DocumentSnapshot> documents = value.getDocuments();
                    boolean isIdExists = false;
                    for (DocumentSnapshot document : documents)
                    {
                        Order order = document.toObject(Order.class);
                        if (holder.getCurrentOrder().orderId .equals(order.orderId))
                        {
                            holder.setOrder(order);
                            isIdExists = true;
                        }
                    }
                    if (!isIdExists)
                    {
                        holder.updateStatus(DELETED);
                    }

                }

            } });


        int status = holder.getCurrentOrder().status;

        if (status==DONE || status==DELETED) {
            //  if status is DONE or not exists or Deleted than go to new_order_screen and etc..
            Intent newOrderIntent = new Intent(OrderIsReadyScreen.this, NewOrderScreen.class);
            startActivity(newOrderIntent);
        }
        else if (status==WAITING) {
            // if status is waiting than go to EditOrderScreen
            Intent EditOrderIntent = new Intent(OrderIsReadyScreen.this, EditOrderScreen.class);
            startActivity(EditOrderIntent);
        }
        else if (status==INPROGRESS) {

            //if status is In Progress than go to OrderInMakingScreen
            Intent orderInProgressIntent = new Intent(OrderIsReadyScreen.this, OrderInProgressScreen.class);
            startActivity(orderInProgressIntent);
        }

        setContentView(R.layout.order_is_ready_screen);
        Button gotiT = findViewById(R.id.got_it);

        gotiT.setOnClickListener(v->
        {
            holder.updateStatus(DONE);
            Intent newOrderIntent = new Intent(OrderIsReadyScreen.this, NewOrderScreen.class);
            startActivity(newOrderIntent);
        });
    }
}
