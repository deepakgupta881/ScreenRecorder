package com.example.recordingservice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

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

/*
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        //The user has denied permission for screen mirroring. Let's notify the user
        super.onActivityResult(requestCode, resultCode, data);
//        recorder.onResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED && requestCode == Const.SCREEN_RECORD_REQUEST_CODE) {
            Toast.makeText(this,
                    getString(R.string.screen_recording_permission_denied), Toast.LENGTH_SHORT).show();
            //Return to home screen if the app was started from app shortcut
            if (getIntent().getAction().equals(getString(R.string.app_shortcut_action)))
                this.finish();
            return;

        }
        */
/*If code reaches this point, congratulations! The user has granted screen mirroring permission
         * Let us set the recorderservice intent with relevant data and start service*//*

        Intent recorderService = new Intent(this, RecorderService.class);
        recorderService.setAction(Const.SCREEN_RECORDING_START);
        recorderService.putExtra(Const.RECORDER_INTENT_DATA, data);
        recorderService.putExtra(Const.RECORDER_INTENT_RESULT, resultCode);
        startService(recorderService);

     */
/*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mService.mMediaRecorder.setOnInfoListener(BaseActivity.this);
            }
        }, 1000);
*//*


    }
*/

    @Override
    public void getAllCallBacks(Context context, Intent intent) {

    }
}