package com.example.recordingservice.receiver;

import android.content.BroadcastReceiver;
import android.content.Intent;

/**
 * the result of registering to a {@link BroadcastReceiver} with a sticky broadcast, and the
 * sticky {@link Intent}
 * <p>
 *
 * @author gaurav
 */
public class StickyRegistration {

    /**
     * the {@link BroadcastReceiver} to unregister later
     */
    public final BroadcastReceiver receiver;

    /**
     * the {@link Intent}
     */
    public final Intent intent;

    StickyRegistration(BroadcastReceiver receiver, Intent intent) {
        this.receiver = receiver;
        this.intent = intent;
    }
}
