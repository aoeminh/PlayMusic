package com.example.apple.playmusic.presenter;

import android.util.Log;

import com.example.apple.playmusic.contract.IListThemePresenter;
import com.example.apple.playmusic.contract.IListThemeView;
import com.example.apple.playmusic.model.Theme;
import com.example.apple.playmusic.service.APIService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListThemePresenter implements IListThemePresenter {
    public static final String TAG = "ListThemePresenter";
    IListThemeView view ;

    public ListThemePresenter( IListThemeView iListThemeView){
        this.view =iListThemeView;
    }

    @Override
    public void getAllTheme() {
        APIService.getRetrofitClient().getAllTheme().enqueue(new Callback<ArrayList<Theme>>() {
            @Override
            public void onResponse(Call<ArrayList<Theme>> call, Response<ArrayList<Theme>> response) {
                if(response.isSuccessful()){
                    view.onResponseAllTheme(response.body());
                    Log.d(TAG,"success size " + response.body().size() );
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Theme>> call, Throwable t) {
                Log.d(TAG,"error  " + t.getMessage());
            }
        });
    }
}
