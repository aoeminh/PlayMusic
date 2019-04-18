package com.example.apple.playmusic.presenter;

import com.example.apple.playmusic.contract.ILocalPresenter;
import com.example.apple.playmusic.contract.IlocalView;
import com.example.apple.playmusic.model.Song;

import java.util.ArrayList;

public class LocalSongPresenter implements ILocalPresenter {
    IlocalView view;
    ArrayList<Song> songs = new ArrayList<>();

    public LocalSongPresenter(IlocalView ilocalView){
        this.view =ilocalView;
    }

    @Override
    public void getAllSongFromPhone() {

    }
}
