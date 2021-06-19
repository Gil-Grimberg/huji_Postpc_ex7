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

import androidx.annotation.NonNull;
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

public class NewOrderScreen extends AppCompatActivity {
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

                if (status.equals(READY)) {
                    // if status is READY than go to OrderIsReadyScreen
                    Intent orderIsReadyIntent = new Intent(NewOrderScreen.this, OrderIsReadyScreen.class);
                    startActivity(orderIsReadyIntent);
                    finish();
                } else if (status.equals(WAITING)) {
                    // if status is waiting than go to EditOrderScreen
                    Intent EditOrderIntent = new Intent(NewOrderScreen.this, EditOrderScreen.class);
                    startActivity(EditOrderIntent);
                    finish();
                } else if (status.equals(INPROGRESS)) {

                    //if status is In Progress than go to OrderInMakingScreen
                    Intent orderInProgressIntent = new Intent(NewOrderScreen.this, OrderInProgressScreen.class);
                    startActivity(orderInProgressIntent);
                    finish();

                }
            }
        });

        setContentView(R.layout.new_order_screen);
        TextView inputName = findViewById(R.id.enter_name);
        EditText pickles = findViewById(R.id.enter_pickles);
        Switch isHummus = findViewById(R.id.hummus_switch);
        Switch isTahini = findViewById(R.id.tahini_switch);
        TextView comments = findViewById(R.id.enter_comment);
        FloatingActionButton send = findViewById(R.id.send_button);

        isHummus.setOnClickListener(v -> {
            if (isHummus.isChecked()) {
                isHummus.setText("Yes please!");
            } else {
                isHummus.setText("not a fan");
            }
        });

        isTahini.setOnClickListener(v -> {
            if (isTahini.isChecked()) {
                isTahini.setText("Yes please!");

            } else {
                isTahini.setText("not a fan");
            }
        });

        send.setOnClickListener(v -> {
            String name = inputName.getText().toString();
            int picklesNumber;
            try {
                picklesNumber = Integer.parseInt(pickles.getText().toString());
                if (picklesNumber < 0 || picklesNumber > 10) {
                    Toast.makeText(getApplicationContext(), "you cam only choose 0-10 pickles!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), "only numbers between 0-10 are allow!", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean hummus = isHummus.isChecked();
            boolean tahini = isTahini.isChecked();
            String comment = comments.getText().toString();
            holder.addNewOrder(name, picklesNumber, hummus, tahini, comment);
            // todo: start editOrder activity
            Intent editOrderIntent = new Intent(NewOrderScreen.this, EditOrderScreen.class);
            startActivity(editOrderIntent);
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
                                status = Objects.requireNonNull(value.get("status")).toString();
                            } catch (Exception err) {
                                status = DONE;
                            }
                            if (status.equals(READY)) {
                                // if status is READY than go to OrderIsReadyScreen
                                Intent orderIsReadyIntent = new Intent(NewOrderScreen.this, OrderIsReadyScreen.class);
                                startActivity(orderIsReadyIntent);
                                finish();
                            } else if (status.equals(WAITING)) {
                                // if status is waiting than go to EditOrderScreen
                                Intent EditOrderIntent = new Intent(NewOrderScreen.this, EditOrderScreen.class);
                                startActivity(EditOrderIntent);
                                finish();
                            } else if (status.equals(INPROGRESS)) {

                                //if status is In Progress than go to OrderInMakingScreen
                                Intent orderInProgressIntent = new Intent(NewOrderScreen.this, OrderInProgressScreen.class);
                                startActivity(orderInProgressIntent);
                                finish();

                            }
                        }
                    }
                });

    }

}
