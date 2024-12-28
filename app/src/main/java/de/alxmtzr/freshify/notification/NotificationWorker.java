package de.alxmtzr.freshify.notification;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
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

        SharedPreferences preferences = getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        int daysUntilExpiry = preferences.getInt(PREF_DAYS_UNTIL_EXPIRY, 3);
        Log.d("NotificationWorker", "Days until expiry: " + daysUntilExpiry);

        List<ItemEntity> expiredItems = repository.getExpiredItems();
        List<ItemEntity> expiringSoonItems = repository.getItemsExpiringSoon(daysUntilExpiry);

        Log.d("NotificationWorker", "Expired items: " + expiredItems.size());
        Log.d("NotificationWorker", "Expiring soon items: " + expiringSoonItems.size());

        notificationService.notifyExpiredItems(expiredItems);
        notificationService.notifyExpiringItems(expiringSoonItems);

//        scheduleNextRun();

        return Result.success();
    }

    /** only for testing purposes */
    private void scheduleNextRun() {
        OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build();

        WorkManager.getInstance(getApplicationContext()).enqueue(workRequest);
    }

}
