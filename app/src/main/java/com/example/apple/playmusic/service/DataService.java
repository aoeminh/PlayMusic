package com.example.apple.playmusic.service;

import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.Album;
import com.example.apple.playmusic.model.CategoryTheme;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.model.Song;
import com.example.apple.playmusic.model.Theme;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DataService {

    @GET("advertise.php")
    Call<ArrayList<Advertise>> getAdvertise();

    @GET("playlist.php")
    Call<ArrayList<Playlist>> getPlaylist();

    @GET("category&themeday.php")
    Call<CategoryTheme> getCategoryThem();

    @GET("albumhot.php")
    Call<ArrayList<Album>> getAlbumHot();

    @GET("lovesong.php")
    Call<ArrayList<Song>> getLoveSong();

    @FormUrlEncoded
    @POST("songlist.php")
    Call<ArrayList<Song>> getSongListFromAdvertise(@Field("advId") String id);

    @FormUrlEncoded
    @POST("songlist.php")
    Call<ArrayList<Song>> getSongListFromPlaylist(@Field("playlistId") String id);

    @FormUrlEncoded
    @POST("songlist.php")
    Call<ArrayList<Song>> getSongFromTheme(@Field("themeId") String themeId);

    @FormUrlEncoded
    @POST("songlist.php")
    Call<ArrayList<Song>> getSongFromCategory(@Field("themeId") String themeId);

    @FormUrlEncoded
    @POST("songlist.php")
    Call<ArrayList<Song>> getSongFromAlbum(@Field("albumId") String albumId);

    @FormUrlEncoded
    @POST("search.php")
    Call<ArrayList<Song>> getSongFromSearch(@Field("songName") String songname);

    @GET("allPlaylist.php")
    Call<ArrayList<Playlist>> getAllPlaylist();

    @GET("allTheme.php")
    Call<ArrayList<Theme>> getAllTheme();

    @GET("getAllAlbum.php")
    Call<ArrayList<Album>> getAllAlbum();
}
