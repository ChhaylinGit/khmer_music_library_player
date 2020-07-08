package com.example.khmer_music_library_player.Fragment.Tab1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.khmer_music_library_player.Activity.MainActivity;
import com.example.khmer_music_library_player.Activity.PlayerActivity;
import com.example.khmer_music_library_player.Adapter.MusicAdapter;
import com.example.khmer_music_library_player.Models.ConstantField;
import com.example.khmer_music_library_player.Models.GetMusics;
import com.example.khmer_music_library_player.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class NewMusicFragment extends Fragment{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Boolean checkIn =false;
    private List<GetMusics> getMusicsList =new ArrayList<>();
    private MusicAdapter musicAdapter;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private ArrayList<JcAudio> jcAudiosList = new ArrayList<>();
    private int currentIndex;
    private LinearLayout linearLayoutWaitLoadMusic;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private Button btnPlay,btnNext,btnPrevious;
    private TextView textViewStartDuration,textViewEndDuration,textViewMusicTitle,textViewSinger;
    private int playingPosition=0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_music, container, false);
        initView(view);
        getMusicsList();
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
                            playingPosition = position;
                            initPlayer(playingPosition);
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
        linearLayoutWaitLoadMusic = view.findViewById(R.id.linearlayoutWaitLoadMusic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playingPosition < getMusicsList.size() - 1) {
                    playingPosition++;
                } else {
                    playingPosition = 0;

                }
                initPlayer(playingPosition);
            }
        });
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playingPosition <= 0) {
                    playingPosition = getMusicsList.size() - 1;
                } else {
                    playingPosition--;
                }

                initPlayer(playingPosition);
            }
        });
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

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }

        GetMusics getMusics = getMusicsList.get(position);
        Uri songResourceUri = Uri.parse(getMusics.mp3Uri);
        textViewMusicTitle.setText(getMusics.musicTitle);
        textViewSinger.setText(getActivity().getResources().getString(R.string.sing_by)+" "+getMusics.singerName);
        mediaPlayer = MediaPlayer.create(getActivity(), songResourceUri); // create and load mediaplayer with song resources
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                String totalTime = createTimeLabel(mediaPlayer.getDuration());
                textViewEndDuration.setText(totalTime);
                seekBar.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                btnPlay.setBackground(getResources().getDrawable(R.drawable.pause_96px));
                musicAdapter.setIndex(position);
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                int curSongPoition = position;
                if (curSongPoition < getMusicsList.size() - 1) {
                    curSongPoition++;
                    initPlayer(curSongPoition);
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

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

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
            btnPlay.setBackground(getResources().getDrawable(R.drawable.pause_96px));
        } else {
            pause();
        }
    }
    private void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setBackground(getResources().getDrawable(R.drawable.play_96px));
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

}