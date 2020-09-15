package com.example.checkit_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TriggerActivity extends AppCompatActivity {

    EditText triggerValue;
    Button triggerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trigger);

        triggerValue=findViewById(R.id.distanceTriggerValue);
        triggerButton = findViewById(R.id.distanceTriggerButton);
    }
}