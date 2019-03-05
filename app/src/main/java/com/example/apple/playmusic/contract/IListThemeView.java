package com.example.apple.playmusic.contract;

import com.example.apple.playmusic.model.Theme;

import java.util.ArrayList;

public interface IListThemeView {
    void onResponseAllTheme(ArrayList<Theme> themes);
}
