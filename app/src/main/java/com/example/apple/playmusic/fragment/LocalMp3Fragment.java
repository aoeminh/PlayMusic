package com.example.apple.playmusic.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.Ultils.RequestPermision;
import com.example.apple.playmusic.action.IOnItemClick;
import com.example.apple.playmusic.activity.MainActivity;
import com.example.apple.playmusic.activity.PlayMusicActivity;
import com.example.apple.playmusic.adapter.LocalListSongAdapter;
import com.example.apple.playmusic.contract.ILocalPresenter;
import com.example.apple.playmusic.contract.IlocalView;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.presenter.LocalSongPresenter;

import java.util.ArrayList;
import java.util.List;

public class LocalMp3Fragment extends Fragment implements IlocalView, IOnItemClick {

    Toolbar toolbar;
    ArrayList<Song> listSongLocal= new ArrayList<>();
    LocalListSongAdapter adapter;
    boolean isRequest = false;
    ILocalPresenter presenter;
    RecyclerView rvLocalListView;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isRequest=RequestPermision.requestPermision(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_mp3,container,false);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setupToolbar();
        presenter = new LocalSongPresenter(this,getActivity());
        presenter.getAllSongFromPhone(isRequest);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_toolbar_local_fragment,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    void initView(View view){
        rvLocalListView = view.findViewById(R.id.rv_list_song_local);
        toolbar = view.findViewById(R.id.toolbar_local_fragment);
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider));

        rvLocalListView.addItemDecoration(itemDecorator);
    }
    void setupToolbar(){
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);


    }

    private ArrayList<Song> scanDeviceForMp3Files(){
        ArrayList<Song> songs = new ArrayList<>();
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA

        };
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = getActivity().getContentResolver().query(uri, projection, selection, null, sortOrder);
            if( cursor != null){
                cursor.moveToFirst();

                while( !cursor.isAfterLast() ){
                    String title = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String path = cursor.getString(2);
                    String displayName  = cursor.getString(3);
                    String songDuration = cursor.getString(4);
                    String data = cursor.getString(5);
                    cursor.moveToNext();
                    if(path != null && path.endsWith(".mp3")) {
                        Song song =new Song();
                        song.setSinger(artist);
                        song.setSongName(title);
                        song.setSonglink(data);
                        songs.add(song);
                    }
                }
            }

        } catch (Exception e) {
            Log.e("minhnhnh", e.toString());
        }finally{
            if( cursor != null){
                cursor.close();
            }
        }
        return songs;
    }

    @Override
    public void responseAllSongFromPhone(ArrayList<Song> song) {
        listSongLocal = song;
        adapter = new LocalListSongAdapter(getActivity(),listSongLocal,this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvLocalListView.setLayoutManager(layoutManager);
        rvLocalListView.setAdapter(adapter);
    }

    @Override
    public void onClickItem(int position) {
        ArrayList<Song> lovesongs = new ArrayList<>();
        lovesongs.add(listSongLocal.get(position));
        Intent intent = new Intent(getActivity(), PlayMusicActivity.class);
        intent.putParcelableArrayListExtra("song",lovesongs);
        startActivity(intent);

    }
}
