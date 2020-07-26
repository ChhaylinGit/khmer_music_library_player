package com.example.khmer_music_library_player.Fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.transition.Fade;
import androidx.transition.Transition;
import androidx.transition.TransitionManager;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.khmer_music_library_player.Activity.MainActivity;
import com.example.khmer_music_library_player.Activity.Navegation_Drawer;
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
    public ImageView imgSingerProfileMain;
    public ArrayList<GetMusics> getMusicsList =new ArrayList<>();
    public MusicAdapter musicAdapter;
    SlidingUpPanelLayout slidingUpPanelLayout;
    CardView cardViewMediaPlayer;
    LinearLayout mediaContainerWithAds;

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
        imgSingerProfileMain = view.findViewById(R.id.imgSingerProfileMain);
        cardViewMediaPlayer = view.findViewById(R.id.cardviewPlayerMain);
        mediaContainerWithAds = view.findViewById(R.id.mediaContainerWithAds);
        slidingUpPanelLayout = view.findViewById(R.id.slideup_panel);
        thisView = view;
        viewPager = view.findViewById(R.id.viewpager);
        tabLayout = view.findViewById(R.id.tablayout);
        tabLayout.setupWithViewPager(viewPager);
        cardViewMediaPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
//                final Toolbar toolbar = ((Navegation_Drawer)getActivity()).findViewById(R.id.toolbar);
//                toolbar.setNavigationIcon(R.drawable.pause_96px);
//                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//                    }
//                });
            }
        });
        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if(newState.equals(SlidingUpPanelLayout.PanelState.DRAGGING))
                {
                    mediaContainerWithAds.setVisibility(View.GONE);
                }
                if(newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED))
                {
                    slideUp(mediaContainerWithAds);
                }else if(newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED))
                {
                    imgSingerProfileMain.setVisibility(View.VISIBLE);
                    mediaContainerWithAds.setVisibility(View.GONE);
                }
            }
        });
    }

    public void slideUp(View view){
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(0,0,view.getHeight(), 0);
        animate.setDuration(200);
        view.startAnimation(animate);
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
