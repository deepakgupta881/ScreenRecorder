package com.example.recordingservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements GlobalReceiver.MyGlobalCallbacks {
    Button btnnxt;
    private GlobalReceiver globalReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnnxt = findViewById(R.id.nxt);
        btnnxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SecondActivity.class));
            }
        });

    }

    @Override
    public void getAllCallBacks(Context context, Intent intent) {

    }
}