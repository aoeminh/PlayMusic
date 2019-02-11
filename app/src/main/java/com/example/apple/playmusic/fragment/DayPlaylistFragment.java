package com.example.apple.playmusic.fragment;

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
import com.example.apple.playmusic.adapter.PlaylistAdapter;
import com.example.apple.playmusic.contract.IPresenterCallback;
import com.example.apple.playmusic.contract.IViewCallback;
import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.CategoryTheme;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.presenter.HomePresenter;
import com.example.apple.playmusic.service.APIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public  class DayPlaylistFragment extends Fragment implements IViewCallback {

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

        return view;
    }

    private void getData() {

        presenterCallback.requestDayPlaylist();
//        APIService.getRetrofitClient().getPlaylist().enqueue(new Callback<ArrayList<Playlist>>() {
//            @Override
//            public void onResponse(@NonNull Call<ArrayList<Playlist>> call, @NonNull Response<ArrayList<Playlist>> response) {
//
//                if(response.isSuccessful()){
//                    playlistArrayList = response.body();
//                    adapter = new PlaylistAdapter(getActivity(),playlistArrayList);
//                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                    layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                    rv_playlist.setLayoutManager(layoutManager);
//                    rv_playlist.setAdapter(adapter);
//                    Log.d("minhnq", "get playlist " +response.body().size());
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ArrayList<Playlist>> call, @NonNull Throwable t) {
//                Log.d("minhnq", "get playlist error " +t.getMessage());
//            }
//        });
    }

    private void initView(View view){
        tvTitle = view.findViewById(R.id.tv_title_playlist);
        tvViewMore = view.findViewById(R.id.tv_viewmore_playlist_fragment);
        rv_playlist = view.findViewById(R.id.rv_day_playlist);
        presenterCallback = new HomePresenter(this);
    }

    @Override
    public void responseBanner(ArrayList<Advertise> advertises) {

    }

    @Override
    public void responsePlaylist(ArrayList<Playlist> playlists) {
        playlistArrayList = playlists;
        adapter = new PlaylistAdapter(getActivity(),playlistArrayList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_playlist.setLayoutManager(layoutManager);
        rv_playlist.setAdapter(adapter);
    }

    @Override
    public void responseCategoryTheme(CategoryTheme categoryThemes) {

    }
}
