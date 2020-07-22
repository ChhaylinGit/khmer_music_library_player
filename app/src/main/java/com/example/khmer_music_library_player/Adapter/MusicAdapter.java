package com.example.khmer_music_library_player.Adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.khmer_music_library_player.Models.GetMusics;
import com.example.khmer_music_library_player.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicAdapterViewHolder> {
    Context context;
    List<GetMusics> getMusicsList;
    private RecyclerItemClickListener itemClickListener;
    private int selectedPosition;
    private int index=-1;
    private boolean _showPlayBar;

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
    public void onBindViewHolder(@NonNull MusicAdapterViewHolder holder, final int position) {
        final GetMusics getMusics = getMusicsList.get(position);
        holder.textViewMusicTitle.setText(getMusics.getMusicTitle());
        holder.textViewSinger.setText(context.getResources().getString(R.string.sing_by)+" "+getMusics.getSingerName());
        holder.textViewMusicDuration.setText(getMusics.getDuration());
        if(getMusics.getSingerImageUrl() != null)
        {
            Picasso.get().load(getMusics.getSingerImageUrl()).placeholder(R.drawable.ic_image_black_24dp).into(holder.singerProfile);
        }else{Picasso.get().load(R.drawable.ic_image_black_24dp).placeholder(R.drawable.ic_image_black_24dp).into(holder.singerProfile);}
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClickListener(getMusics,position);
                index = position;
                notifyDataSetChanged();
                notifyItemMoved(position,0);
            }
        });
        if(index == position)
        {
            holder.textViewMusicTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.textViewSinger.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            if(_showPlayBar)
            {
                holder.imageViewGif.setVisibility(View.VISIBLE);
                Glide.with(context).load(R.drawable.playing_bar).into(holder.imageViewGif);
            }else{
                    holder.imageViewGif.setVisibility(View.GONE);
                }
        }else{
            holder.textViewMusicTitle.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.textViewSinger.setTextColor(context.getResources().getColor(R.color.colorBlack));
            holder.imageViewGif.setVisibility(View.GONE);
        }
    }

    public void  setIndex(int position,boolean showPlayBar)
    {
        _showPlayBar = showPlayBar;
        index = position;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        Log.e("oooooooosize",getMusicsList.size()+"");
        return getMusicsList.size();
    }

    public class MusicAdapterViewHolder extends RecyclerView.ViewHolder{
        private TextView textViewMusicTitle,textViewSinger,textViewMusicDuration;
        private ImageView singerProfile,imageViewGif;
        public MusicAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMusicTitle = itemView.findViewById(R.id.textViewMusicTitle);
            textViewSinger =itemView.findViewById(R.id.textViewSinger);
            textViewMusicDuration = itemView.findViewById(R.id.textViewMusicDuration);
            singerProfile = itemView.findViewById(R.id.imgSingerProfile);
            imageViewGif = itemView.findViewById(R.id.imagePlayingGif);
        }

        public void bind(final GetMusics getMusics, final RecyclerItemClickListener itemClickListener,final MusicAdapterViewHolder holder,final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClickListener(getMusics,getAdapterPosition());
                    index = position;
                    if(index == getAdapterPosition())
                    {
                        holder.textViewMusicTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        Toast.makeText(context, "Equal", Toast.LENGTH_SHORT).show();
                    }else{
                        holder.textViewMusicTitle.setTextColor(context.getResources().getColor(R.color.colorBlack));
                        Toast.makeText(context, "Not Equal", Toast.LENGTH_SHORT).show();
                    }
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
