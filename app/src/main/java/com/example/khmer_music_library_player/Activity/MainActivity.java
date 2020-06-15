package com.example.khmer_music_library_player.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.khmer_music_library_player.Adapter.MusicAdapter;

import com.example.khmer_music_library_player.Models.CustomTypefaceSpan;
import com.example.khmer_music_library_player.Models.GetMusics;
import com.example.khmer_music_library_player.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Boolean checkIn =false;
    private List<GetMusics> getMusicsList;
    private MusicAdapter musicAdapter;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private JcPlayerView jcPlayerView;
    private ArrayList<JcAudio> jcAudiosList = new ArrayList<>();
    private int currentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void  initView()
    {
        recyclerView = findViewById(R.id.recyclerViewMusic);
        progressBar = findViewById(R.id.progressBarPlayer);
        jcPlayerView = findViewById(R.id.jcPlayerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getMusicsList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("Music").child("-M93w3-Vz3l4jTCUktkb").child("-M93wGgy5cFejwlFazHM").child("-M93wTsrYrj1KcM6V4oo").child("-M93wJHs-4CHdVP5PVWh");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                getMusicsList.clear();
                for (DataSnapshot dss : dataSnapshot.getChildren()){
                    GetMusics getMusics = dss.getValue(GetMusics.class);
//                  getMusics.setMusicKey(dss.getKey());
                    currentIndex = 0;

                    getMusicsList.add(getMusics);
                    checkIn = true;
                    jcAudiosList.add(JcAudio.createFromURL(getMusics.getMusic(),getMusics.uri));
                    Log.e("ooooooooo",getMusics.getMusic()+"");
                }
                musicAdapter = new MusicAdapter(getApplicationContext(), getMusicsList, new MusicAdapter.RecyclerItemClickListener() {
                    @Override
                    public void onClickListener(GetMusics getMusics, int position) {
                        changeSelectedSong(position);
                        jcPlayerView.playAudio(jcAudiosList.get(position));
                        jcPlayerView.createNotification();
                    }
                });
                recyclerView.setAdapter(musicAdapter);
                musicAdapter.setSelectedPosition(0);
                recyclerView.setAdapter(musicAdapter);
                musicAdapter.notifyDataSetChanged();
                if(checkIn)
                {
                    jcPlayerView.initPlaylist(jcAudiosList,null);
                }else{
                    Toast.makeText(MainActivity.this, "There are no musid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

}
