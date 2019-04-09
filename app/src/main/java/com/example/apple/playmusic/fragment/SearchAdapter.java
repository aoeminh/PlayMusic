package com.example.apple.playmusic.fragment;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.model.Song;

import java.util.ArrayList;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>  {

    private Context context;
    private ArrayList<Song> songArrayList = new ArrayList<>();

    public SearchAdapter(Context context, ArrayList<Song> songs){
        this.context =context;
        this.songArrayList = songs;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_list_song_search,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tvSongName.setText(songArrayList.get(i).getSongName());
        viewHolder.tvSingerName.setText(songArrayList.get(i).getSinger());
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvSongName,tvSingerName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSingerName = itemView.findViewById(R.id.tv_singer_item_search);
            tvSongName = itemView.findViewById(R.id.tv_song_name_item_search);
        }
    }
}
