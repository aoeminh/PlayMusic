package com.example.apple.playmusic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Album {
    @SerializedName("AlbumID")
    @Expose
    private int albumID;

    @SerializedName("AlbumName")
    @Expose
    private String albumName;

    @SerializedName("SingerName")
    @Expose
    private String singerName;

    @SerializedName("AlbumImage")
    @Expose
    private String AlbumImage;

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getAlbumImage() {
        return AlbumImage;
    }

    public void setAlbumImage(String albumImage) {
        AlbumImage = albumImage;
    }
}
