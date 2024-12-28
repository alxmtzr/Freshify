package de.alxmtzr.freshify.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.List;

import de.alxmtzr.freshify.R;
import de.alxmtzr.freshify.data.model.ItemEntity;
import de.alxmtzr.freshify.ui.MainActivity;

public class NotificationService {

    private static final int NOTIFY_SINGLE_EXPIRING_ITEM_ID = 1;
    private static final int NOTIFY_MULTIPLE_EXPIRING_ITEMS_ID = 2;
    private static final int NOTIFY_SINGLE_EXPIRED_ITEM_ID = 3;
    private static final int NOTIFY_MULTIPLE_EXPIRED_ITEMS_ID = 4;

    private static final String CHANNEL_ID = "freshify_notifications";
    private static final String CHANNEL_DESCRIPTION = "Show notifications for expired or expiring items";
    private final Context context;
    private final NotificationManager notificationManager;

    public NotificationService(Context context) {
        this.context = context;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_DESCRIPTION,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void notifyExpiredItems(List<ItemEntity> expiredItems) {
        if (expiredItems.isEmpty()) {
            return; // no notification if no items have expired
        }

        if (expiredItems.size() == 1) {
            // notify user about a single expired item
            ItemEntity item = expiredItems.get(0);
            sendNotification(
                    context.getString(R.string.item_expired),
                    context.getString(R.string.the_item)
                            + item.getName()
                            + context.getString(R.string.has_expired),
                    NOTIFY_SINGLE_EXPIRING_ITEM_ID
            );
        } else {
            // notify user about multiple expired items
            sendNotification(
                    context.getString(R.string.multiple_items_expired),
                    expiredItems.size() + context.getString(R.string.items_have_expired_check_your_list),
                    NOTIFY_MULTIPLE_EXPIRING_ITEMS_ID
            );
        }
    }

    public void notifyExpiringItems(List<ItemEntity> expiringItems) {
        if (expiringItems.isEmpty()) {
            return; // no notification if no items have expired
        }

        if (expiringItems.size() == 1) {
            // notify user about a single expiring item
            ItemEntity item = expiringItems.get(0);
            sendNotification(
                    context.getString(R.string.item_expiring_soon),
                    context.getString(R.string.the_item)
                            + item.getName()
                            + context.getString(R.string.is_expiring_soon),
                    NOTIFY_SINGLE_EXPIRED_ITEM_ID
            );
        } else {
            // notify user about multiple expiring items
            sendNotification(
                    context.getString(R.string.multiple_items_expiring_soon),
                    expiringItems.size() + context.getString(R.string.items_are_expiring_soon_check_your_list),
                    NOTIFY_MULTIPLE_EXPIRED_ITEMS_ID
            );
        }
    }

    private void sendNotification(String title, String message, int id) {
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(createContentIntent())
                .setAutoCancel(true)
                .build();
        notificationManager.notify(id, notification);
    }

    private PendingIntent createContentIntent() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        return PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
    }
}


