package com.example.checkit_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class update extends AppCompatActivity {
    EditText editText;
    Button updateBtn;
    Button deleteBtn;
    FirebaseAuth auth;
    DatabaseReference reference;

    String upItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        editText = findViewById(R.id.editItem);
        updateBtn = findViewById(R.id.updateAction);
        deleteBtn = findViewById(R.id.deleteBtn);

        auth = FirebaseAuth.getInstance();

        upItem = getIntent().getStringExtra("Update");
        editText.setText(upItem);



        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Uid = auth.getUid().toString();
                //reference = FirebaseDatabase.getInstance().getReference(Uid).child("User Items");
                //reference.removeValue();
                Intent intent = new Intent(update.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}