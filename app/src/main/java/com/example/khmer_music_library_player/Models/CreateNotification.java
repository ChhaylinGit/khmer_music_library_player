package com.example.khmer_music_library_player.Models;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.example.khmer_music_library_player.Activity.MainActivity;
import com.example.khmer_music_library_player.Activity.Navegation_Drawer;
import com.example.khmer_music_library_player.R;


import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class CreateNotification {

    public static final String CHANNEL_ID = "_channel1";
    public static final String ACTION_PREVIUOS = "_actionprevious";
    public static final String ACTION_PLAY = "_actionplay";
    public static final String ACTION_NEXT = "_actionnext";
    public static Notification notification;
    static final int NOTIFICATION_ID = 1;
    public static void createNotification(Context context, GetMusics track, int playbutton, int pos, int size){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            MediaSessionCompat mediaSessionCompat = new MediaSessionCompat( context, "tag");
            Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.drawable.one);
            PendingIntent pendingIntentPrevious;
            int drw_previous;
            if (pos == 0){
                pendingIntentPrevious = null;
                drw_previous = 0;
            } else {
                Intent intentPrevious = new Intent(context, NotificationActionService.class)
                        .setAction(ACTION_PREVIUOS);
                pendingIntentPrevious = PendingIntent.getBroadcast(context, 0,
                        intentPrevious, PendingIntent.FLAG_UPDATE_CURRENT);
                drw_previous = R.drawable.ic_skip_previous_black_24dp;
            }

            Intent intentPlay = new Intent(context, NotificationActionService.class).setAction(ACTION_PLAY);
            PendingIntent pendingIntentPlay = PendingIntent.getBroadcast(context, 0, intentPlay, PendingIntent.FLAG_UPDATE_CURRENT);

            PendingIntent pendingIntentNext;
            int drw_next;
            if (pos == size){
                pendingIntentNext = null;
                drw_next = 0;
            } else {
                Intent intentNext = new Intent(context, NotificationActionService.class)
                        .setAction(ACTION_NEXT);
                pendingIntentNext = PendingIntent.getBroadcast(context, 0,
                        intentNext, PendingIntent.FLAG_UPDATE_CURRENT);
                drw_next = R.drawable.ic_skip_next_black_24dp;
            }

            Intent buttonIntent = new Intent(context, NotificationActionService.class);
            buttonIntent.putExtra("notificationId",NOTIFICATION_ID);
            PendingIntent btPendingIntent = PendingIntent.getBroadcast(context, 0, buttonIntent,0);


            //create notification
            Intent intent = new Intent(context, Navegation_Drawer.class);
            PendingIntent resultIntent = PendingIntent. getActivity (context, 0 , intent , 0 ) ;
            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_music_note)
                    .setContentTitle(track.getMusicTitle())
                    .setContentText(track.getSingerName())
                    .setContentIntent(resultIntent)
                    .setLargeIcon(icon)
                    .setOnlyAlertOnce(true)//show notification for only first time
                    .setShowWhen(true)
                    .addAction(drw_previous, "Previous", pendingIntentPrevious)
                    .addAction(playbutton, "Play", pendingIntentPlay)
                    .addAction(drw_next, "Next", pendingIntentNext)
                    .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2)
                    .setShowCancelButton(true))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .build();

            notificationManagerCompat.notify(1, notification);
        }
    }

    public static Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
