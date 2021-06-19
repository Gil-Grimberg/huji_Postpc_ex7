package com.example.ex7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Objects;

public class EditOrderScreen extends AppCompatActivity {
    public OrdersHolder holder = null;
    final String WAITING = "waiting";
    final String INPROGRESS = "in-progress";
    final String READY = "ready";
    final String DONE = "done";
    private String status;

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
                    Intent newOrderIntent = new Intent(EditOrderScreen.this, NewOrderScreen.class);
                    startActivity(newOrderIntent);
                    finish();
                }  else if (status.equals(INPROGRESS)) {

                    //if status is In Progress than go to OrderInMakingScreen
                    Intent orderInProgressIntent = new Intent(EditOrderScreen.this, OrderInProgressScreen.class);
                    startActivity(orderInProgressIntent);
                    finish();
                } else if (status.equals(READY)) {
                    // if status is READY than go to OrderIsReadyScreen
                    Intent orderIsReadyIntent = new Intent(EditOrderScreen.this, OrderIsReadyScreen.class);
                    startActivity(orderIsReadyIntent);
                    finish();
                }

            }
        });

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
        int picklesNumber = currentOrder.pickles;
        boolean hummus = currentOrder.hummus;
        boolean tahini = currentOrder.tahini;
        String comment = currentOrder.comment;

        inputName.setText(name);
        pickles.setText(String.valueOf(picklesNumber));
        isHummus.setChecked(hummus);
        isTahini.setChecked(tahini);
        comments.setText(comment);

        updateButton.setOnClickListener(v -> {
            // todo: update using holder.
            // make sure sp is updated and also db
            // make sure order status is "waiting" before updating! otherwise dont update!!!
            int picklesN;
            try {
                picklesN = Integer.parseInt(pickles.getText().toString());
                if (picklesN < 0 || picklesN > 10) {
                    Toast.makeText(getApplicationContext(), "you cam only choose 0-10 pickles!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "only numbers between 0-10 are allow!", Toast.LENGTH_SHORT).show();
                return;
            }
            String userName = inputName.getText().toString();
            boolean ishummus = isHummus.isChecked();
            boolean istahini = isTahini.isChecked();
            String note = comments.getText().toString();
            holder.editOrder(holder.getCurrentOrder().orderId,
                    userName, picklesN, ishummus, istahini, note,holder.WAITING);

        });

        deleteButton.setOnClickListener(v->{
            holder.deleteOrder(id);
            // todo: navigate to New Order Screen
            // make sure order status is "waiting" before deleting! otherwise dont delete!!!
            Intent newOrderIntent = new Intent(EditOrderScreen.this, NewOrderScreen.class);
            startActivity(newOrderIntent);
            finish();
        });

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
                                Intent newOrderIntent = new Intent(EditOrderScreen.this, NewOrderScreen.class);
                                startActivity(newOrderIntent);
                                finish();
                            }  else if (status.equals(INPROGRESS)) {

                                //if status is In Progress than go to OrderInMakingScreen
                                Intent orderInProgressIntent = new Intent(EditOrderScreen.this, OrderInProgressScreen.class);
                                startActivity(orderInProgressIntent);
                                finish();
                            } else if (status.equals(READY)) {
                                // if status is READY than go to OrderIsReadyScreen
                                Intent orderIsReadyIntent = new Intent(EditOrderScreen.this, OrderIsReadyScreen.class);
                                startActivity(orderIsReadyIntent);
                                finish();
                            }
                        }
                    }
                });

    }
}
