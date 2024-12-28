package de.alxmtzr.freshify.notification;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.alxmtzr.freshify.data.local.FreshifyRepository;
import de.alxmtzr.freshify.data.local.impl.FreshifyDBHelper;
import de.alxmtzr.freshify.data.local.impl.FreshifyRepositoryImpl;
import de.alxmtzr.freshify.data.model.ItemEntity;

public class NotificationWorker extends Worker {
    static final String PREF_DAYS_UNTIL_EXPIRY = "days_until_expiry";
    static final String PREF_NAME = "prefs_freshify";

    private FreshifyRepository repository;
    private final NotificationService notificationService;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.notificationService = new NotificationService(context);

        FreshifyDBHelper dbHelper = new FreshifyDBHelper(context);
        repository = new FreshifyRepositoryImpl(dbHelper);
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.d("NotificationWorker", "Worker started");

        // check permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU
                && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.d("NotificationWorker", "Permission missing. Skip notification.");
                return Result.success();
            }


        SharedPreferences prefs = getApplicationContext().getSharedPreferences(PREF_NAME, 0);
        int daysUntilExpiry = prefs.getInt(PREF_DAYS_UNTIL_EXPIRY, 3);

        List<ItemEntity> expiredItems = repository.getExpiredItems();
        List<ItemEntity> expiringSoonItems = repository.getItemsExpiringSoon(daysUntilExpiry);

        notificationService.notifyExpiredItems(expiredItems);
        notificationService.notifyExpiringItems(expiringSoonItems);

        // scheduleNextRun(); // only for testing purposes

        return Result.success();
    }

    /** only for testing purposes
     * sends notifications every minute */
    private void scheduleNextRun() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
    }

}
