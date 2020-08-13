package com.example.recordingservice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import static android.app.Activity.RESULT_CANCELED;

public class Recorder {

    Context context;
    static Recorder rec;

    public static Recorder getInstance(Context context) {
        if (rec == null) {
            rec = new Recorder(context);
        }
        return rec;
    }

    public Recorder(Context ctx) {
        context = ctx;
    }

    public void onResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED && requestCode == Const.SCREEN_RECORD_REQUEST_CODE) {
            Toast.makeText(context,
                    context.getString(R.string.screen_recording_permission_denied), Toast.LENGTH_SHORT).show();
            //Return to home screen if the app was started from app shortcut
            if (((Activity) context).getIntent().getAction().equals(context.getString(R.string.app_shortcut_action)))
                ((Activity) context).finish();
            return;
        }
        /*If code reaches this point, congratulations! The user has granted screen mirroring permission
         * Let us set the recorderservice intent with relevant data and start service*/
        Intent recorderService = new Intent(((Activity) context), RecorderService.class);
        recorderService.setAction(Const.SCREEN_RECORDING_START);
        recorderService.putExtra(Const.RECORDER_INTENT_DATA, data);
        recorderService.putExtra(Const.RECORDER_INTENT_RESULT, resultCode);
        ((Activity) context).startService(recorderService);

    }


}