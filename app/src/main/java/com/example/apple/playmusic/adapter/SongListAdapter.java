package com.example.apple.playmusic.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.model.Song;

import java.util.ArrayList;

public class SongListAdapter extends RecyclerView.Adapter<SongListAdapter.ViewHolder>{

    private ArrayList<Song> songs = new ArrayList<>();
    private Context context;
    private IOnItemClick iOnItemClick;
    private OnOptionClick onOptionClick;
    public SongListAdapter(Context context,ArrayList<Song>songs,IOnItemClick itemClick,OnOptionClick onOptionClick){
        this.iOnItemClick = itemClick;
        this.context = context;
        this.songs = songs;
        this.onOptionClick = onOptionClick;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_item_song_list,viewGroup,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Song song = songs.get(i);

        viewHolder.tvSongname.setText(song.getSongName());
        viewHolder.tvNumber.setText(String.valueOf(i+1));
        viewHolder.tvSinger.setText(song.getSinger());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvSongname,tvSinger,tvNumber;
        public ImageView imLike,imOption;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvSongname = itemView.findViewById(R.id.tv_song_name_item_songlist);
            tvNumber = itemView.findViewById(R.id.tv_number_item_song_list);
            tvSinger = itemView.findViewById(R.id.tv_singer_item_songlist);
            imLike = itemView.findViewById(R.id.im_like_item_list__song);
            imOption = itemView.findViewById(R.id.im_option_item_list__song);
            itemView.setOnClickListener(view1 -> {
                iOnItemClick.onClickItem(getAdapterPosition());
            });
            imOption.setOnClickListener(view -> {
                onOptionClick.onOptionClick(getAdapterPosition());
            });


        }
    }




    public interface OnOptionClick{
        void onOptionClick(int position);
    }
}
