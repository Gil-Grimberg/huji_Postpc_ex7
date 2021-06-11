package com.example.ex7;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NewOrderScreen extends AppCompatActivity {
    public OrdersHolder holder = null;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (holder == null) {
            holder = OrdersApp.getInstance().getDataBase();
        }

        setContentView(R.layout.new_order_screen);
        TextView inputName = findViewById(R.id.enter_name);
        NumberPicker pickles = findViewById(R.id.enter_pickles);
        pickles.setMinValue(0);
        pickles.setMaxValue(10);
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
            int picklesNumber = pickles.getValue();
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
