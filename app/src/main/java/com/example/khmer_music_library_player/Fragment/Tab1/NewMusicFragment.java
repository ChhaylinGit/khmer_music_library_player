package com.example.khmer_music_library_player.Fragment.Tab1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jean.jcplayer.model.JcAudio;
import com.example.jean.jcplayer.view.JcPlayerView;
import com.example.khmer_music_library_player.Activity.MainActivity;
import com.example.khmer_music_library_player.Adapter.MusicAdapter;
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
import java.util.List;


public class NewMusicFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_music, container, false);
        initView(view);
        return view;
    }

    private void  initView(View view)
    {
        recyclerView = view.findViewById(R.id.recyclerViewMusic);
        progressBar = view.findViewById(R.id.progressBarPlayer);
        jcPlayerView = view.findViewById(R.id.jcPlayerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                musicAdapter = new MusicAdapter(getActivity(), getMusicsList, new MusicAdapter.RecyclerItemClickListener() {
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
                    Toast.makeText(getActivity(), "There are no musid", Toast.LENGTH_SHORT).show();
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