package com.example.apple.playmusic.service;

import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.Album;
import com.example.apple.playmusic.model.CategoryTheme;
import com.example.apple.playmusic.model.Playlist;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DataService {

    @GET("advertise.php")
    Call<ArrayList<Advertise>> getAdvertise();

    @GET("playlist.php")
    Call<ArrayList<Playlist>> getPlaylist();

    @GET("category&themeday.php")
    Call<CategoryTheme> getCategoryThem();

    @GET("albumhot.php")
    Call<ArrayList<Album>> getAlbumHot();
}
