package com.example.apple.playmusic.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.apple.playmusic.R;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.adapter.SongListAdapter;
import com.example.apple.playmusic.contract.ISongListPresenter;
import com.example.apple.playmusic.contract.ISongListViewCallback;
import com.example.apple.playmusic.fragment.Fragment_Banner;
import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.presenter.SongListPresenter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import retrofit2.http.Url;

public class SongListActivity extends AppCompatActivity implements ISongListViewCallback,IOnItemClick{

    private RecyclerView rvSonglist;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private Advertise advertise;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton btnPlay;
    private CoordinatorLayout coordinatorLayout;
    private ISongListPresenter presenter;
    private ImageView songAvatar;
    private ArrayList<Song> songList;
    private SongListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        initView();
        initToolbar();
        if(getIntent() != null){
            getDataIntent();
            setViewCollaplayout(advertise.getSongName(),advertise.getAdImage(),advertise.getSongImage());
        }

    }

    private void initView(){
        coordinatorLayout = findViewById(R.id.coordinator_songlist);
        collapsingToolbarLayout = findViewById(R.id.collap_songlist);
        rvSonglist = findViewById(R.id.rv_songlist);
        btnPlay = findViewById(R.id.float_btn_songlist);
        toolbar = findViewById(R.id.tool_bar_songlist);
        songAvatar = findViewById(R.id.im_songlist_ava);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        presenter = new SongListPresenter(this);



    }

    private void setViewCollaplayout(String songName,String advImage,String songImage){
        collapsingToolbarLayout.setTitle(songName);
            GetImageFromUrlAsync getImageFromUrlAsync = new GetImageFromUrlAsync();
            getImageFromUrlAsync.execute(advImage);
            Glide.with(this).load(advImage).into(songAvatar);
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });

    }
    private void getDataIntent(){
         advertise = (Advertise) getIntent().getSerializableExtra(Fragment_Banner.EXTRA_ADVERTISE);
         presenter.songlistRequest(String.valueOf(advertise.getAdID()));
    }

    @Override
    public void songListResponse(ArrayList<Song> songs) {
        songList = songs;
        adapter = new SongListAdapter(this,songList,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSonglist.setAdapter(adapter);
        rvSonglist.setLayoutManager(layoutManager);

    }

    @Override
    public void onClickItem(int position) {
        Song song =songList.get(position);
        Glide.with(this).load(song.getSongImage()).into(songAvatar);

    }

    class GetImageFromUrlAsync  extends AsyncTask<String,Void,Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {
            String strUrl = strings[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(strUrl);
                 bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(),bitmap);
            collapsingToolbarLayout.setBackground(bitmapDrawable);
        }
    }
}