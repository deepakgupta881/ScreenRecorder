package com.example.recordingservice;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class BaseActivity extends AppCompatActivity implements GlobalReceiverCallBack, RecordingRestartNewFileCallBack {
    private MediaProjection mMediaProjection;
    private MediaProjectionManager mProjectionManager;
    RecorderService mService;
    boolean mBound = false;
    private GlobalReceiver globalReceiver;

    public ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder binder) {
            Log.d("ServiceConnection", "connected");
            RecorderService.LocalBinder binders = (RecorderService.LocalBinder) binder;
            mService = ((RecorderService.LocalBinder) binder).getService();
            mBound = true;
        }
        //binder comes from server to communicate with method's of

        public void onServiceDisconnected(ComponentName className) {
            Log.d("ServiceConnection", "disconnected");
            mService = null;
            mBound = false;
        }

    };

    public boolean requestPermissionStorage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.storage_permission_request_title))
                    .setMessage(getString(R.string.storage_permission_request_summary))
                    .setNeutralButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(BaseActivity.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    Const.EXTDIR_REQUEST_CODE);
                        }
                    })
                    .setCancelable(false);

            alert.create().show();
            return false;
        }
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissionStorage();

        /*//Acquiring media projection service to start screen mirroring
        mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);


        if (isServiceRunning(RecorderService.class)) {
            Log.d(Const.TAG, "service is running");
        }*/

        Session.setmGlobalReceiverCallback(this);
        Session.setRecordingRestartNewFileCallBackCallback(this);
        mProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (isServiceRunning(RecorderService.class)) {
            Log.d(Const.TAG, "service is running");
        }
    }

    public void start() {
        if (mMediaProjection == null) {
            //Request Screen recording permission
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), Const.SCREEN_RECORD_REQUEST_CODE);
        }/* else if (isServiceRunning(RecorderService.class)) {
            //stop recording if the service is already active and recording
            Toast.makeText(BaseActivity.this, "Screen already recording", Toast.LENGTH_SHORT).show();
        }*/
    }   /* Set resume intent and start the recording service
     * NOTE: A service can be started only once. Any subsequent startService only passes the intent
     * if any by calling onStartCommand */

    public void resumeScreenRecording() {
        if (isServiceRunning(RecorderService.class)) {
            Intent resumeIntent = new Intent(this, RecorderService.class);
            resumeIntent.setAction(Const.SCREEN_RECORDING_RESUME);
            startService(resumeIntent);
        }
    }

    // Set pause intent and start the recording service
    public void pauseScreenRecording() {
        if (isServiceRunning(RecorderService.class)) {
            Intent pauseIntent = new Intent(this, RecorderService.class);
            pauseIntent.setAction(Const.SCREEN_RECORDING_PAUSE);
            startService(pauseIntent);
        }
    }

    // Set stop intent and start the recording service
    public void stopScreenSharing() {
        if (isServiceRunning(RecorderService.class)) {
            Intent stopIntent = new Intent(this, RecorderService.class);
            stopIntent.setAction(Const.SCREEN_RECORDING_STOP);
            startService(stopIntent);
        }
    }

    //Method to check if the service is running
    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCallBackReceived(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.SCREEN_OFF")) {
            Log.d(BaseActivity.class.getName(), "off");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pauseScreenRecording();
                }
            }, 1000);
        }
        if (intent.getAction().equals("android.intent.action.SCREEN_ON")) {
            Log.d(BaseActivity.class.getName(), "on");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    resumeScreenRecording();
                }
            }, 1000);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //The user has denied permission for screen mirroring. Let's notify the user
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED && requestCode == Const.SCREEN_RECORD_REQUEST_CODE) {
            Toast.makeText(this,
                    getString(R.string.screen_recording_permission_denied), Toast.LENGTH_SHORT).show();
            //Return to home screen if the app was started from app shortcut
            if (getIntent().getAction().equals(getString(R.string.app_shortcut_action)))
                this.finish();
            return;

        }
        /*If code reaches this point, congratulations! The user has granted screen mirroring permission
         * Let us set the recorderservice intent with relevant data and start service*/
        Intent recorderService = new Intent(this, RecorderService.class);
        recorderService.setAction(Const.SCREEN_RECORDING_START);
        recorderService.putExtra(Const.RECORDER_INTENT_DATA, data);
        recorderService.putExtra(Const.RECORDER_INTENT_RESULT, resultCode);
        startService(recorderService);


     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mService.mMediaRecorder.setOnInfoListener(BaseActivity.this);
            }
        }, 1000);
*/

    }

    @Override
    public void onCallRecordingRestart() {
        Log.d(Const.TAG, "aayasaaaaaaaaaaaa");
        stopScreenSharing();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }, 1000);

    }

}
