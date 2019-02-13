package com.example.apple.playmusic.contract;

import com.example.apple.playmusic.model.Advertise;
import com.example.apple.playmusic.model.Album;
import com.example.apple.playmusic.model.CategoryTheme;
import com.example.apple.playmusic.model.Playlist;
import com.example.apple.playmusic.model.Song;

import java.util.ArrayList;

public interface IViewCallback {
    void responseBanner(ArrayList<Advertise> advertises);
    void responsePlaylist(ArrayList<Playlist> playlists);
    void responseCategoryTheme(CategoryTheme categoryThemes);
    void responseAlbumHot(ArrayList<Album> albums);
    void responseLoveSong(ArrayList<Song> songs);
}
