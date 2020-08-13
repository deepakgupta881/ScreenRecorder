package com.example.recordingservice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

@BroadcastReceiverActions({"android.intent.action.SCREEN_ON", "android.intent.action.SCREEN_OFF"})
public class GlobalReceiver extends BroadcastReceiver {


    public interface MyGlobalCallbacks {
        void getAllCallBacks(Context context, Intent intent);
    }

    public MyGlobalCallbacks listener;


    public void setListener(MyGlobalCallbacks mListener) {
        listener = mListener;
    }

    public GlobalReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Session.getGlobalReceiverCallBack(context, intent);
        if (listener != null) {
            listener.getAllCallBacks(context, intent);
        }
    }


}