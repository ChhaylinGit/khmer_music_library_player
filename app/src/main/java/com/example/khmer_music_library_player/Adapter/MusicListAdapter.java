package com.example.khmer_music_library_player.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.khmer_music_library_player.Models.GetMusics;
import com.example.khmer_music_library_player.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MusicListAdapter extends BaseAdapter {
    private List<GetMusics> getMusicsList;
    private int selectedPosition;

    public MusicListAdapter(List<GetMusics> getMusics)
    {
        this.getMusicsList = getMusics;
    }
    @Override
    public int getCount() {
        return getMusicsList.size();
    }

    @Override
    public Object getItem(int i) {
        return getMusicsList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View  view1 = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_layout_music_list,viewGroup,false);
        GetMusics getMusics = getMusicsList.get(i);
        TextView textViewMusicTitle = view1.findViewById(R.id.textViewMusicTitle);
        ImageView imgSingerProfile = view1.findViewById(R.id.imgSingerProfile);
        textViewMusicTitle.setText(getMusics.getMusicTitle());
        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/khmer-music-library.appspot.com/o/Singer%2FAlbums%2F%E1%9E%86%E1%9E%B6%E1%9E%99%20%E1%9E%9B%E1%9E%B8%E1%9E%93_1592320097999.jpg?alt=media&token=953c5b79-67ed-4265-83f7-a7e4185bcb7e").placeholder(R.drawable.version).into(imgSingerProfile);
        return view1;
    }
    public int getSelectedPosition(){
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition)
    {
        this.selectedPosition = selectedPosition;
    }
}
