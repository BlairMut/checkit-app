package com.example.checkit_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionButton addBtn;
    ArrayList<String> arrayList;
    public static ArrayList<String> userSelection;
    ListViewAdapter adapter;
    ListView lv;
    String item;
    String itemKey;
    CheckBox checkBox;
    FirebaseAuth auth;
    DatabaseReference reference;
    DatabaseReference reference2;
    public static boolean isActionMode = false;
    private static final String TAG ="Test" ;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar tool;
    TextView text;
    ActionBarDrawerToggle toggle;
    public static ActionMode actionMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//------------------------------------------------------------------------------------------
//        Navigation drawer
//------------------------------------------------------------------------------------------
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        tool= findViewById(R.id.toolbar);
        text = findViewById(R.id.text);

        setSupportActionBar(tool);
        navigationView.bringToFront();
        toggle = new ActionBarDrawerToggle(this,drawerLayout,tool,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
//------------------------------------------------------------------------------------------
//        Hamburger Toolbar
//------------------------------------------------------------------------------------------
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        addBtn = findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(MainActivity.this, AddItem.class);
                                          startActivity(intent);

                                      }
                                  });
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
//        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                item = parent.getItemAtPosition(position).toString();
//                Intent intent = new Intent(MainActivity.this, update.class);
//                intent.putExtra("Update", item);
//                startActivity(intent);
//
//            }
//
//        });

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
            {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem)
    {
        switch(menuItem.getItemId())
        {
            case R.id.nav_home:
                break;
            case R.id.basecampLocation:
                Intent intentBasecamp = new Intent(MainActivity.this,MapsActivity.class);
                startActivity(intentBasecamp);
                break;
            case R.id.distanceTrigger:
                Intent intentTrigger = new Intent(MainActivity.this,TriggerActivity.class);
                startActivity(intentTrigger);
                break;
            case R.id.help:
                Intent intentHelp = new Intent(MainActivity.this,HelpActivity.class);
                startActivity(intentHelp);
                break;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, StartActivity.class));
                finish();
                return true;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void OnBtnClick(){
        String Uid = auth.getUid().toString();
        reference = FirebaseDatabase.getInstance().getReference().child(Uid).child("User Items");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    //itemKey = dataSnapshot.getKey();
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
//            if(userSelection.contains(arrayList.get(position))){
//                userSelection.remove(arrayList.get(position));
//            }
//            else {
//                userSelection.add(arrayList.get(position));
//            }
//            mode.setTitle(userSelection.size()+ " items selected...");
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu,menu);

            isActionMode = true;
            actionMode  =mode;

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
                    return true;
                default:
                    return false;
            }

        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            isActionMode = false;
            actionMode = null;
            userSelection.clear();
        }
    };

}


