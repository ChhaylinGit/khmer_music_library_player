package com.example.khmer_music_library_player.Fragment.Tab1;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Message;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.khmer_music_library_player.Activity.MainActivity;
import com.example.khmer_music_library_player.Activity.PlayerActivity;
import com.example.khmer_music_library_player.Adapter.MusicAdapter;
import com.example.khmer_music_library_player.Models.ConstantField;
import com.example.khmer_music_library_player.Models.CreateNotification;
import com.example.khmer_music_library_player.Models.GetMusics;
import com.example.khmer_music_library_player.Models.OnClearFromRecentService;
import com.example.khmer_music_library_player.Models.Playable;
import com.example.khmer_music_library_player.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class NewMusicFragment extends Fragment implements Playable {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Boolean checkIn =false;
    private ArrayList<GetMusics> getMusicsList =new ArrayList<>();
    private MusicAdapter musicAdapter;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private ArrayList<JcAudio> jcAudiosList = new ArrayList<>();
    private int currentIndex;
    private LinearLayout linearLayoutWaitLoadMusic;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private FloatingActionButton btnPlay,btnNext,btnPrevious;
    private TextView textViewStartDuration,textViewEndDuration,textViewMusicTitle,textViewSinger;
    private int playingPosition=0;
    private NotificationManager notificationManager;
    private CardView cardviewMain;
    private ImageView imageView;


    private int notifposition = 0;
    private boolean isPlaying = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_music, container, false);
        initView(view);
        getMusicsList();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            getActivity().registerReceiver(broadcastReceiver, new IntentFilter("_TRACKS_TRACKS"));
            getActivity().startService(new Intent(getActivity().getBaseContext(), OnClearFromRecentService.class));
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void getMusicsList()
    {
        databaseReference = FirebaseDatabase.getInstance().getReference(ConstantField.DATABASE_PATH_MUSIC);
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMusicsList.clear();
                for (DataSnapshot dss : dataSnapshot.getChildren())
                {
                    for(DataSnapshot singerSnapshot : dataSnapshot.child(dss.getKey()).getChildren())
                    {
                        GetMusics getMusics = singerSnapshot.getValue(GetMusics.class);
                        getMusicsList.add(getMusics);
                    }
                }
                Collections.shuffle(getMusicsList); // Random item in list

                    musicAdapter = new MusicAdapter(getActivity(), getMusicsList, new MusicAdapter.RecyclerItemClickListener() {
                        @Override
                        public void onClickListener(GetMusics getMusics, int position) {
                            cardviewMain.setVisibility(View.VISIBLE);
                            playingPosition = position;
                            notifposition = position;
                            initPlayer(playingPosition);
                            onTrackPlay();
                        }
                    });
                    recyclerView.setAdapter(musicAdapter);
                    musicAdapter.setSelectedPosition(0);
                    musicAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    linearLayoutWaitLoadMusic.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void  initView(View view)
    {
        recyclerView = view.findViewById(R.id.recyclerViewMusic);
        progressBar = view.findViewById(R.id.progressBarPlayer);
        seekBar = view.findViewById(R.id.seekBar);
        btnPlay = view.findViewById(R.id.btnPlay);
        btnNext = view.findViewById(R.id.btnNext);
        btnPrevious = view.findViewById(R.id.btnPrevious);
        textViewStartDuration = view.findViewById(R.id.textViewStartDuration);
        textViewEndDuration = view.findViewById(R.id.textViewEndDuration);
        textViewMusicTitle = view.findViewById(R.id.textViewMusicTitle);
        textViewSinger = view.findViewById(R.id.textViewSinger);
        imageView = view.findViewById(R.id.imgSingerProfile);
        progressBar = view.findViewById(R.id.progressBarPlayer);
        linearLayoutWaitLoadMusic = view.findViewById(R.id.linearlayoutWaitLoadMusic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cardviewMain = view.findViewById(R.id.cardviewMain);
        mediaPlayer = new MediaPlayer();
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextMusic();
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                previousMusic();
            }
        });
    }

    private void previousMusic()
    {
        if (playingPosition <= 0) {
            playingPosition = getMusicsList.size() - 1;
        } else {
            playingPosition--;
        }
        initPlayer(playingPosition);
        onTrackPrevious();
    }

    private void nextMusic()
    {
        if (playingPosition < getMusicsList.size() - 1) {
            playingPosition++;
        } else {
            playingPosition = 0;
        }
        initPlayer(playingPosition);
        onTrackNext();
    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID,
                    "ChhayLin", NotificationManager.IMPORTANCE_LOW);

            notificationManager = getActivity().getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
    public void changeSelectedSong(int index)
    {
        musicAdapter.setSelectedPosition(musicAdapter.getSelectedPosition());
        currentIndex = index;
        musicAdapter.setSelectedPosition(currentIndex);
        musicAdapter.setSelectedPosition(currentIndex);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int current_position = msg.what;
            seekBar.setProgress(current_position);
            String cTime = createTimeLabel(current_position);
            textViewStartDuration.setText(cTime);
        }
    };

    private void initPlayer(final int position) {
        progressBar.setVisibility(View.VISIBLE);
        btnPlay.setImageResource(R.drawable.play_96px);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }
        GetMusics getMusics = getMusicsList.get(position);
        textViewMusicTitle.setText(getMusics.musicTitle);
        textViewSinger.setText(getActivity().getResources().getString(R.string.sing_by)+" "+getMusics.singerName);
//        mediaPlayer = MediaPlayer.create(getContext(), Uri.parse(getMusics.mp3Uri)); // create and load mediaplayer with song resources
        Picasso.get().load(getMusics.getSingerImageUrl()).placeholder(R.drawable.ic_image_black_24dp).into(imageView);
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(getMusics.mp3Uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                btnPlay.setImageResource(R.drawable.pause_96px);
                String totalTime = createTimeLabel(mediaPlayer.getDuration());
                textViewEndDuration.setText(totalTime);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                musicAdapter.setIndex(position,true);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int curSongPoition = position;
                if (curSongPoition < getMusicsList.size() - 1) {
                    curSongPoition++;
                    playingPosition = curSongPoition;
                    initPlayer(curSongPoition);
                    onTrackNext();
                } else {
                    curSongPoition = 0;
                    initPlayer(curSongPoition);
                }

            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                btnPlay.setImageResource(R.drawable.play_96px);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                btnPlay.setImageResource(R.drawable.pause_96px);
                progressBar.setVisibility(View.GONE);
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            Message msg = new Message();
                            msg.what = mediaPlayer.getCurrentPosition();
                            handler.sendMessage(msg);
                            Thread.sleep(1000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void play() {

        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            btnPlay.setImageResource(R.drawable.pause_96px);
            musicAdapter.setIndex(playingPosition,true);
            onTrackPlay();
        } else {
            pause();
            onTrackPause();
        }
    }
    private void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setImageResource(R.drawable.play_96px);
            musicAdapter.setIndex(playingPosition,false);
        }
    }
    public String createTimeLabel(int duration) {
        String timeLabel = "";
        int min = duration / 1000 / 60;
        int sec = duration / 1000 % 60;
        timeLabel += min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;
        return timeLabel;
    }


    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getExtras().getString("_actionname");
            Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
            switch (action){
                case CreateNotification.ACTION_PREVIUOS:
                    onTrackPrevious();
                    break;
                case CreateNotification.ACTION_PLAY:
                    if (isPlaying){
                        onTrackPause();
                    } else {
                        onTrackPlay();
                    }
                    break;
                case CreateNotification.ACTION_NEXT:
                    onTrackNext();
                    break;
            }
        }
    };

    @Override
    public void onTrackPrevious() {
        notifposition--;
        CreateNotification.createNotification(getActivity(), getMusicsList.get(notifposition),
                R.drawable.ic_pause_black_24dp, notifposition, getMusicsList.size()-1);
    }

    @Override
    public void onTrackPlay() {

        CreateNotification.createNotification(getActivity(), getMusicsList.get(notifposition),
                R.drawable.ic_pause_black_24dp, notifposition, getMusicsList.size()-1);
        isPlaying = true;
    }

    @Override
    public void onTrackPause() {
        CreateNotification.createNotification(getActivity(), getMusicsList.get(notifposition),
                R.drawable.ic_play_arrow_black_24dp, notifposition, getMusicsList.size()-1);
        isPlaying = false;
    }

    @Override
    public void onTrackNext() {
        notifposition++;
        CreateNotification.createNotification(getActivity(), getMusicsList.get(notifposition),
                R.drawable.ic_pause_black_24dp, notifposition, getMusicsList.size()-1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notificationManager.cancelAll();
        }
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}