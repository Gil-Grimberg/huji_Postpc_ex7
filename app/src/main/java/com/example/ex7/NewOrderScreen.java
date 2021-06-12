package com.example.ex7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class NewOrderScreen extends AppCompatActivity {
    public OrdersHolder holder = null;
    final int WAITING = 1;
    final int INPROGRESS = 2;
    final int READY = 3;
    final int DONE = 4;
    final int DELETED = 5;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
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

        if (status==WAITING) {
            // if status is waiting than go to EditOrderScreen
//            Intent EditOrderIntent = new Intent(EditOrderScreen.this, EditOrderScreen.class);
//            startActivity(EditOrderIntent);
        }
        else if (status==INPROGRESS) {

            //if status is In Progress than go to OrderInMakingScreen
            Intent orderInProgressIntent = new Intent(NewOrderScreen.this, OrderInProgressScreen.class);
            startActivity(orderInProgressIntent);
        }
        else if(status==READY)
        {
            // if status is READY than go to OrderIsReadyScreen
            Intent orderIsReadyIntent = new Intent(NewOrderScreen.this,OrderIsReadyScreen.class);
            startActivity(orderIsReadyIntent);
        }


        setContentView(R.layout.new_order_screen);
        TextView inputName = findViewById(R.id.enter_name);
        EditText pickles = findViewById(R.id.enter_pickles);
        Switch isHummus = findViewById(R.id.hummus_switch);
        Switch isTahini = findViewById(R.id.tahini_switch);
        TextView comments = findViewById(R.id.enter_comment);
        FloatingActionButton send = findViewById(R.id.send_button);

        isHummus.setOnClickListener(v->{
            if (isHummus.isChecked())
            {
                isHummus.setText("Yes please!");
            }
            else
            {
                isHummus.setText("not a fan");
            }
        });

        isTahini.setOnClickListener(v->{
            if (isTahini.isChecked())
            {
                isTahini.setText("Yes please!");

            }
            else
            {
                isTahini.setText("not a fan");
            }
        });

        send.setOnClickListener(v->{
            String name = inputName.getText().toString();
            String picklesNumber = pickles.getText().toString();
            boolean hummus = isHummus.isChecked();
            boolean tahini = isTahini.isChecked();
            String comment = comments.getText().toString();
            holder.addNewOrder(name,picklesNumber,hummus,tahini,comment);
            // todo: start editOrder activity
            Intent editOrderIntent = new Intent(NewOrderScreen.this, EditOrderScreen.class);
            startActivity(editOrderIntent);
        });

        holder.ordersLiveDataPublic.observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {

            }
        });
    }

}
