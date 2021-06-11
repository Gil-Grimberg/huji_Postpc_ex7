package com.example.ex7;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class OrderInProgressScreen extends AppCompatActivity {

    public OrdersHolder holder = null;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (holder == null) {
            holder = OrdersApp.getInstance().getDataBase();
        }

        TextView userName = findViewById(R.id.userName);
        TextView pickles = findViewById(R.id.ordered_pickles);
        TextView hummus = findViewById(R.id.ordered_hummus);
        TextView tahini = findViewById(R.id.ordered_tahini);
        TextView comments = findViewById(R.id.ordered_comment);

    }
}
