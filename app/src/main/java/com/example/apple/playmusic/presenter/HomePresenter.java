package com.example.apple.playmusic.presenter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.apple.playmusic.adapter.PlaylistAdapter;
import com.example.apple.playmusic.contract.IPresenterCallback;
import com.example.apple.playmusic.contract.IViewCallback;
import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.Album;
import com.example.apple.playmusic.model.CategoryTheme;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.service.APIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePresenter  implements IPresenterCallback{
    IViewCallback view;

    public HomePresenter(IViewCallback iViewCallback){
        this.view = iViewCallback;
    }


    @Override
    public void requestBannerHome() {
            APIService.getRetrofitClient().getAdvertise().enqueue(new Callback<ArrayList<Advertise>>() {
                @Override
                public void onResponse(@NonNull Call<ArrayList<Advertise>> call, @NonNull Response<ArrayList<Advertise>> response) {
                    if(response.isSuccessful()){
                        view.responseBanner(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ArrayList<Advertise>> call, @NonNull Throwable t) {
                    Log.d("minhnq","getData Banner Fragment Error: " +t.getMessage());
                }
            });

    }

    @Override
    public void requestDayPlaylist() {

        APIService.getRetrofitClient().getPlaylist().enqueue(new Callback<ArrayList<Playlist>>() {
            @Override
            public void onResponse(@NonNull Call<ArrayList<Playlist>> call, @NonNull Response<ArrayList<Playlist>> response) {

                if(response.isSuccessful()){
                    view.responsePlaylist(response.body());
                    Log.d("minhnq", "get playlist " +response.body().size());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArrayList<Playlist>> call, @NonNull Throwable t) {
                Log.d("minhnq", "get playlist error " +t.getMessage());
            }
        });
    }

    @Override
    public void requestCategoryTheme() {
        APIService.getRetrofitClient().getCategoryThem().enqueue(new Callback<CategoryTheme>() {
            @Override
            public void onResponse(Call<CategoryTheme> call, Response<CategoryTheme> response) {
                if(response.isSuccessful());
                view.responseCategoryTheme(response.body());
            }

            @Override
            public void onFailure(Call<CategoryTheme> call, Throwable t) {
                Log.d("minhnq", "get category$theme error " +t.getMessage());

            }
        });
    }

    @Override
    public void requestAlbumHot() {
        APIService.getRetrofitClient().getAlbumHot().enqueue(new Callback<ArrayList<Album>>() {
            @Override
            public void onResponse(Call<ArrayList<Album>> call, Response<ArrayList<Album>> response) {
                if(response.isSuccessful()){
                    view.responseAlbumHot(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Album>> call, Throwable t) {
                Log.d("minhnq", "get album hot error " +t.getMessage());
            }
        });
    }

    @Override
    public void requestLoveSong() {

        APIService.getRetrofitClient().getLoveSong().enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if(response.isSuccessful()){
                    view.responseLoveSong(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                Log.d("minhnq", "get lovesong error " +t.getMessage());
            }
        });
    }
}
