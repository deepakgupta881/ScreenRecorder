package com.example.recordingservice;


// POJO class for bunch of statics used across the app
public class Const {
    public static final int VIDEO_EDIT_REQUEST_CODE = 1004;
    public static final int VIDEO_EDIT_RESULT_CODE = 1005;
    public static final String TAG = "SCREENRECORDER";
    public static final String APPDIR = "screenrecorder";
    public static final String VIDEO_EDIT_URI_KEY = "edit_video";
    static final int EXTDIR_REQUEST_CODE = 1000;
    static final int AUDIO_REQUEST_CODE = 1001;
    static final int SYSTEM_WINDOWS_CODE = 1002;
    static final int SCREEN_RECORD_REQUEST_CODE = 1003;
    static final String SCREEN_RECORDING_START = "com.orpheusdroid.screenrecorder.services.action.startrecording";
    static final String SCREEN_RECORDING_PAUSE = "com.orpheusdroid.screenrecorder.services.action.pauserecording";
    static final String SCREEN_RECORDING_RESUME = "com.orpheusdroid.screenrecorder.services.action.resumerecording";
    static final String SCREEN_RECORDING_STOP = "com.orpheusdroid.screenrecorder.services.action.stoprecording";
    static final int SCREEN_RECORDER_NOTIFICATION_ID = 5001;
    static final int SCREEN_RECORDER_SHARE_NOTIFICATION_ID = 5002;
    static final String RECORDER_INTENT_DATA = "recorder_intent_data";
    static final String RECORDER_INTENT_RESULT = "recorder_intent_result";

    public enum RecordingState {
        RECORDING, PAUSED, STOPPED
    }
}
