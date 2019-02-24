package com.example.apple.playmusic.contract;

import com.example.apple.playmusic.model.Playlist;

import java.util.ArrayList;

public interface IListPlaylistView {
    void responseAllPlaylist(ArrayList<Playlist> allPlaylist);

}
