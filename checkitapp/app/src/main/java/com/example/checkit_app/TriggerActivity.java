package com.example.checkit_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.Serializable;

public class TriggerActivity extends AppCompatActivity {

    EditText triggerValue;
    Button triggerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        triggerValue=findViewById(R.id.distanceTriggerValue);
        triggerButton = findViewById(R.id.distanceTriggerButton);

        triggerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = triggerValue.getText().toString();
                int temp = Integer.parseInt(str);
//                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                Intent intent = new Intent(TriggerActivity.this,MapsActivity.class);
                intent.putExtra("triggerValue",temp);
                startActivity(intent);

            }
        });


    }

}