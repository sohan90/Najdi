package com.najdi.android.najdiapp.common;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.launch.view.SplashScreenActivity;
import com.najdi.android.najdiapp.utitility.LogUtil;
import com.najdi.android.najdiapp.utitility.PreferenceUtils;

import java.util.Map;

import static com.najdi.android.najdiapp.utitility.PreferenceUtils.FCM_TOKEN_KEY;

public class FirebaseService extends FirebaseMessagingService {
    private static final String TAG = FirebaseService.class.getSimpleName();
    public static final String CHANNEL_ID = "channel_id";
    private static final int NOTIFICATION_ID = 12345678;

    @Override
    public void onNewToken(@NonNull String s) {
        LogUtil.d(TAG, "onNewToken " + s);
        PreferenceUtils.setValueString(this, FCM_TOKEN_KEY, s);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        String token = task.getResult().getToken(); // Get new Instance ID token
                        LogUtil.d("My Token", token);
                    }

                });
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().size() > 0) {
            sendPushNotification(remoteMessage.getData());
        }
    }

    private void sendPushNotification(Map<String, String> data) {
        String title = data.get("title");
        String message = data.get("message");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // The PendingIntent to launch activity.
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)  //a resource for your custom small icon
                .setContentTitle(title) //the "title" value you sent in your notification
                .setContentText(message) //ditto
                .setAutoCancel(true)  //dismisses the notification on click
                .setSound(defaultSoundUri)
                .setContentIntent(activityPendingIntent);

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && notificationManager != null) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            notificationBuilder.setChannelId(CHANNEL_ID); // Channel ID
        }

        if (notificationManager == null) return;
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

    }
}
