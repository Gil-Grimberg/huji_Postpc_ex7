package com.example.ex7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    public OrdersHolder holder = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_order_screen);
        if (holder == null) {
            holder = OrdersApp.getInstance().getDataBase();
        }
    }

}


//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//    Map<String, Object> user = new HashMap<>();
//        user.put("first", "Alan");
//                user.put("middle", "Mathison");
//                user.put("last", "Turing");
//                user.put("born", 1912);
//
//// Add a new document with a generated ID
//final String TAG = "1";
//        db.collection("orders").add(user)
//        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//
//
//@Override
//public void onSuccess(DocumentReference documentReference) {
//        Log.d(TAG,"DocumentSnapshot added with ID: " + documentReference.getId());
//
//        }
//        }).addOnFailureListener(new OnFailureListener() {
//@Override
//public void onFailure(@NonNull Exception e) {
//        Log.w(TAG, "Error adding document", e);
//        }
//        });