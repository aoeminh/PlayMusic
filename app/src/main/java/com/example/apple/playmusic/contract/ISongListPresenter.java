package com.example.apple.playmusic.contract;

public interface ISongListPresenter {
    void songlistRequest(String id);
    void songlistFromPlaylist(String id);
    void songlistFromTheme(String id);
    void songlistFromCategory(String id);
    void songListFromAlbum(String id);
}
