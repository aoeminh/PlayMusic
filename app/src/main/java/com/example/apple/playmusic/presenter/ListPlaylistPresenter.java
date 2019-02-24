package com.example.apple.playmusic.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.apple.playmusic.contract.IListPlaylistView;
import com.example.apple.playmusic.contract.IListplaylistPresenter;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.service.APIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListPlaylistPresenter implements IListplaylistPresenter {
    public static String TAG = "ListPlaylistPresenter";
    IListPlaylistView view;

    public ListPlaylistPresenter(IListPlaylistView view){
        this.view = view;

    }
    @Override
    public void getAllPlaylist() {
        APIService.getRetrofitClient().getAllPlaylist().enqueue(new Callback<ArrayList<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Playlist>> call, @NonNull Response<ArrayList<Playlist>> response) {
                if(response.isSuccessful()){
                    view.responseAllPlaylist(response.body());
                    Log.d(TAG,"success list size: " +response.body().size());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Playlist>> call, @NonNull Throwable t) {
                Log.d(TAG,"error  " + t.getMessage());
            }
        });
    }
}
