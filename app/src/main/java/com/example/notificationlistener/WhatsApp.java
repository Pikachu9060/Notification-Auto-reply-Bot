package com.example.notificationlistener;

import android.app.Application;
import android.content.Intent;

public class WhatsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MyService.class));
    }
}
