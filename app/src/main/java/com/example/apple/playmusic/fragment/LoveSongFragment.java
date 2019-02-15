package com.example.apple.playmusic.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.adapter.LoveSongAdapter;
import com.example.apple.playmusic.contract.IPresenterCallback;
import com.example.apple.playmusic.contract.IViewCallback;
import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.Album;
import com.example.apple.playmusic.model.CategoryTheme;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.presenter.HomePresenter;

import java.util.ArrayList;

public class LoveSongFragment extends Fragment implements IViewCallback{

    View view;
    private RecyclerView rvLoveSong;
    private IPresenterCallback presenter;
    private LoveSongAdapter adapter;
    private ArrayList<Song> songs = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.love_song_fragment,container,false);
        initView(view);
        getData();
        return view;
    }

    private void initView(View view){
        rvLoveSong=view.findViewById(R.id.rv_love_song);
        presenter = new HomePresenter(this);
    }

    @Override
    public void responseBanner(ArrayList<Advertise> advertises) {

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
    public void responseLoveSong(ArrayList<Song> songs1) {
        songs=songs1;
        adapter = new LoveSongAdapter(getActivity(),songs);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvLoveSong.setLayoutManager(layoutManager);
        rvLoveSong.setAdapter(adapter);

    }

    public void getData() {

        presenter.requestLoveSong();
    }
}
