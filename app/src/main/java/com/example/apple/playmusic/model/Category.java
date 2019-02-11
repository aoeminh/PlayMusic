package com.example.apple.playmusic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Category {

    @SerializedName("idCategory")
    @Expose
    private int idCategory;

    @SerializedName("idChude")
    @Expose
    private int idChude;

    @SerializedName("categoryName")
    @Expose
    private String categoryName;

    @SerializedName("categoryImage")
    @Expose
    private String categoryImage;

    public int getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public int getIdChude() {
        return idChude;
    }

    public void setIdChude(int idChude) {
        this.idChude = idChude;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }
}
