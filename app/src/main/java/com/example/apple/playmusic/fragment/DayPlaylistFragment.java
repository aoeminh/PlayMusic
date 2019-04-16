package com.example.apple.playmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.activity.ListPlaylistActivity;
import com.example.apple.playmusic.activity.SongListActivity;
import com.example.apple.playmusic.adapter.PlaylistAdapter;
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
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public  class DayPlaylistFragment extends Fragment implements IViewCallback, IOnItemClick {

    public  static final String EXTRA_PLAYLIST =  "playlist";
    public  static final String ACTION_PLAYLIST =  "action_playlist";
    private IPresenterCallback presenterCallback;
    private View view;
    private TextView tvTitle,tvViewMore;
    private RecyclerView rv_playlist;
    private PlaylistAdapter adapter;
    private ArrayList<Playlist> playlistArrayList = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_day_playlist,container,false);
        initView(view);

        getData();
        setAction();

        return view;
    }

    private void getData() {

        presenterCallback.requestDayPlaylist();
    }

    private void initView(View view){
        tvTitle = view.findViewById(R.id.tv_title_playlist);
        rv_playlist = view.findViewById(R.id.rv_day_playlist);
        presenterCallback = new HomePresenter(this);
    }

    private void setAction(){
        tvTitle.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity(), ListPlaylistActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void responseBanner(ArrayList<Advertise> advertises) {

    }

    @Override
    public void responsePlaylist(ArrayList<Playlist> playlists) {
        playlistArrayList = playlists;
        adapter = new PlaylistAdapter(getActivity(),playlistArrayList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_playlist.setLayoutManager(layoutManager);
        rv_playlist.setAdapter(adapter);
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
        Playlist playlist = playlistArrayList.get(position);
        Intent intent = new Intent(getActivity(), SongListActivity.class);
        intent.putExtra(EXTRA_PLAYLIST,playlist);
        intent.setAction(ACTION_PLAYLIST);
        Objects.requireNonNull(getActivity()).startActivity(intent);
    }
}
