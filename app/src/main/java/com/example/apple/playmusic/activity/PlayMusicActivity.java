package com.example.apple.playmusic.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.adapter.PlayMusicAdapter;
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

public class PlayMusicActivity extends AppCompatActivity {
    PlayerView playerView;
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "positio";
    private SimpleExoPlayer player;
    private int currentWindow = 0;
    private long playbackPosition =0;
    private boolean playWhenReady =false;

    private boolean shouldAutoPlay = true;
    private DefaultTrackSelector trackSelector= null;
    private TrackGroupArray lastSeenTrackGroupArray = null;
    private DataSource.Factory  mediaDataSourceFactory;
    private BandwidthMeter bandwidthMeter =  new DefaultBandwidthMeter();

    private ProgressBar mProgressBar;
    private ViewPager viewPager;
    private PlayMusicAdapter adapter;
    private ArrayList<Song> songs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        if(getIntent() !=null){
            songs = getIntent().getParcelableArrayListExtra("song");
            if(songs.size()>0){
                Log.d("minh", "" + songs.size());
                adapter = new PlayMusicAdapter(getSupportFragmentManager(),songs);
            }
        }
        viewPager =findViewById(R.id.view_pager_play_activity);
        viewPager.setAdapter(adapter);

    }
}
