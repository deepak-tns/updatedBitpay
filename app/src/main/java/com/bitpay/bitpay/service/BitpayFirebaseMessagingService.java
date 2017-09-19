package com.bitpay.bitpay.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bitpay.bitpay.R;
import com.bitpay.bitpay.activity.SplashActivity;
import com.bitpay.bitpay.constant.AppConstants;
import com.bitpay.bitpay.models.NotificationModel;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import org.json.JSONObject;

import java.util.Map;

import static android.media.RingtoneManager.getDefaultUri;


/**
 * Created by admin on 1-2-2017.
 */
public class BitpayFirebaseMessagingService extends FirebaseMessagingService implements AppConstants {

    private static final String TAG = "FirebaseMessagService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived() enter");
        if (remoteMessage == null)
            return;
        Log.d(TAG, "onMessageReceived() From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "DataPayload: " + remoteMessage.getData().toString());
            try {

                Map<String, String> params = remoteMessage.getData();
                JSONObject object = new JSONObject(params);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                NotificationModel notificationModel = gson.fromJson(object.toString(), NotificationModel.class);
                handleDataMessage(notificationModel);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleDataMessage(NotificationModel notificationModel) {
        Context context = getApplicationContext();
        if (context == null || notificationModel == null) {
            return;
        }
        try {
            String title = notificationModel.getTitle();
            String message = notificationModel.getMessage();
            String type = notificationModel.getType();
            long timestamp = notificationModel.getTimestamp();

            final int icon = R.mipmap.launcher_icon;
            Intent intent = new Intent(context, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            final PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);

            Uri alarmSound = getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.bigText(message);
            bigTextStyle.setBigContentTitle(title);

            Notification notification = mBuilder.setSmallIcon(icon).setTicker(title).setWhen(0)
                    .setStyle(bigTextStyle)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setContentIntent(resultPendingIntent)
                    .setSound(alarmSound)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setWhen(timestamp)
                    .setSmallIcon(getNotificationIcon())
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setColor(getNotificationColor())
                    .setContentText(message)
                    .build();

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.notify(0, notification);
            }

        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private int getNotificationColor() {
        return R.color.colorPrimary;
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.launcher_icon : R.mipmap.launcher_icon;
    }
}
