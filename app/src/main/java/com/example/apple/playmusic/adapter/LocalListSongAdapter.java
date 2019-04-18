package com.example.apple.playmusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.model.Song;

import java.util.ArrayList;

public class LocalListSongAdapter extends RecyclerView.Adapter<LocalListSongAdapter.ViewHolder> {

    private ArrayList<Song> listSong;
    private Context mContext;
    private IOnItemClick mIOnItemClick;

    public LocalListSongAdapter(Context context,ArrayList<Song> songs,IOnItemClick i){
        this.mContext = context;
        this.listSong = songs;
        this.mIOnItemClick = i;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_song_list_local,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvSongName.setText(listSong.get(i).getSongName());
        viewHolder.tvSingerName.setText(listSong.get(i).getSinger());
    }

    @Override
    public int getItemCount() {
        return listSong !=null ? listSong.size() : 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvSongName,tvSingerName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSingerName = itemView.findViewById(R.id.tv_singer_item_local);
            tvSongName = itemView.findViewById(R.id.tv_song_name_item_local);
            itemView.setOnClickListener(v -> mIOnItemClick.onClickItem(getAdapterPosition()));
        }
    }
}
