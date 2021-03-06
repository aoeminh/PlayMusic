package com.example.apple.playmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ViewFlipper;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.activity.SongListActivity;
import com.example.apple.playmusic.adapter.AdvertiseAdapter;
import com.example.apple.playmusic.contract.IPresenterCallback;
import com.example.apple.playmusic.contract.IViewCallback;
import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.Album;
import com.example.apple.playmusic.model.CategoryTheme;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.presenter.HomePresenter;
import com.example.apple.playmusic.service.APIService;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Banner extends Fragment implements IViewCallback,IOnItemClick {

    public static final String EXTRA_ADVERTISE="advertise";
    public static final String ACTION_ADVERTISE = "action_advertise";
    IPresenterCallback homePresenter;
    ViewPager viewPager;
    CircleIndicator circleIndicator;
    AdvertiseAdapter adapter ;
    Handler handler;
    Runnable runnable;
    int currentItem;
    private ArrayList<Advertise> advertiseList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_banner,container,false);
        initView(view);
        getData();
        return view;
    }


    private void getData(){
        homePresenter.requestBannerHome();
    }

    private void initView(View view){
        viewPager = view.findViewById(R.id.view_fliper_banner);
        circleIndicator = view.findViewById(R.id.indicator_banner);
        homePresenter = new HomePresenter(this);
    }

    @Override
    public void responseBanner(ArrayList<Advertise> advertises) {
        advertiseList = advertises;
        adapter = new AdvertiseAdapter(advertiseList,getActivity(),this);
        viewPager.setAdapter(adapter);
        circleIndicator.setViewPager(viewPager);
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                currentItem = viewPager.getCurrentItem();
                currentItem++;
                if(currentItem > adapter.getCount()){
                    currentItem=0;
                }
                viewPager.setCurrentItem(currentItem);
                handler.postDelayed(runnable,4000);
            }
        };
        handler.postDelayed(runnable,4000);
    }

    @Override
    public void responsePlaylist(ArrayList<Playlist> playlists) {

    }

    @Override
    public void responseCategoryTheme(CategoryTheme categoryThemes) {

    }

    @Override
    public void responseAlbumHot(ArrayList<Album> albums) {

    }

    @Override
    public void responseLoveSong(ArrayList<Song> songs) {

    }

    @Override
    public void onClickItem(int position) {

        Intent intent =new Intent(getActivity(), SongListActivity.class);
        intent.putExtra(EXTRA_ADVERTISE,advertiseList.get(position));
        intent.setAction(ACTION_ADVERTISE);
        getActivity().startActivity(intent);

    }
}
