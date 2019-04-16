package com.example.apple.playmusic.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.activity.ListAlbumActivity;
import com.example.apple.playmusic.activity.ListThemeActivity;
import com.example.apple.playmusic.activity.SongListActivity;
import com.example.apple.playmusic.adapter.AlbumHotAdapter;
import com.example.apple.playmusic.contract.IPresenterCallback;
import com.example.apple.playmusic.contract.IViewCallback;
import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.Album;
import com.example.apple.playmusic.model.CategoryTheme;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.presenter.HomePresenter;

import java.util.ArrayList;

public class AlbumFragment extends Fragment implements IViewCallback,IOnItemClick {
    private View view;
    private IPresenterCallback presenter;
    private RecyclerView rvAlbumHot;
    private TextView tv_title;
    private ArrayList<Album> albumList = new ArrayList<>();
    private AlbumHotAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.album_fragment,container,false);
        initView(view);
        getData();
        return view;
    }


    private void initView(View view){
        rvAlbumHot = view.findViewById(R.id.rv_album_hot);
        tv_title = view.findViewById(R.id.tv_title_album_fragment);
        presenter = new HomePresenter(this);

        tv_title.setOnClickListener(view1 -> {
            Intent intent= new Intent(getActivity(), ListAlbumActivity.class);
            startActivity(intent);
        });

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

        albumList = albums;
        adapter = new AlbumHotAdapter(getActivity(),albumList,this::onClickItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAlbumHot.setAdapter(adapter);
        rvAlbumHot.setLayoutManager(layoutManager);
    }

    @Override
    public void responseLoveSong(ArrayList<Song> songs) {

    }

    void getData(){
        presenter.requestAlbumHot();
    }

    @Override
    public void onClickItem(int position) {

        Album album =albumList.get(position);
        Intent intent = new Intent(getActivity(),SongListActivity.class);
        intent.putExtra(ListAlbumActivity.EXTRA_ALBUM,album);
        startActivity(intent);
    }
}
