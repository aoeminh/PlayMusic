package com.example.apple.playmusic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Advertise implements Serializable {

    @SerializedName("AdID")
    @Expose
    private int adID;

    @SerializedName("AdImage")
    @Expose
    private String adImage;

    @SerializedName("AdvContent")
    @Expose
    private String advContent;

    @SerializedName("AdSongID")
    @Expose
    private int adSongID;

    @SerializedName("songname")
    @Expose
    private String songName;

    @SerializedName("songimage")
    @Expose
    private String songImage;

    public int getAdID() {
        return adID;
    }

    public void setAdID(int adID) {
        this.adID = adID;
    }

    public String getAdImage() {
        return adImage;
    }

    public void setAdImage(String adImage) {
        this.adImage = adImage;
    }

    public String getAdvContent() {
        return advContent;
    }

    public void setAdvContent(String advContent) {
        this.advContent = advContent;
    }

    public int getAdSongID() {
        return adSongID;
    }

    public void setAdSongID(int adSongID) {
        this.adSongID = adSongID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongImage() {
        return songImage;
    }

    public void setSongImage(String songImage) {
        this.songImage = songImage;
    }
}
