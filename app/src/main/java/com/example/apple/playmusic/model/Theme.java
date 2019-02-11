package com.example.apple.playmusic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Theme {

    @SerializedName("idChude")
    @Expose
    private int idChude;

    @SerializedName("tenChude")
    @Expose
    private String tenChude;

    @SerializedName("hinhChude")
    @Expose
    private String hinhChude;

    public int getIdChude() {
        return idChude;
    }

    public void setIdChude(int idChude) {
        this.idChude = idChude;
    }

    public String getTenChude() {
        return tenChude;
    }

    public void setTenChude(String tenChude) {
        this.tenChude = tenChude;
    }

    public String getHinhChude() {
        return hinhChude;
    }

    public void setHinhChude(String hinhChude) {
        this.hinhChude = hinhChude;
    }
}
