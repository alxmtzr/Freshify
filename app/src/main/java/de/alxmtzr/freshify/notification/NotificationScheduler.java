package de.alxmtzr.freshify.notification;

import android.content.Context;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import java.util.concurrent.TimeUnit;

public class NotificationScheduler {

    public static void scheduleNotifications(Context context) {
        PeriodicWorkRequest notificationRequest =
                new PeriodicWorkRequest.Builder(
                        NotificationWorker.class,
                        15,
                        TimeUnit.MINUTES
                ).build();

        WorkManager.getInstance(context).enqueue(notificationRequest);
    }
}
