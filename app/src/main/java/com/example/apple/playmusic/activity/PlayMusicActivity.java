package com.example.apple.playmusic.activity;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.apple.playmusic.R;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);
        playerView = findViewById(R.id.video_view);
        mProgressBar = findViewById(R.id.progress_bar);
        if (savedInstanceState != null) {


                playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
                currentWindow = savedInstanceState.getInt(KEY_WINDOW);
                playbackPosition = savedInstanceState.getLong(KEY_POSITION);

        }

        shouldAutoPlay = true;
        mediaDataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "mediaPlayerSample"),
                (TransferListener<? super DataSource>) bandwidthMeter);
        initializePlayer();

    }

    private void initializePlayer() {
        Uri[] uris = new Uri[2];
        uris[0] = Uri.parse("https://aoeminh1993.000webhostapp.com/Song/Album/Bolero%20Tinh%20yeu/Anh-Da-Thay-Long-Duong-Hong-Loan.mp3");
        uris[1] = Uri.parse("https://aoeminh1993.000webhostapp.com/Song/Album/NgayChuaGiongBaoNguoiBatTuOst-BuiLanHuong-5708274.mp3");
        playerView.requestFocus();

        AdaptiveTrackSelection.Factory videoTrackSelectionFactory = new  AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        lastSeenTrackGroupArray = null;

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        playerView.setPlayer(player);

        if(player!=null) {
            player.addListener(new PlayerEventListener());
            player.addListener(new ExoPlayerListerner());
            playWhenReady = shouldAutoPlay;
        }
        MediaSource mediaSource =  new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(Uri.parse("https://aoeminh1993.000webhostapp.com/Song/Album/NgayChuaGiongBaoNguoiBatTuOst-BuiLanHuong-5708274.mp3"));
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        MediaSource[] mediaSources = new MediaSource[uris.length];
        for (int i = 0; i< mediaSources.length ; i++){
            mediaSources[i] = new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uris[i]);
        }

        concatenatingMediaSource.addMediaSources(0, Arrays.asList(mediaSources));
        boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(currentWindow, playbackPosition);
        }

        player.prepare(concatenatingMediaSource, !haveStartPosition, false);
        player.setPlayWhenReady(true);
        player.setRepeatMode(Player.REPEAT_MODE_ALL);

    }
    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {

        if (player != null) {
            updateStartPosition();
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void updateStartPosition() {

        if(player!= null) {
            playbackPosition = player.getContentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
        }
    }


    private void updateButtonVisibilities() {
        if (player == null) {
            return;
        }

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if(mappedTrackInfo ==null) return;

//        for (int i = 0; i <= mappedTrackInfo.getRendererCount(); i++) {
//            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
//            if (trackGroups.length != 0) {
//                if (player.getRendererType(i) == C.TRACK_TYPE_VIDEO) {
//                    ivSettings.visibility = View.VISIBLE;
//                    ivSettings.setOnClickListener(this);
//                    ivSettings.tag = i
//                }
//            }
//        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateStartPosition();
        outState    .putBoolean(KEY_PLAY_WHEN_READY, playWhenReady);
        outState.putInt(KEY_WINDOW, currentWindow);
        outState.putLong(KEY_POSITION, playbackPosition);
    }

    class PlayerEventListener extends Player.DefaultEventListener  {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState){
                case Player.STATE_IDLE:
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case Player.STATE_BUFFERING:
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case Player.STATE_READY:
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case Player.STATE_ENDED:
                    mProgressBar.setVisibility(View.GONE);
                    break;

            }
        }

    }

    class ExoPlayerListerner extends ExoPlayer.DefaultEventListener {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            Log.d("minhnq","onTimelineChanged");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.d("minhnq","onTracksChanged");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            if(isLoading){
                mProgressBar.setVisibility(View.VISIBLE);
            }else {
                mProgressBar.setVisibility(View.GONE);
            }
            Log.d("minhnq","onLoadingChanged");
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.d("minhnq","onPlayerStateChanged");
            switch (playbackState) {
                case Player.STATE_IDLE:
                    mProgressBar.setVisibility(View.VISIBLE);
                    break;
                case Player.STATE_READY:
                    mProgressBar.setVisibility(View.GONE);
                    break;
            }
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            Log.d("minhnq","onRepeatModeChanged");
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            Log.d("minhnq","onShuffleModeEnabledChanged");
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.d("minhnq","onPlayerError");
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            Log.d("minhnq","onPositionDiscontinuity + " +reason);
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            Log.d("minhnq","onPlaybackParametersChanged");
        }

        @Override
        public void onSeekProcessed() {
            Log.d("minhnq","onSeekProcessed");
        }

    }
}
