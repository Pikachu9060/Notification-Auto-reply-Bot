package com.example.notificationlistener;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.notificationlistener.NotificationHelper.Action;
import com.example.notificationlistener.NotificationHelper.NotificationUtils;

import java.util.Locale;

public class MyService extends NotificationListenerService {
    private DatabaseHelper databaseHelper;
    private static final int NOTIF_ID = 1;
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startForeground();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.out.println("Destroyed");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        System.out.println("Unbinded");
        sendBroadcast(new Intent(this, ServiceStateBroadcastReceiver.class));
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        System.out.println("Hello");
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
            Action action = NotificationUtils.getQuickReplyAction(sbn.getNotification(), this.getPackageName());
            if(sbn.getPackageName().equals("com.whatsapp")){
                String reply = getReply(sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT).toString());
                if(action != null){
                    try{
                        action.sendReply(getApplicationContext(), reply);
                    }catch(Exception e){
                        Log.d("Posted Exception",e.getMessage());
                    }
                }
            }

        MyService.this.cancelNotification(sbn.getKey());

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        sendBroadcast(new Intent(this, ServiceStateBroadcastReceiver.class));
        System.out.println("Task removed");
    }

    private void startForeground(){
        Intent notificationIntent = new Intent(this, MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("Notification", "Whatsapp Listener in Background", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = (NotificationManager) getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Notification");
        builder.setContentTitle("Whatsapp Listener");
        builder.setContentText("Whatsapp Listener in Background");
        builder.setSmallIcon(R.drawable.ic_android_black_24dp);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);

        startForeground(NOTIF_ID, builder.build());

    }

    private String getReply(String message){
        Cursor cursor = databaseHelper.getReply(message.toLowerCase(Locale.ROOT));
        try{
            cursor.moveToFirst();
            String reply = cursor.getString(0);
            System.out.println("Reply got : " + reply);
            return reply;
        }catch(Exception e){
            Log.d("GetReply",e.getMessage());
            return "Sorry i didn't understand";
        }
    }
}