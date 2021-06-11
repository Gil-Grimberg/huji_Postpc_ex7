package com.example.ex7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class EditOrderScreen extends AppCompatActivity {
    public OrdersHolder holder = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (holder == null) {
            holder = OrdersApp.getInstance().getDataBase();
        }

        setContentView(R.layout.edit_order_screen);
        TextView inputName = findViewById(R.id.update_name);
        NumberPicker pickles = findViewById(R.id.update_pickles);
        pickles.setMinValue(0);
        pickles.setMaxValue(10);
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
        pickles.setValue(picklesNumber);
        isHummus.setChecked(hummus);
        isTahini.setChecked(tahini);
        comments.setText(comment);

        updateButton.setOnClickListener(v -> {
            // todo: update using holder.
            // make sure sp is updated and also db
            // make sure order status is "waiting" before updating! otherwise dont update!!!
            String userName = inputName.getText().toString();
            int picklesNum = pickles.getValue();
            boolean ishummus = isHummus.isChecked();
            boolean istahini = isTahini.isChecked();
            String note = comments.getText().toString();
            holder.editOrder(userName, picklesNum, ishummus, istahini, note);

        });

        deleteButton.setOnClickListener(v->{
            String id = holder.getCurrentOrder().orderId;
            holder.deleteOrder(id);
            // todo: navigate to New Order Screen
            // make sure order status is "waiting" before deleting! otherwise dont delete!!!
            Intent newOrderIntent = new Intent(EditOrderScreen.this, NewOrderScreen.class);
            startActivity(newOrderIntent);
        });
    }
}
