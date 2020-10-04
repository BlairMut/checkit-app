package com.example.checkit_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
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
   ArrayList<String> userSelection;
    ListViewAdapter adapter;
    ListView lv;
    String item;
    String itemKey;
    CheckBox checkBox;

    //To access user info
    FirebaseAuth auth;
    DatabaseReference reference;
    DatabaseReference reference2;

    private static boolean isActionMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        //if(MainActivity.isActionMode){
        //    checkBox.setVisibility(View.VISIBLE);
        //}else if(MainActivity.isActionMode ){
        //   checkBox.setVisibility(View.GONE);
        //}
        //takes you to the add item activity
        addBtn = findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddItem.class);
                startActivity(intent);

            }
        });

        //display list of items from the realtime database
        lv = findViewById(R.id.listView);
        lv.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        lv.setMultiChoiceModeListener(modeListener);
        auth = FirebaseAuth.getInstance();

        arrayList = new ArrayList<String>();
        userSelection = new ArrayList<>();
        adapter = new ListViewAdapter(arrayList, this);
        lv.setAdapter(adapter);
        registerForContextMenu(lv);
        OnBtnClick();

        //checkBox = findViewById(R.id.checkBox);


        //selectItem for editing update/delete
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

    //reads list of items from the users database
    public void OnBtnClick(){
        //final String[] itemKey = new String[1];
        String Uid = auth.getUid().toString();
        reference = FirebaseDatabase.getInstance().getReference().child(Uid).child("User Items");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    itemKey = dataSnapshot.getKey();
                    arrayList.add(dataSnapshot.getValue().toString());
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //context menu
    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            if(userSelection.contains(arrayList.get(position))){
                userSelection.remove(arrayList.get(position));
            }
            else {
                userSelection.add(arrayList.get(position));
            }
            mode.setTitle(userSelection.size()+ " items selected...");
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu,menu);

            isActionMode = true;

            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.deleteAction:
                    adapter.removeItems(userSelection);
                    mode.finish();

                    //String Uid = auth.getUid().toString();
                    //reference2 = FirebaseDatabase.getInstance().getReference(Uid).child("User Items").child(itemKey);
                    //Toast.makeText(MainActivity.this, "id: " + itemKey, Toast.LENGTH_SHORT).show();
                    //reference2.removeValue();



                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isActionMode = false;
        }
    };


    //menu for editing
    /*@Override
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
    }*/
}