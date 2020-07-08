package com.example.khmer_music_library_player.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import com.example.khmer_music_library_player.R;


public class PlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Uri songResourceUri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/khmer-music-library.appspot.com/o/Music%2F%E1%9E%9F%E1%9F%82%E1%9E%93%E1%9E%9F%E1%9F%92%E1%9E%9A%E1%9E%8E%E1%9F%84%E1%9F%87.%E1%9E%9F%E1%9E%B6%E1%9E%9A%E1%9F%89%E1%9E%B6%E1%9E%8F%E1%9F%8B.mp3?alt=media&token=2d176fb9-a10e-4f3a-b5d3-d38c38764539");
        mediaPlayer = MediaPlayer.create(getApplicationContext(), songResourceUri); // create and load mediaplayer with song resources
        mediaPlayer.start();
    }
}
