package com.example.khmer_music_library_player.Fragment.Tab1;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.khmer_music_library_player.Adapter.MusicAdapter;
import com.example.khmer_music_library_player.Models.ConstantField;
import com.example.khmer_music_library_player.Models.CreateNotification;
import com.example.khmer_music_library_player.Models.GetMusics;
import com.example.khmer_music_library_player.Models.OnClearFromRecentService;
import com.example.khmer_music_library_player.Models.Playable;
import com.example.khmer_music_library_player.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class NewMusicFragment extends Fragment implements Playable, View.OnClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Boolean checkIn =false;
    private ArrayList<GetMusics> getMusicsList =new ArrayList<>();
    private MusicAdapter musicAdapter;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private LinearLayout linearLayoutWaitLoadMusic;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar,seekBarMain;
    private FloatingActionButton btnPlay,btnPlayMain,btnNext,btnNextMain,btnPrevious,btnPreviousMain;
    private TextView textViewStartDuration,textViewStartDurationMain,textViewEndDuration,textViewEndDurationMain,textViewMusicTitle,textViewMusicTitleMain,textViewSinger,textViewSingerMain;
    private int playingPosition=0;
    private NotificationManager notificationManager;
    private ImageView imageView;
    private boolean isPlaying = false;
    private Context thisContext;
    private FrameLayout frameLayout;
    private SlidingUpPanelLayout slideup_panel;
    private CardView cardView;
    private AdView adView;
    private LinearLayout mediaContainerWithAds;
    private ImageView imgSingerProfileMain;
    private Button btnDownload,btnTimer,btnPlayList;
    private ObjectAnimator animator;
    private LinearLayout linearlayoutImage;

    public NewMusicFragment(Context context)
    {
        thisContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_music, container, false);
        initMainView();
        initView(view);
        getMusicsList();
        initAnimate();
        return view;
    }

    private void initAnimate()
    {
        animator = ObjectAnimator.ofFloat(imgSingerProfileMain, View.ROTATION, 0f, 360f);
        animator.setDuration(40000);
        animator.setRepeatCount(Animation.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannel();
            getActivity().registerReceiver(broadcastReceiver, new IntentFilter("_TRACKS_TRACKS"));
            getActivity().startService(new Intent(getActivity().getBaseContext(), OnClearFromRecentService.class));
        }
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
                Collections.shuffle(getMusicsList); //Random item in list
                musicAdapter = new MusicAdapter(getActivity(), getMusicsList, new MusicAdapter.RecyclerItemClickListener() {
                        @Override
                        public void onClickListener(GetMusics getMusics, int position) {
                          playingPosition = position;
                          initPlayer(playingPosition);
                          slideup_panel.setPanelHeight(mediaContainerWithAds.getHeight());
                          frameLayout.setVisibility(View.VISIBLE);
                          animator.start();
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

    //  RotateAnimation  mRotateUpAnim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
    //  mRotateUpAnim.setInterpolator(new LinearInterpolator());
    //  mRotateUpAnim.setRepeatCount(Animation.INFINITE);
    //  mRotateUpAnim.setDuration(40000);
    //  mRotateUpAnim.setFillAfter(true);
    //  imgSingerProfileMain.startAnimation(mRotateUpAnim);
    //  if(!result){mRotateUpAnim.cancel();}

    private void initMainView()
    {
        imgSingerProfileMain = ((Activity)thisContext).findViewById(R.id.imgSingerProfileMain);
        mediaContainerWithAds = ((Activity)thisContext).findViewById(R.id.mediaContainerWithAds);
        cardView = ((Activity)thisContext).findViewById(R.id.cardviewPlayerMain);
        slideup_panel = ((Activity)thisContext).findViewById(R.id.slideup_panel);
        frameLayout = ((Activity)thisContext).findViewById(R.id.musicContainer);
        progressBar = ((Activity)thisContext).findViewById(R.id.progressBarPlayer);
        seekBarMain = ((Activity)thisContext).findViewById(R.id.seekBarMain);
        seekBar = ((Activity)thisContext).findViewById(R.id.seekBar);
        btnPlay = ((Activity)thisContext).findViewById(R.id.btnPlay);
        btnPlayMain = ((Activity)thisContext).findViewById(R.id.btnPlayMain);
        btnNext = ((Activity)thisContext).findViewById(R.id.btnNext);
        btnNextMain = ((Activity)thisContext).findViewById(R.id.btnNextMain);
        btnPrevious = ((Activity)thisContext).findViewById(R.id.btnPrevious);
        btnPreviousMain = ((Activity)thisContext).findViewById(R.id.btnPreviousMain);
        textViewStartDuration = ((Activity)thisContext).findViewById(R.id.textViewStartDuration);
        textViewStartDurationMain = ((Activity)thisContext).findViewById(R.id.textViewStartDurationMain);
        textViewEndDuration = ((Activity)thisContext).findViewById(R.id.textViewEndDuration);
        textViewEndDurationMain = ((Activity)thisContext).findViewById(R.id.textViewEndDurationMain);
        textViewMusicTitle = ((Activity)thisContext).findViewById(R.id.textViewMusicTitle);
        textViewMusicTitleMain = ((Activity)thisContext).findViewById(R.id.textViewMusicTitleMain);
        textViewSinger = ((Activity)thisContext).findViewById(R.id.textViewSinger);
        textViewSingerMain = ((Activity)thisContext).findViewById(R.id.textViewSingerMain);
        imageView = ((Activity)thisContext).findViewById(R.id.imgSingerProfile);
        progressBar = ((Activity)thisContext).findViewById(R.id.progressBarPlayer);
        btnDownload = ((Activity)thisContext).findViewById(R.id.btnDownload);
        btnTimer = ((Activity)thisContext).findViewById(R.id.btnTimer);
        btnPlayList = ((Activity)thisContext).findViewById(R.id.btnPlayList);
        linearlayoutImage = ((Activity)thisContext).findViewById(R.id.linearlayoutImage);
    }

    private void  initView(View view)
    {
        adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        recyclerView = view.findViewById(R.id.recyclerViewMusic);
        linearLayoutWaitLoadMusic = view.findViewById(R.id.linearlayoutWaitLoadMusic);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mediaPlayer = new MediaPlayer();
        btnPlay.setOnClickListener(this);
        btnPlayMain.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnNextMain.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        btnPreviousMain.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
        btnTimer.setOnClickListener(this);
        btnPlayList.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnPlay:
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    onTrackPlay();
                }else{onTrackPause();}
                break;
            case R.id.btnPlayMain:
                if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                    onTrackPlay();
                }else{onTrackPause();}
                break;
            case R.id.btnNext:
                onTrackNext();
                break;
            case R.id.btnNextMain:
                onTrackNext();
                break;
            case R.id.btnPrevious:
                onTrackPrevious();
                break;
            case R.id.btnPreviousMain:
                onTrackPrevious();
                break;
            case R.id.btnDownload:
                Toast.makeText(thisContext, "ttttttt", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    private void createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CreateNotification.CHANNEL_ID, ConstantField.CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            notificationManager = getActivity().getSystemService(NotificationManager.class);
            if (notificationManager != null){
                notificationManager.createNotificationChannel(channel);
            }
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int current_position = msg.what;
            seekBar.setProgress(current_position);
            seekBarMain.setProgress(current_position);
            String cTime = createTimeLabel(current_position);
            textViewStartDuration.setText(cTime);
            textViewStartDurationMain.setText(cTime);
        }
    };

    private void initPlayer(final int position) {
        progressBar.setVisibility(View.VISIBLE);
        btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        btnPlayMain.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.reset();
        }
        GetMusics getMusics = getMusicsList.get(position);
        textViewMusicTitle.setText(getMusics.musicTitle);
        textViewMusicTitleMain.setText(getMusics.musicTitle);
        textViewSinger.setText(getActivity().getResources().getString(R.string.sing_by)+" "+getMusics.singerName);
        textViewSingerMain.setText(getActivity().getResources().getString(R.string.sing_by)+" "+getMusics.singerName);
        Picasso.get().load(getMusics.getSingerImageUrl()).placeholder(R.drawable.ic_image_black_24dp).into(imageView);
        Picasso.get().load(getMusics.getSingerImageUrl()).placeholder(R.drawable.ic_image_black_24dp).into(imgSingerProfileMain);
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
                btnPlay.setImageResource(R.drawable.ic_baseline_pause_24);
                btnPlayMain.setImageResource(R.drawable.ic_baseline_pause_24);
                String totalTime = createTimeLabel(mediaPlayer.getDuration());
                textViewEndDuration.setText(totalTime);
                textViewEndDurationMain.setText(totalTime);
                seekBar.setMax(mediaPlayer.getDuration());
                seekBarMain.setMax(mediaPlayer.getDuration());
                mediaPlayer.start();
                musicAdapter.setIndex(position,true);
                CreateNotification.createNotification(getActivity(),getMusicsList.get(position),R.drawable.ic_pause_black_24dp, position, getMusicsList.size()-1);
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
                } else {
                    curSongPoition = 0;
                    playingPosition = curSongPoition;
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
                    seekBarMain.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                btnPlayMain.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                btnPlay.setImageResource(R.drawable.ic_baseline_pause_24);
                btnPlayMain.setImageResource(R.drawable.ic_baseline_pause_24);
                progressBar.setVisibility(View.GONE);
            }
        });

        seekBarMain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    seekBar.setProgress(progress);
                    seekBarMain.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                btnPlayMain.setImageResource(R.drawable.ic_baseline_play_arrow_24);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                btnPlay.setImageResource(R.drawable.ic_baseline_pause_24);
                btnPlayMain.setImageResource(R.drawable.ic_baseline_pause_24);
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
        if (playingPosition <= 0) {
            playingPosition = getMusicsList.size() - 1;
        } else {
            playingPosition--;
        }
        initPlayer(playingPosition);
        CreateNotification.createNotification(getActivity(), getMusicsList.get(playingPosition), R.drawable.ic_pause_black_24dp, playingPosition, getMusicsList.size()-1);
        recyclerView.smoothScrollToPosition(playingPosition);
    }

    private void startAnimate()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (animator.isPaused()) {
                animator.resume();
            } else {
                animator.start();
            }}
    }

    private void pauseAnimate()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animator.pause();
        }
    }

    @Override
    public void onTrackPlay() {
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            btnPlay.setImageResource(R.drawable.ic_baseline_pause_24);
            btnPlayMain.setImageResource(R.drawable.ic_baseline_pause_24);
            musicAdapter.setIndex(playingPosition,true);
            startAnimate();
        }
        CreateNotification.createNotification(getActivity(), getMusicsList.get(playingPosition),
                R.drawable.ic_pause_black_24dp, playingPosition, getMusicsList.size()-1);
        isPlaying = true;
    }

    @Override
    public void onTrackPause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlay.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            btnPlayMain.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            musicAdapter.setIndex(playingPosition,false);
           pauseAnimate();
        }
        CreateNotification.createNotification(getActivity(), getMusicsList.get(playingPosition),R.drawable.ic_play_arrow_black_24dp, playingPosition, getMusicsList.size()-1);
        isPlaying = false;
    }

    @Override
    public void onTrackNext() {
        if (playingPosition < getMusicsList.size() - 1) {
            playingPosition++;
        } else {
            playingPosition = 0;
        }
        initPlayer(playingPosition);
        CreateNotification.createNotification(getActivity(), getMusicsList.get(playingPosition), R.drawable.ic_pause_black_24dp, playingPosition, getMusicsList.size()-1);
        recyclerView.smoothScrollToPosition(playingPosition);
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