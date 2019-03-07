package com.example.apple.playmusic.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.adapter.AlbumHotAdapter;
import com.example.apple.playmusic.adapter.ListAlbumAdapter;
import com.example.apple.playmusic.contract.IAlbumPresenter;
import com.example.apple.playmusic.contract.IAlbumView;
import com.example.apple.playmusic.contract.IPresenterCallback;
import com.example.apple.playmusic.model.Album;
import com.example.apple.playmusic.presenter.ListAlbumPresenter;

import java.util.ArrayList;

public class ListAlbumActivity extends AppCompatActivity implements IAlbumView,IOnItemClick {

    public static  final String EXTRA_ALBUM= " extra.album";
    private IAlbumPresenter presenter;
    private RecyclerView rvAlbumHot;
    private TextView tvViewMore;
    private ArrayList<Album> albumList = new ArrayList<>();
    private ListAlbumAdapter adapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_album);
        initView();

        presenter = new ListAlbumPresenter(this);
        presenter.getAllAlbum();

    }

    void initView(){
        rvAlbumHot = findViewById(R.id.rv_list_album);
        toolbar = findViewById(R.id.toolbar_list_album);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> finish());

    }

    @Override
    public void onClickItem(int position) {
        Album album =albumList.get(position);
        Intent intent = new Intent(this,SongListActivity.class);
        intent.putExtra(EXTRA_ALBUM,album);
        startActivity(intent);
    }

    @Override
    public void responseAllAlbum(ArrayList<Album> albums) {
        albumList = albums;
        adapter = new ListAlbumAdapter(this,albumList,this::onClickItem);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAlbumHot.setLayoutManager(layoutManager);
        rvAlbumHot.setAdapter(adapter);
    }
}
