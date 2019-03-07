package com.example.apple.playmusic.contract;

import com.example.apple.playmusic.model.Album;

import java.util.ArrayList;

public interface IAlbumView {
    void responseAllAlbum(ArrayList<Album> albums);
}
