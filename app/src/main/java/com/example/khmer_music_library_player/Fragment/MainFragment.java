package com.example.khmer_music_library_player.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.khmer_music_library_player.Fragment.Tab1.MusicListFragment;
import com.example.khmer_music_library_player.Fragment.Tab2.SingerFragment;
import com.example.khmer_music_library_player.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainFragment extends Fragment {

    private BottomNavigationView bottomNavigationView;
    private AdView adView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
//        openFragment(new MusicListFragment());
        loadFragment(new MusicListFragment());
        return view;
    }



    private void initView(View view)
    {
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


        private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId())
                {
                    case R.id.bottom_navigation_music:
                        fragment = new MusicListFragment();
                        break;
                    case R.id.bottom_navigation_singer:
                        fragment = new SingerFragment();
                        break;
                }
                return loadFragment(fragment);
            }
        };


    public void openFragment(Fragment fragment) {
        FragmentTransaction transaction =  getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.musicContainer, fragment);
        transaction.commit();
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.musicContainer, fragment)
                    .commit();
            return true;
        }
        return false;
    }


}
