package com.example.recordingservice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * an annotation that determines the Broadcast Receiver data MIME type in manifest, and
 * it is also the same as calling {@code IntentFilter.addDataType(String)}
 * <p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface
BroadcastReceiverDataMimeType {

    String value();
}
