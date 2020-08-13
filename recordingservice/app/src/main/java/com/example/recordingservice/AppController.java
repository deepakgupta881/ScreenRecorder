package com.example.recordingservice;

import android.app.Application;
import android.content.BroadcastReceiver;

import com.example.recordingservice.receiver.DynamicReceiver;

public class AppController extends Application {
    public BroadcastReceiver receiver;
    GlobalReceiver globalReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        globalReceiver = new GlobalReceiver();
        receiver = DynamicReceiver.with(globalReceiver).register(getApplicationContext());


    }
}
