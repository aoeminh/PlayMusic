package com.example.apple.playmusic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Playlist implements Serializable {

    @SerializedName("PlaylistID")
    @Expose
    private int playlistId;

    @SerializedName("PlaylistName")
    @Expose
    private String playlistName;

    @SerializedName("PlaylistImage")
    @Expose
    private String playlistImage;

    @SerializedName("PlaylistIcon")
    @Expose
    private String playlistIcon;

    @SerializedName("songCount")
    @Expose
    private int songNumber;

    public int getSongNumber() {
        return songNumber;
    }

    public void setSongNumber(int songNumber) {
        this.songNumber = songNumber;
    }

    public int getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(int playlistId) {
        this.playlistId = playlistId;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getPlaylistImage() {
        return playlistImage;
    }

    public void setPlaylistImage(String playlistImage) {
        this.playlistImage = playlistImage;
    }

    public String getPlaylistIcon() {
        return playlistIcon;
    }

    public void setPlaylistIcon(String playlistIcon) {
        this.playlistIcon = playlistIcon;
    }
}
