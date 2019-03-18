package com.example.apple.playmusic.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.adapter.PlayMusicAdapter;
import com.example.apple.playmusic.fragment.PlayMusicFragment;
import com.example.apple.playmusic.model.Song;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Arrays;

public class PlayMusicActivity extends AppCompatActivity{

    private ViewPager viewPager;
    private PlayMusicAdapter adapter;
    private ArrayList<Song> songs;
    private int curentPostion =0;
    private int firstPosition=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        Bundle  bundle = new Bundle();
        if(getIntent().getExtras() !=null){
            bundle = getIntent().getExtras();
            songs = bundle.getParcelableArrayList("song");
            firstPosition = bundle.getInt("position");
            adapter = new PlayMusicAdapter(getSupportFragmentManager(),songs);

        }
        viewPager =findViewById(R.id.view_pager_play_activity);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(firstPosition);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                curentPostion = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    public  ViewPager getViewPager(){
        return this.viewPager;
    }

    public int getCurrentPosition() {
        return curentPostion;
    }

    public void setNextFragment(int curentPostion){
        viewPager.setCurrentItem(curentPostion +1);
    }

    public int getSongSize(){
        return songs.size();
    }

}
