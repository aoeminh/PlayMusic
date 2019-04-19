package com.example.apple.playmusic.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apple.playmusic.R;
import com.example.apple.playmusic.Ultils.DownLoadFromUrl;
import com.example.apple.playmusic.Ultils.GetImageFromUrl;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.adapter.SongListAdapter;
import com.example.apple.playmusic.contract.ISongListPresenter;
import com.example.apple.playmusic.contract.ISongListViewCallback;
import com.example.apple.playmusic.fragment.CategoryThemeFragment;
import com.example.apple.playmusic.fragment.DayPlaylistFragment;
import com.example.apple.playmusic.fragment.Fragment_Banner;
import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.Album;
import com.example.apple.playmusic.model.Category;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.model.Theme;
import com.example.apple.playmusic.presenter.SongListPresenter;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import retrofit2.http.Url;

public class SongListActivity extends AppCompatActivity implements ISongListViewCallback, IOnItemClick,
        GetImageFromUrl.IOnGetBitmap, SongListAdapter.OnOptionClick {

    private RecyclerView rvSonglist;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private Advertise advertise;
    private Playlist mPlaylist;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private FloatingActionButton btnPlay;
    private CoordinatorLayout coordinatorLayout;
    private ISongListPresenter presenter;
    private ImageView songAvatar;
    private ArrayList<Song> songList;
    private SongListAdapter adapter;
    private Theme theme;
    private Category category;
    private Album album;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list);
        initView();
        initToolbar();
        getDataIntent();
    }

    private void initView() {
        coordinatorLayout = findViewById(R.id.coordinator_songlist);
        collapsingToolbarLayout = findViewById(R.id.collap_songlist);
        rvSonglist = findViewById(R.id.rv_songlist);
        btnPlay = findViewById(R.id.float_btn_songlist);
        toolbar = findViewById(R.id.tool_bar_songlist);
        songAvatar = findViewById(R.id.im_songlist_ava);
        collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppBar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppBar);
        presenter = new SongListPresenter(this);

        btnPlay.setOnClickListener(view -> {
            if(songList !=null && songList.size() >0){
                Intent intent = new Intent(this,PlayMusicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("song", songList);
                bundle.putInt("position",0);
                intent.putExtras(bundle);
                startActivity(intent);
            }else {
                Toast.makeText(this,"No song in playlist",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void setViewCollaplayout(String name, String image) {
        collapsingToolbarLayout.setTitle(name);
//        GetImageFromUrlAsync getImageFromUrlAsync = new GetImageFromUrlAsync();
        GetImageFromUrl getImageFromUrl = new GetImageFromUrl(this);
        getImageFromUrl.execute(image);
        Glide.with(this).load(image).into(songAvatar);
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(view -> {
            finish();
        });
    }

    private void getDataIntent() {
        if (getIntent() != null) {
            Intent intent = getIntent();
            if (intent.hasExtra(Fragment_Banner.EXTRA_ADVERTISE)) {
                advertise = (Advertise) getIntent().getSerializableExtra(Fragment_Banner.EXTRA_ADVERTISE);
                presenter.songlistRequest(String.valueOf(advertise.getAdID()));
                setViewCollaplayout(advertise.getSongName(), advertise.getAdImage());
            } else if (intent.hasExtra(DayPlaylistFragment.EXTRA_PLAYLIST)) {
                mPlaylist = (Playlist) intent.getSerializableExtra(DayPlaylistFragment.EXTRA_PLAYLIST);
                presenter.songlistFromPlaylist(String.valueOf(mPlaylist.getPlaylistId()));
                setViewCollaplayout(mPlaylist.getPlaylistName(), mPlaylist.getPlaylistImage());
            } else if (intent.hasExtra(CategoryThemeFragment.EXTRA_THEME) ) {
                theme = intent.getParcelableExtra(CategoryThemeFragment.EXTRA_THEME);
                presenter.songlistFromTheme(String.valueOf(theme.getIdChude()));
                setViewCollaplayout(theme.getTenChude(), theme.getHinhChude());
            } else if (intent.hasExtra(CategoryThemeFragment.EXTRA_CATEGORY)) {
                category = intent.getParcelableExtra(CategoryThemeFragment.EXTRA_CATEGORY);
                presenter.songlistFromCategory(String.valueOf(category.getIdCategory()));
                setViewCollaplayout(category.getCategoryName(), category.getCategoryImage());
            }else if(intent.hasExtra(ListAlbumActivity.EXTRA_ALBUM)){
                album = intent.getParcelableExtra(ListAlbumActivity.EXTRA_ALBUM);
                presenter.songListFromAlbum(String.valueOf(album.getAlbumID()));
                setViewCollaplayout(album.getAlbumName(),album.getAlbumImage());
            }
        }
    }

    void setValueRecyclerView(ArrayList<Song> songListrv){
        songList = songListrv;
        adapter = new SongListAdapter(this, songList, this,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSonglist.setAdapter(adapter);
        rvSonglist.setLayoutManager(layoutManager);
    }

    @Override
    public void songListResponse(ArrayList<Song> songs) {
        setValueRecyclerView(songs);
    }

    @Override
    public void songListFromPlaylistResponse(ArrayList<Song> songs) {
        setValueRecyclerView(songs);
    }

    @Override
    public void songlistFromThemeResponse(ArrayList<Song> songs) {
        setValueRecyclerView(songs);
    }

    @Override
    public void songlistFromCategoryResponse(ArrayList<Song> songs) {
        setValueRecyclerView(songs);
    }

    @Override
    public void songListFromAlbumResponse(ArrayList<Song> songs) {
        setValueRecyclerView(songs);
    }

    @Override
    public void onClickItem(int position) {
        Song song = songList.get(position);
        Glide.with(this).load(song.getSongImage()).into(songAvatar);
        collapsingToolbarLayout.setTitle(song.getSongName());

    }

    @Override
    public void getBitmap(Bitmap bitmap) {
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmap);
        collapsingToolbarLayout.setBackground(bitmapDrawable);
    }

    @Override
    public void onOptionClick(int position) {
        showOptionDialog(position);
    }

    public void showOptionDialog(int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
// add a list
        String[] animals = {"Play", "Remove song","Download"};
        builder.setItems(animals, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:

                        ArrayList<Song> songs = new ArrayList<>();
                        songs.add(songList.get(position));
                        Intent intent = new Intent(SongListActivity.this,PlayMusicActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("song",  songList);
                        bundle.putInt("position",position);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                    case 1:
                        songList.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        DownLoadFromUrl downLoadFromUrl = new DownLoadFromUrl(SongListActivity.this);
                        downLoadFromUrl.execute(songList.get(position).getSonglink());
                        break;
                }
            }
        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
