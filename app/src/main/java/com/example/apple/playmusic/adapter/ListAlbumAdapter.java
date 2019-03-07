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
import com.example.apple.playmusic.model.Album;

import java.util.ArrayList;



public class ListAlbumAdapter extends RecyclerView.Adapter<ListAlbumAdapter.ViewHolder> {
    private ArrayList<Album> albums = new ArrayList<>();
    private Context context;
    private IOnItemClick iOnItemClick;
    public ListAlbumAdapter(Context context, ArrayList<Album> a, IOnItemClick i){
        this.context = context;
        this.albums = a;
        this.iOnItemClick = i;

    }
    @NonNull
    @Override
    public ListAlbumAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_list_album_activity,viewGroup,false);
        return new ListAlbumAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Album album = albums.get(i);
        Glide.with(context).load(album.getAlbumImage()).into(viewHolder.imAlbum);
        viewHolder.tvSinger.setText(album.getSingerName());
        viewHolder.tvTittle.setText(album.getAlbumName());
    }

    @Override
    public int getItemCount() {
        return albums.size() ;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView imAlbum;
        private TextView tvTittle,tvSinger;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imAlbum = itemView.findViewById(R.id.im_row_item_list_album);
            tvTittle = itemView.findViewById(R.id.tv_tittle_row_item_list_album);
            tvSinger = itemView.findViewById(R.id.tv_singer_row_item_list_album);

            itemView.setOnClickListener(view -> iOnItemClick.onClickItem(getAdapterPosition()));
        }
    }
}
