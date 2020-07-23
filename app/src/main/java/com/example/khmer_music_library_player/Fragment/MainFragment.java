package com.example.khmer_music_library_player.Fragment;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khmer_music_library_player.Adapter.MusicAdapter;
import com.example.khmer_music_library_player.Fragment.Tab1.MusicListFragment;
import com.example.khmer_music_library_player.Fragment.Tab1.NewMusicFragment;
import com.example.khmer_music_library_player.Models.GetMusics;
import com.example.khmer_music_library_player.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {

    private AdView adView;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public CardView cardView;
    private View thisView;
    private Context context;
    private int[] tabIcons = {
            R.drawable.bottom_nav_music,
            R.drawable.bottom_nav_singer,
            R.drawable.bottom_nav_music_list,
            R.drawable.bottom_nav_download
    };
    public int currentIndex;
    public LinearLayout linearLayoutWaitLoadMusic;
    public MediaPlayer mediaPlayer;
    public SeekBar seekBar;
    public FloatingActionButton btnPlay,btnNext,btnPrevious;
    public TextView textViewStartDuration,textViewEndDuration,textViewMusicTitle,textViewSinger;
    public int playingPosition=0;
    public NotificationManager notificationManager;
    public ImageView imageView;
    public ArrayList<GetMusics> getMusicsList =new ArrayList<>();
    public MusicAdapter musicAdapter;
    SlidingUpPanelLayout slidingUpPanelLayout;
    CardView musicContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View  view = inflater.inflate(R.layout.fragment_main, container, false);
        initView(view);
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
        adapter.addFragment(new NewMusicFragment(getActivity()),getResources().getText(R.string.music).toString());
        adapter.addFragment(new SingerFragment(),getResources().getText(R.string.singer).toString());
        adapter.addFragment(new PlaylistFragment(),getResources().getText(R.string.music_list).toString());
        adapter.addFragment(new DownloadFragment(),getResources().getText(R.string.download).toString());
        viewPager.setOffscreenPageLimit(adapter.getCount());
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
        slidingUpPanelLayout = view.findViewById(R.id.slideup_panel);
        musicContainer = view.findViewById(R.id.cardviewPlayerMain);
        thisView = view;
//        adView = view.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);


        musicContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                musicContainer.setVisibility(View.GONE);
            }
        });
//        cardView = view.findViewById(R.id.cardviewPlayer);

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
