package com.example.apple.playmusic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Song {

    @SerializedName("songId")
    @Expose
    private int songId;

    @SerializedName("songName")
    @Expose
    private String songName;

    @SerializedName("songImage")
    @Expose
    private String songImage;

    @SerializedName("singer")
    @Expose
    private String singer;

    @SerializedName("songlink")
    @Expose
    private String songlink;

    @SerializedName("likeNumber")
    @Expose
    private int likeNumber;
}
