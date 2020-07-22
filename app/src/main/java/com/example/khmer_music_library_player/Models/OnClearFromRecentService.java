package com.example.khmer_music_library_player.Models;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class OnClearFromRecentService extends Service {

    public class KillBinder extends Binder {
        public final Service service;
        public KillBinder(Service service) {
            this.service = service;
        }
    }

    private final IBinder mBinder = new KillBinder(this);
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopSelf();
    }
}
