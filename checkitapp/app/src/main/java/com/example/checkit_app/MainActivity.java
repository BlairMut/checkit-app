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
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);


//        //------------------------intent test
//        Intent intent = getIntent();
//        String str=intent.getStringExtra("triggerValue");
//        text.setText(str);

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
                //NOT IMPLEMENTED DUE TO LACK OF CODE AVAILABILITY
//            case R.id.basecampLocation:
//                Intent intentBasecamp = new Intent(MainActivity.this,BaseCampActivity.class);
//                startActivity(intentBasecamp);
//                break;
            case R.id.distanceTrigger:
                Intent intentTrigger = new Intent(MainActivity.this,TriggerActivity.class);
                startActivity(intentTrigger);
                break;
            case R.id.help:
                Intent intentHelp = new Intent(MainActivity.this,HelpActivity.class);
                startActivity(intentHelp);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}