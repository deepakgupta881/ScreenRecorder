package com.example.recordingservice;

import android.content.Context;
import android.content.Intent;

public class Session {
    public static GlobalReceiverCallBack mGlobalReceiverCallback;
    public static RecordingRestartNewFileCallBack mRecordingRestartNewFileCallBack;
    public static void setRecordingRestartNewFileCallBackCallback(RecordingRestartNewFileCallBack listener) {
        if (listener != null) {
            mRecordingRestartNewFileCallBack = listener;
        }
    }

    public static void getRecordingRestartNewFileCallBackCallback() {
        if (mRecordingRestartNewFileCallBack != null) {
            mRecordingRestartNewFileCallBack.onCallRecordingRestart();
        }
    }

    public static void setmGlobalReceiverCallback(GlobalReceiverCallBack listener) {
        if (listener != null) {
            mGlobalReceiverCallback = listener;
        }
    }

    public static void getGlobalReceiverCallBack(Context context, Intent intent) {
        if (mGlobalReceiverCallback != null) {
            mGlobalReceiverCallback.onCallBackReceived(context, intent);
        }
    }



}
