package com.example.apple.playmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Song  implements Parcelable{

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

    protected Song(Parcel in) {
        songId = in.readInt();
        songName = in.readString();
        songImage = in.readString();
        singer = in.readString();
        songlink = in.readString();
        likeNumber = in.readInt();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
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

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSonglink() {
        return songlink;
    }

    public void setSonglink(String songlink) {
        this.songlink = songlink;
    }

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(songId);
        parcel.writeString(songName);
        parcel.writeString(songImage);
        parcel.writeString(singer);
        parcel.writeString(songlink);
        parcel.writeInt(likeNumber);
    }
}
