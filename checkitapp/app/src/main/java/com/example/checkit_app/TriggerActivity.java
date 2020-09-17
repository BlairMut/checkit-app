package com.example.checkit_app;

import androidx.appcompat.app.AppCompatActivity;

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

        triggerValue=findViewById(R.id.distanceTriggerValue);
        triggerButton = findViewById(R.id.distanceTriggerButton);

//        triggerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String str = triggerValue.getText().toString();
//                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//                intent.putExtra("triggerValue",str);
//
//                startActivity(intent);
//
//            }
//        });


    }

}