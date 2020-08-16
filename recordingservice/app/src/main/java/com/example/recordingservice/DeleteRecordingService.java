package com.example.recordingservice;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.abedelazizshe.lightcompressorlibrary.CompressionListener;
import com.abedelazizshe.lightcompressorlibrary.VideoCompressor;
import com.abedelazizshe.lightcompressorlibrary.VideoQuality;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DeleteRecordingService extends JobIntentService {
    final Handler mHandler = new Handler();
    public ArrayList<String> pathToBeDeleted = new ArrayList<>();
    private SharedPreferences prefs;

    private static final String TAG = "MyJobIntentService";
    /**
     * Unique job ID for this service.
     */
    private static final int JOB_ID = 2;

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, DeleteRecordingService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Const.TAG, "Job Execution Started");
    }

    // Helper for showing tests
    void showToast(final String text) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(DeleteRecordingService.this, text, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        String oldPath = intent.getStringExtra("savepath");
        pathToBeDeleted.add(oldPath);
        compressVideo();
    }

    private String getFileSaveName() {
        String filename = prefs.getString(getString(R.string.filename_key), "yyyyMMdd_hhmmss");
        String prefix = prefs.getString(getString(R.string.fileprefix_key), "recording");
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(filename);
        return prefix + "_" + formatter.format(today);
    }

    private void compressVideo() {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String saveLocation = Environment.getExternalStorageDirectory() + File.separator + Const.APPDIR;
        File saveDir = new File(saveLocation);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) && !saveDir.isDirectory()) {
            saveDir.mkdirs();
        }
        String saveFileName = getFileSaveName();
        String filep = saveLocation + File.separator + saveFileName + "_order.mp4";
        final ArrayList<String> toBeScanned = new ArrayList<>();
        toBeScanned.add(filep);
        String[] toBeScannedStr = new String[toBeScanned.size()];
        toBeScannedStr = toBeScanned.toArray(toBeScannedStr);
        final String[] finalToBeScannedStr = toBeScannedStr;

        VideoCompressor.INSTANCE.start(pathToBeDeleted.get(0), filep, new CompressionListener() {
            @Override
            public void onStart() {
                // Compression start
            }

            @Override
            public void onSuccess() {
                // On Compression success
                MediaScannerConnection.scanFile(DeleteRecordingService.this, finalToBeScannedStr, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @Override
                    public void onScanCompleted(String path, Uri uri) {
                        Log.i(Const.TAG, "SCAN COMPLETED: " + path);
                        //Show toast on main thread
                        if (pathToBeDeleted.size() > 0) {
                            File fileDelete = new File(pathToBeDeleted.get(0));
                            if (fileDelete.exists()) {
                                if (fileDelete.delete()) {
                                    Log.i(Const.TAG, "file Deleted : ");
                                    pathToBeDeleted.remove(0);
                                } else {
                                    Log.i(Const.TAG, "file not Deleted: ");
                                }
                            }
                        }

                    }
                });
            }

            @Override
            public void onFailure(String failureMessage) {
                // On Failure
            }

            @Override
            public void onProgress(float v) {
                // Update UI with progress value

            }

            @Override
            public void onCancelled() {
                // On Cancelled
            }
        }, VideoQuality.LOW, false, false);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(Const.TAG, "Job Execution Finished");
//        showToast("Job Execution Finished");
    }

}
