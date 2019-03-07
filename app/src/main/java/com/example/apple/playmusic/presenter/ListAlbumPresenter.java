package com.example.apple.playmusic.presenter;

import com.example.apple.playmusic.contract.IAlbumPresenter;
import com.example.apple.playmusic.contract.IAlbumView;
import com.example.apple.playmusic.model.Album;
import com.example.apple.playmusic.service.APIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListAlbumPresenter implements IAlbumPresenter {
    private IAlbumView view;


    public ListAlbumPresenter(IAlbumView iAlbumView){
        this.view = iAlbumView;
    }
    @Override
    public void getAllAlbum() {
        APIService.getRetrofitClient().getAllAlbum().enqueue(new Callback<ArrayList<Album>>() {
            @Override
            public void onResponse(Call<ArrayList<Album>> call, Response<ArrayList<Album>> response) {
                if(response.isSuccessful()){
                    view.responseAllAlbum(response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Album>> call, Throwable t) {

            }
        });
    }
}
