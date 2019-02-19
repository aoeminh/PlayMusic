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
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.action.IOnItemClickPlaylist;
import com.example.apple.playmusic.model.Playlist;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {

    private ArrayList<Playlist> playlists = new ArrayList<>();
    private Context context;
    private IOnItemClick mIOnItemClickPlaylist;
    public PlaylistAdapter(Context context,ArrayList<Playlist> playlists, IOnItemClick i){
        this.mIOnItemClickPlaylist =i;
        this.context = context;
        this.playlists = playlists;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_playlist,viewGroup,false);
        view.setOnClickListener(view1 -> {
            mIOnItemClickPlaylist.onClickItem(i);
        });
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Playlist playlist = playlists.get(i);
        viewHolder.tvPlaylistName.setText(playlist.getPlaylistName());
        Glide.with(context).load(playlist.getPlaylistImage()).into(viewHolder.imgBackground);
        Glide.with(context).load(playlist.getPlaylistIcon()).into(viewHolder.imgIcon);
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tvPlaylistName;
        private ImageView imgIcon,imgBackground;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvPlaylistName = itemView.findViewById(R.id.tv_title_item_playlist_fragment);
            imgBackground = itemView.findViewById(R.id.img_background_item_playlist_fragment);
            imgIcon = itemView.findViewById(R.id.img_icon_item_playlist_fragment);
        }
    }
}
