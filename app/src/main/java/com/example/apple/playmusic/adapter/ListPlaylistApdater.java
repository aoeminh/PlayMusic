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
import com.example.apple.playmusic.model.Playlist;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListPlaylistApdater extends RecyclerView.Adapter<ListPlaylistApdater.ViewHolder> {

    private Context context;
    private ArrayList<Playlist> playlists;
    private IOnItemClick iOnItemClick;

    public ListPlaylistApdater(Context context,ArrayList<Playlist> playlists, IOnItemClick i){
        this.context = context;
        this.iOnItemClick =i;
        this.playlists = playlists;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(context).inflate(R.layout.row_item_list_playlist,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Playlist playlist= playlists.get(i);
        viewHolder.tvPlaylistName.setText(playlist.getPlaylistName());
        Glide.with(context).load(playlist.getPlaylistImage()).into(viewHolder.imgThumnail);
        viewHolder.tvNumber.setText(playlist.getSongNumber());
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgMore,imgThumnail;
        private TextView tvPlaylistName,tvNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgMore = itemView.findViewById(R.id.im_item_list_playlist_more);
            imgThumnail = itemView.findViewById(R.id.im_item_list_playlist_thumnail);
            tvNumber = itemView.findViewById(R.id.tv_singer_item_list_playlist);
            tvPlaylistName = itemView.findViewById(R.id.tv_name_playlist);
            itemView.setOnClickListener(view -> {
                iOnItemClick.onClickItem(getAdapterPosition());
            });
        }
    }
}
