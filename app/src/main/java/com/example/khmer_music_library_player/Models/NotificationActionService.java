package com.example.khmer_music_library_player.Models;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class NotificationActionService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent("_TRACKS_TRACKS")
        .putExtra("_actionname", intent.getAction()));
    }


}
