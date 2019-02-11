package com.example.apple.playmusic.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apple.playmusic.R;
import com.example.apple.playmusic.model.Advertise;

import java.util.ArrayList;

public class AdvertiseAdapter extends PagerAdapter {

    ArrayList<Advertise> advertiseArrayList = new ArrayList<>();
    Context context;

    public AdvertiseAdapter(ArrayList<Advertise> advertiseArrayList, Context context) {
        this.advertiseArrayList = advertiseArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return advertiseArrayList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {

        return view==o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row_item_advertise,container,false);

        ImageView imgBackground = view.findViewById(R.id.img_background_banner);
        ImageView imgSong = view.findViewById(R.id.img_icon_row_item_banner);
        TextView tvSongName = view.findViewById(R.id.tv_song_name);
        TextView tvSongContent = view.findViewById(R.id.tv_song_content);

        Advertise advertise = advertiseArrayList.get(position);
        //set data into view
        Glide.with(context).load(advertise.getAdImage()).into(imgBackground);
        Glide.with(context).load(advertise.getSongImage()).into(imgSong);
        tvSongName.setText(advertise.getSongName());
        tvSongContent.setText(advertise.getAdvContent());
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
