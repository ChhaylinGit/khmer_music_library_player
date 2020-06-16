package com.example.khmer_music_library_player.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.khmer_music_library_player.Fragment.Tab1.MusicListFragment;
import com.example.khmer_music_library_player.Fragment.Tab2.SingerFragment;
import com.example.khmer_music_library_player.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
        openFragment(new MusicListFragment());
        return view;
    }

    private void initView(View view)
    {
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
    }


        private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.bottom_navigation_music:
                        openFragment(new MusicListFragment());
                        return  true;
                    case R.id.bottom_navigation_singer:
                        openFragment(new SingerFragment());
                        return  true;
                }
                return false;
            }
        };


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction =  getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.musicContainer, fragment);
        transaction.commit();
    }



}
