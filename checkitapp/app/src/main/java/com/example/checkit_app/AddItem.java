package com.example.checkit_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddItem extends AppCompatActivity {
    EditText addItem;
    Button addBtn;
    String item;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        addItem = findViewById(R.id.addItem);
        addBtn = findViewById(R.id.addBtn);

        OnBtnClick();

    }

    //adding data to firebase database
    public void OnBtnClick(){
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = addItem.getText().toString();
                String Uid = auth.getUid().toString();

                FirebaseDatabase.getInstance().getReference().child(Uid).child("User Items").push().setValue(result);
                Intent intent = new Intent(AddItem.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
    }
}