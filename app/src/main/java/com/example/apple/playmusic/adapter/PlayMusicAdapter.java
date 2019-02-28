package com.example.apple.playmusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.apple.playmusic.fragment.PlayMusicFragment;
import com.example.apple.playmusic.model.Song;

import java.util.ArrayList;

public class PlayMusicAdapter extends FragmentStatePagerAdapter {
    ArrayList<Song> songs ;
    public PlayMusicAdapter(FragmentManager fm, ArrayList<Song> songs ) {
        super(fm);
        this.songs = songs;
    }

    @Override
    public Fragment getItem(int i) {

        return PlayMusicFragment.newInstance(songs.get(i));
    }

    @Override
    public int getCount() {
        return songs.size();
    }
}
