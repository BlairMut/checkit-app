package com.example.checkit_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
    //Strin

    FirebaseAuth auth;
    DatabaseReference reference;

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
        registerForContextMenu(lv);
        OnBtnClick();

        //selectItem
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                Intent intent = new Intent(MainActivity.this, update.class);
                intent.putExtra("Update", item);
                startActivity(intent);

            }

        });

    }

    public void OnBtnClick(){
        //item = getIntent().getStringExtra("Value");
        String Uid = auth.getUid().toString();
        reference = FirebaseDatabase.getInstance().getReference().child(Uid).child("User Items");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    arrayList.add(dataSnapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        menu.setHeaderTitle("Option");


    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.updateAction){
            Toast.makeText(this, "update selected", Toast.LENGTH_SHORT).show();

        }else if(item.getItemId() == R.id.deleteAction){
            Toast.makeText(this, "delete selected", Toast.LENGTH_SHORT).show();

        }else {
            return false;
        }
            return true;
    }
}