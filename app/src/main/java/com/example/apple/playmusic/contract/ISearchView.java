package com.example.apple.playmusic.contract;

import com.example.apple.playmusic.model.Song;

import java.util.ArrayList;

public interface ISearchView {
    void onResponseSearch(ArrayList<Song> songs);
}
