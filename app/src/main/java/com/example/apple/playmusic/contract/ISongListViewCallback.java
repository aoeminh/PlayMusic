package com.example.apple.playmusic.contract;

import com.example.apple.playmusic.model.Song;

import java.util.ArrayList;

public interface ISongListViewCallback {

    void songListResponse(ArrayList<Song> songs);
    void songListFromPlaylistResponse(ArrayList<Song> songs);
    void songlistFromThemeResponse(ArrayList<Song> songs);
    void songlistFromCategoryResponse(ArrayList<Song> songs);
}
