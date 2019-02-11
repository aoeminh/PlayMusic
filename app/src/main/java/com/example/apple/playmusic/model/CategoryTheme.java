package com.example.apple.playmusic.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class CategoryTheme {

    @SerializedName("Category")
    @Expose
    private ArrayList<Category> categoryArrayList;

    @SerializedName("Chude")
    @Expose
    private ArrayList<Theme> themeArrayList;

    public ArrayList<Category> getCategoryArrayList() {
        return categoryArrayList;
    }

    public void setCategoryArrayList(ArrayList<Category> categoryArrayList) {
        this.categoryArrayList = categoryArrayList;
    }

    public ArrayList<Theme> getThemeArrayList() {
        return themeArrayList;
    }

    public void setThemeArrayList(ArrayList<Theme> themeArrayList) {
        this.themeArrayList = themeArrayList;
    }
}
