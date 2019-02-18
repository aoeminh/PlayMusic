package com.example.apple.playmusic.presenter;

import android.util.Log;

import com.example.apple.playmusic.contract.ISongListPresenter;
import com.example.apple.playmusic.contract.ISongListViewCallback;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.service.APIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongListPresenter implements ISongListPresenter{
    public static final String TAG ="SongListPresenter";
    private ISongListViewCallback view;
    public SongListPresenter(ISongListViewCallback iSongListViewCallback){
        this.view = iSongListViewCallback;
    }
    @Override
    public void songlistRequest(String id) {
        APIService.getRetrofitClient().getSongListFromAdvertise(id).enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if(response.isSuccessful()&& response.body() !=null){
                    Log.d(TAG,"success " + response.body().size() );
                    view.songListResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                Log.d(TAG,"error " + t.getMessage());
            }
        });
    }
}
