package com.example.khmer_music_library_player.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.khmer_music_library_player.Models.GetMusics;
import com.example.khmer_music_library_player.R;

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
        holder.textViewMusicTitle.setText("បទ: " + getMusics.getMusic());
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
        public MusicAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMusicTitle = itemView.findViewById(R.id.textViewMusicTitle);
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
