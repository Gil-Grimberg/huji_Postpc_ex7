package com.example.ex7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class EditOrderScreen extends AppCompatActivity {
    public OrdersHolder holder = null;
    final int WAITING = 1;
    final int INPROGRESS = 2;
    final int READY = 3;
    final int DONE = 4;
    final int DELETED = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("1","www");
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
            Intent newOrderIntent = new Intent(EditOrderScreen.this, NewOrderScreen.class);
            startActivity(newOrderIntent);
        }

        else if (status==INPROGRESS) {

            //if status is In Progress than go to OrderInMakingScreen
            Intent orderInProgressIntent = new Intent(EditOrderScreen.this, OrderInProgressScreen.class);
            startActivity(orderInProgressIntent);
        }
        else if(status==READY)
        {
            // if status is READY than go to OrderIsReadyScreen
            Intent orderIsReadyIntent = new Intent(EditOrderScreen.this,OrderIsReadyScreen.class);
            startActivity(orderIsReadyIntent);
        }

        setContentView(R.layout.edit_order_screen);
        TextView inputName = findViewById(R.id.update_name);
        EditText pickles = findViewById(R.id.update_pickles);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch isHummus = findViewById(R.id.update_hummus_switch);
        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch isTahini = findViewById(R.id.update_tahini_switch);
        TextView comments = findViewById(R.id.update_enter_comment);
        FloatingActionButton updateButton = findViewById(R.id.update_button);
        FloatingActionButton deleteButton = findViewById(R.id.delete_button);

        Order currentOrder = holder.getCurrentOrder();
        String name = currentOrder.customer_name;
        String picklesNumber = currentOrder.pickles;
        boolean hummus = currentOrder.hummus;
        boolean tahini = currentOrder.tahini;
        String comment = currentOrder.comment;

        inputName.setText(name);
        pickles.setText(picklesNumber);
        isHummus.setChecked(hummus);
        isTahini.setChecked(tahini);
        comments.setText(comment);

        updateButton.setOnClickListener(v -> {
            // todo: update using holder.
            // make sure sp is updated and also db
            // make sure order status is "waiting" before updating! otherwise dont update!!!
            String userName = inputName.getText().toString();
            String picklesNum = pickles.getText().toString();
            boolean ishummus = isHummus.isChecked();
            boolean istahini = isTahini.isChecked();
            String note = comments.getText().toString();
            holder.editOrder(holder.getCurrentOrder().orderId,
                    userName, picklesNum, ishummus, istahini, note,holder.WAITING);

        });

        deleteButton.setOnClickListener(v->{
            String id = holder.getCurrentOrder().orderId;
            holder.deleteOrder(id);
            // todo: navigate to New Order Screen
            // make sure order status is "waiting" before deleting! otherwise dont delete!!!
            Intent newOrderIntent = new Intent(EditOrderScreen.this, NewOrderScreen.class);
            startActivity(newOrderIntent);
        });

        holder.ordersLiveDataPublic.observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {

            }
        });

    }
}
