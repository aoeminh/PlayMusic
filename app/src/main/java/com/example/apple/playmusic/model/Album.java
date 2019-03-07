package com.example.apple.playmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Album  implements Parcelable{
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(this.albumID);
        dest.writeString(this.albumName);
        dest.writeString(this.singerName);
        dest.writeString(this.AlbumImage);
    }

    protected Album(Parcel in) {
        this.albumID = in.readInt();
        this.albumName = in.readString();
        this.singerName = in.readString();
        this.AlbumImage = in.readString();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel source) {
            return new Album(source);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

}
