package com.example.khmer_music_library_player.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.khmer_music_library_player.Models.GetMusics;
import com.example.khmer_music_library_player.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicAdapterViewHolder> {
    Context context;
    List<GetMusics> getMusicsList;
    private RecyclerItemClickListener itemClickListener;
    private int selectedPosition;

    public MusicAdapter(Context context, List<GetMusics> getMusicsList, RecyclerItemClickListener itemClickListener) {
        this.context = context;
        this.getMusicsList = getMusicsList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MusicAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_layout_music_list,parent,false);
        return new MusicAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapterViewHolder holder, int position) {
        GetMusics getMusics = getMusicsList.get(position);
        holder.textViewMusicTitle.setText(getMusics.getMusic());

        Picasso.get().load("https://firebasestorage.googleapis.com/v0/b/khmer-music-library.appspot.com/o/Singer%2FAlbums%2F%E1%9E%86%E1%9E%B6%E1%9E%99%20%E1%9E%9B%E1%9E%B8%E1%9E%93_1592320097999.jpg?alt=media&token=953c5b79-67ed-4265-83f7-a7e4185bcb7e").placeholder(R.drawable.version).into(holder.singerProfile);
        if(getMusics != null)
        {
            if(selectedPosition == position)
            {

            }
        }
        holder.bind(getMusics,itemClickListener);
    }

    @Override
    public int getItemCount() {
        Log.e("oooooooosize",getMusicsList.size()+"");
        return getMusicsList.size();
    }


    public class MusicAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewMusicTitle;
        private ImageView singerProfile;
        public MusicAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMusicTitle = itemView.findViewById(R.id.textViewMusicTitle);
            singerProfile = itemView.findViewById(R.id.imgSingerProfile);
        }

        public void bind(final GetMusics getMusics, final RecyclerItemClickListener itemClickListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClickListener(getMusics,getAdapterPosition());
                }
            });
        }
    }

    public interface RecyclerItemClickListener {
        void onClickListener(GetMusics getMusics,int position);
    }

    public int getSelectedPosition(){
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition)
    {
        this.selectedPosition = selectedPosition;
    }
}
