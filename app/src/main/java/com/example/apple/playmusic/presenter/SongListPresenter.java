package com.example.apple.playmusic.presenter;

import android.util.Log;

import com.example.apple.playmusic.contract.ISongListPresenter;
import com.example.apple.playmusic.contract.ISongListViewCallback;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.model.Theme;
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

    @Override
    public void songlistFromPlaylist(String id) {
        APIService.getRetrofitClient().getSongListFromPlaylist(id).enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if(response.isSuccessful()){
                    view.songListFromPlaylistResponse(response.body());
                    Log.d(TAG,"success get list song from playlist" + response.body().size() );
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                Log.d(TAG,"error get list song from playlist" + t.getMessage() );
            }
        });
    }

    @Override
    public void songlistFromTheme(String id) {
        APIService.getRetrofitClient().getSongFromTheme(id).enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if(response.isSuccessful()){
                    view.songlistFromThemeResponse(response.body());
                    Log.d(TAG, "success get list song from theme size: " +response.body().size());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                Log.d(TAG, "error get list song from theme : " + t.getMessage());
            }
        });
    }

    @Override
    public void songlistFromCategory(String id) {
        APIService.getRetrofitClient().getSongFromCategory(id).enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if(response.isSuccessful()){
                    view.songlistFromCategoryResponse(response.body());
                    Log.d(TAG, "success get list song from category size: " +response.body().size());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                Log.d(TAG, "error get list song from category : " + t.getMessage());
            }
        });
    }

    @Override
    public void songListFromAlbum(String id) {
        APIService.getRetrofitClient().getSongFromAlbum(id).enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if(response.isSuccessful()){
                    view.songlistFromCategoryResponse(response.body());
                    Log.d(TAG, "success get list song from category size: " +response.body().size());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                Log.d(TAG, "error get list song from category : " + t.getMessage());
            }
        });
    }
}
