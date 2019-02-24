package com.example.apple.playmusic.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.adapter.ListPlaylistApdater;
import com.example.apple.playmusic.contract.IListPlaylistView;
import com.example.apple.playmusic.contract.IListplaylistPresenter;
import com.example.apple.playmusic.fragment.DayPlaylistFragment;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.presenter.ListPlaylistPresenter;

import java.util.ArrayList;
import java.util.Objects;

public class ListPlaylistActivity extends AppCompatActivity implements IListPlaylistView, IOnItemClick {

    public static final String ACTION_PLAYLIST = "action.playlist";
    public static final String EXTRA_PLAYLIST = "data.playlist";

    private RecyclerView rvListPlaylist;
    private Toolbar toolbar;
    private ArrayList<Playlist> playlistArrayList ;
    private IListplaylistPresenter presenter;
    private ListPlaylistApdater adapter;
    private ArrayList<Playlist> playlists;
    private ListPlaylistApdater apdater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_playlist);
        initView();
        getData();
    }

    private void initView(){
        rvListPlaylist = findViewById(R.id.rv_list_playlist);
        toolbar = findViewById(R.id.toolbar_list_playlist);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

    }

    private  void getData(){
        presenter = new ListPlaylistPresenter(this);
        presenter.getAllPlaylist();;
    }

    @Override
    public void responseAllPlaylist(ArrayList<Playlist> allPlaylist) {
        playlistArrayList = allPlaylist;
        adapter = new ListPlaylistApdater(this,allPlaylist,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this  );
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvListPlaylist.setLayoutManager(layoutManager);
        rvListPlaylist.setAdapter(adapter);

    }

    @Override
    public void onClickItem(int position) {
        Playlist playlist = playlistArrayList.get(position);
        Intent intent = new Intent(this,SongListActivity.class);
        intent.putExtra(DayPlaylistFragment.EXTRA_PLAYLIST,playlist);
        intent.setAction(DayPlaylistFragment.ACTION_PLAYLIST);
        startActivity(intent);

    }
}
