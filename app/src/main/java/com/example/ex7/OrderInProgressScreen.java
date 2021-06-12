package com.example.ex7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.List;

public class OrderInProgressScreen extends AppCompatActivity {

    public OrdersHolder holder = null;
    final int WAITING = 1;
    final int INPROGRESS = 2;
    final int READY = 3;
    final int DONE = 4;
    final int DELETED = 5;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
            Intent newOrderIntent = new Intent(OrderInProgressScreen.this, NewOrderScreen.class);
            startActivity(newOrderIntent);
        }
        else if (status==WAITING) {
            // if status is waiting than go to EditOrderScreen
            Intent EditOrderIntent = new Intent(OrderInProgressScreen.this, EditOrderScreen.class);
            startActivity(EditOrderIntent);
        }
        else if(status==READY)
        {
            // if status is READY than go to OrderIsReadyScreen
            Intent orderIsReadyIntent = new Intent(OrderInProgressScreen.this,OrderIsReadyScreen.class);
            startActivity(orderIsReadyIntent);
        }

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

    }
}
