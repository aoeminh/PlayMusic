package com.example.apple.playmusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apple.playmusic.R;
import com.example.apple.playmusic.model.Song;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoveSongAdapter  extends RecyclerView.Adapter<LoveSongAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Song>  songs = new ArrayList<>();
    public  LoveSongAdapter(Context context,ArrayList<Song> songs){
        this.context = context;
        this.songs = songs;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_love_song,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Song song = songs.get(i);
        viewHolder.tvSongName.setText(song.getSongName());
        viewHolder.tvSinger.setText(song.getSinger());
        Glide.with(context).load(song.getSongImage()).into(viewHolder.songImage);
    }

    @Override
    public int getItemCount() {
        return songs!=null ? songs.size():0;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private CircleImageView songImage;
        private ImageView imLike;
        private TextView tvSongName,tvSinger;
        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSinger =itemView.findViewById(R.id.tv_singer_love_song);
            tvSongName = itemView.findViewById(R.id.tv_name_love_song);
            songImage= itemView.findViewById(R.id.im_ava_love_song);
            imLike = itemView.findViewById(R.id.im_like_love_song_fragment);
        }
    }
}
