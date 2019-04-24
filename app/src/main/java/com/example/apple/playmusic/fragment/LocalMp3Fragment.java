package com.example.apple.playmusic.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
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
import android.support.v4.view.MenuItemCompat;
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
import android.widget.SearchView;
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
import java.util.Iterator;
import java.util.List;

public class LocalMp3Fragment extends Fragment implements IlocalView, IOnItemClick {

    Toolbar toolbar;
    ArrayList<Song> listSongLocal= new ArrayList<>();
    LocalListSongAdapter adapter;
    boolean isRequest = false;
    ILocalPresenter presenter;
    RecyclerView rvLocalListView;
    ArrayList<Song> listTemp= new ArrayList<>();
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
        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        SearchManager searchManager =    (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView.setIconifiedByDefault(false);
        searchView.requestFocus();
        searchView.setQueryHint("Search");
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getActivity(),"search",Toast.LENGTH_SHORT).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.isEmpty()) {
                    listTemp = listSongLocal;
                    adapter.appendlist(listTemp);
                    Log.d("minhnn", "empty");
                }

                for(Song song: listSongLocal){
                    if(song.getSongName().contains(newText)){
                        listTemp.add(song);

                    }
                }
                adapter.appendlist(listTemp);
                return false;
            }
        });
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
