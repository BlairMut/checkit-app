package com.example.checkit_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    FloatingActionButton addBtn;

   ArrayList<String> arrayList;
    ArrayAdapter<String> adapter;
    ListView lv;
    String item;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        addBtn = findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItem.class);
                startActivity(intent);
                //item = getIntent().getExtras().getString("Values");

            }
        });

        lv = findViewById(R.id.listView);

        auth = FirebaseAuth.getInstance();

        arrayList = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.custom,R.id.listText, arrayList);
        lv.setAdapter(adapter);



        OnBtnClick();
    }

    public void OnBtnClick(){
        String Uid = auth.getUid().toString();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Uid).child("User Items");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    arrayList.add((String) dataSnapshot.getValue());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}