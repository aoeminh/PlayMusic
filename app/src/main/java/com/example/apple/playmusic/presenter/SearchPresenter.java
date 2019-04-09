package com.example.apple.playmusic.presenter;

import android.util.Log;

import com.example.apple.playmusic.contract.ISearchPresenter;
import com.example.apple.playmusic.contract.ISearchView;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.service.APIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenter implements ISearchPresenter {

    ISearchView view;
    public SearchPresenter (ISearchView iSearchView){
        this.view = iSearchView;
    }
    @Override
    public void getSongFromSearch(String name) {
        APIService.getRetrofitClient().getSongFromSearch(name).enqueue(new Callback<ArrayList<Song>>() {
            @Override
            public void onResponse(Call<ArrayList<Song>> call, Response<ArrayList<Song>> response) {
                if(response.isSuccessful()){
                    view.onResponseSearch(response.body());
                    Log.d("SearchPresenter","size list song + " + response.body().size());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Song>> call, Throwable t) {
                Log.d("SearchPresenter","error + " + t.getMessage());

            }
        });
    }
}
