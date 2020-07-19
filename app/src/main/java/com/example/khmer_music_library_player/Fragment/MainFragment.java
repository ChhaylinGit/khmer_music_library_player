package com.example.khmer_music_library_player.Fragment;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.khmer_music_library_player.Adapter.MusicAdapter;
import com.example.khmer_music_library_player.Fragment.Tab1.MusicListFragment;
import com.example.khmer_music_library_player.Fragment.Tab1.NewMusicFragment;
import com.example.khmer_music_library_player.Models.GetMusics;
import com.example.khmer_music_library_player.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

//    private BottomNavigationView bottomNavigationView;
    private AdView adView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArrayList<GetMusics> getMusicsList = new ArrayList<>();
    private MusicAdapter musicAdapter;
    private CardView cardView;
    private int[] tabIcons = {
            R.drawable.bottom_nav_music,
            R.drawable.bottom_nav_singer,
            R.drawable.bottom_nav_music_list,
            R.drawable.bottom_nav_download
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
//      openFragment(new MusicListFragment());
//      loadFragment(new NewMusicFragment());
        setUpviewPager();
        setCustomFont();
        return view;
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void  setUpviewPager()
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager(),1);
        adapter.addFragment(new NewMusicFragment(),getResources().getText(R.string.music).toString());
        adapter.addFragment(new SingerFragment(),getResources().getText(R.string.singer).toString());
        adapter.addFragment(new PlaylistFragment(),getResources().getText(R.string.music_list).toString());
        adapter.addFragment(new DownloadFragment(),getResources().getText(R.string.download).toString());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment,String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void initView(View view)
    {
//        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
//        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        adView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        cardView = view.findViewById(R.id.cardviewMain);
    }

    public void showMediaPlayer()
    {
        cardView.setVisibility(View.VISIBLE);
    }



//    private void setUpMediaplayer()
//    {
//        musicAdapter = new MusicAdapter(getActivity(), getMusicsList, new MusicAdapter.RecyclerItemClickListener() {
//            @Override
//            public void onClickListener(GetMusics getMusics, int position) {
//                cardviewMain.setVisibility(View.VISIBLE);
//                playingPosition = position;
//                notifposition = position;
//                initPlayer(playingPosition);
//
//            }
//        });
//    }


        private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId())
                {
                    case R.id.bottom_navigation_music:
                        fragment = new NewMusicFragment();
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

    public void setCustomFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    //Put your font in assests folder
                    //assign name of the font here (Must be case sensitive)
                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "font/khmer_os_battambang.ttf"));
                }
            }
        }
    }


}
