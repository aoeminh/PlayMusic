package com.example.apple.playmusic.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.Ultils.GetImageFromUrl;
import com.example.apple.playmusic.activity.PlayMusicActivity;
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
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.Arrays;

public class PlayMusicFragment extends Fragment implements GetImageFromUrl.IOnGetBitmap {

    public static final String TAG= "PlayMusicFragment";
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
    private TextView tvSongName,tvSinger;
    private Song song;

    private RelativeLayout mFrameLayout;

    public static PlayMusicFragment newInstance(Song song){
        Log.d(TAG,"newInstance");
        PlayMusicFragment fragment = new PlayMusicFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("song",song);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() !=null){
            song = getArguments().getParcelable("song");
        }
        Log.d(TAG,"onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(KEY_WINDOW);
            playbackPosition = savedInstanceState.getLong(KEY_POSITION);
        }
        View view= inflater.inflate(R.layout.fragment_play_music,container,false);
        initView(view);
        GetImageFromUrl getImageFromUrl = new GetImageFromUrl(this);
        getImageFromUrl.execute(song.getSongImage());
        shouldAutoPlay = true;
        mediaDataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "mediaPlayerSample"),
                (TransferListener<? super DataSource>) bandwidthMeter);
        initializePlayer(song.getSonglink());

        return view;
    }

    private  void initView(View view){
        playerView = view.findViewById(R.id.video_view);
        tvSongName = view.findViewById(R.id.tv_songname_playmusic);
        tvSinger = view.findViewById(R.id.tv_singer_playmusic);
        mFrameLayout = view.findViewById(R.id.fl_play_music_fragment);

        tvSinger.setText(song.getSinger());
        tvSongName.setText(song.getSongName());
    }

    private void initializePlayer(String url) {
         playerView.requestFocus();

        AdaptiveTrackSelection.Factory videoTrackSelectionFactory = new  AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        lastSeenTrackGroupArray = null;

        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        playerView.setPlayer(player);

        if(player!=null) {
            player.addListener(new PlayerEventListener());
            player.addListener(new ExoPlayerListerner());
            playWhenReady = shouldAutoPlay;
        }
        MediaSource mediaSource =  new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(Uri.parse(url));
        boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
        if (haveStartPosition) {
            player.seekTo(currentWindow, playbackPosition);
        }

        player.prepare(mediaSource, !haveStartPosition, false);
        player.setPlayWhenReady(true);
//        player.setRepeatMode(Player.REPEAT_MODE_ALL);

    }

    public ConcatenatingMediaSource setDataForConcatenatingMediaSource(){
        Uri[] uris = new Uri[2];
        uris[0] = Uri.parse("https://aoeminh1993.000webhostapp.com/Song/Album/Bolero%20Tinh%20yeu/Anh-Da-Thay-Long-Duong-Hong-Loan.mp3");
        uris[1] = Uri.parse("https://aoeminh1993.000webhostapp.com/Song/Album/NgayChuaGiongBaoNguoiBatTuOst-BuiLanHuong-5708274.mp3");
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        MediaSource[] mediaSources = new MediaSource[uris.length];
        for (int i = 0; i< mediaSources.length ; i++){
            mediaSources[i] = new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uris[i]);
        }

        concatenatingMediaSource.addMediaSources(0, Arrays.asList(mediaSources));
        return concatenatingMediaSource;
    }

    @Override
    public void onStart() {
        super.onStart();

            initializePlayer(song.getSonglink());

        Log.d(TAG,"onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume");
        hideSystemUi();
        if (player == null) {
//            initializePlayer(song.getSonglink());
            player.setPlayWhenReady(true);
        }
    }
    @Override
    public void onPause() {
        Log.d(TAG,"onPause");
        super.onPause();

//            releasePlayer();
            player.setPlayWhenReady(false);

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
            player.setPlayWhenReady(false);
            Log.d(TAG,"onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"onDestroy");
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG,"onDestroyView");
        super.onDestroyView();
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        updateStartPosition();
        outState.putBoolean(KEY_PLAY_WHEN_READY, playWhenReady);
        outState.putInt(KEY_WINDOW, currentWindow);
        outState.putLong(KEY_POSITION, playbackPosition);
    }

    @Override
    public void getBitmap(Bitmap bitmap) {
        if(getActivity().getResources() !=null){
            BitmapDrawable bitmapDrawable = new BitmapDrawable(getActivity().getResources(),bitmap);
            mFrameLayout.setBackground(bitmapDrawable);
        }
    }


    class PlayerEventListener extends Player.DefaultEventListener  {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
//            switch (playbackState){
//                case Player.STATE_IDLE:
//                    mProgressBar.setVisibility(View.GONE);
//                    break;
//                case Player.STATE_BUFFERING:
//                    mProgressBar.setVisibility(View.VISIBLE);
//                    break;
//                case Player.STATE_READY:
//                    mProgressBar.setVisibility(View.GONE);
//                    break;
//                case Player.STATE_ENDED:
//                    mProgressBar.setVisibility(View.GONE);
//                    break;
//
//            }
        }

    }

    class ExoPlayerListerner extends ExoPlayer.DefaultEventListener {
        @Override
        public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {
            Log.d("minhnq", "onTimelineChanged");
        }

        @Override
        public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
            Log.d("minhnq", "onTracksChanged");
        }

        @Override
        public void onLoadingChanged(boolean isLoading) {
            Log.d("minhnq", "onLoadingChanged");
        }

        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            Log.d("minhnq", "onPlayerStateChanged");
        }

        @Override
        public void onRepeatModeChanged(int repeatMode) {
            Log.d("minhnq", "onRepeatModeChanged");
        }

        @Override
        public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {
            Log.d("minhnq", "onShuffleModeEnabledChanged");
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            Log.d("minhnq", "onPlayerError");
        }

        @Override
        public void onPositionDiscontinuity(int reason) {
            Log.d("minhnq", "onPositionDiscontinuity + " + reason);
        }

        @Override
        public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
            Log.d("minhnq", "onPlaybackParametersChanged");
        }

        @Override
        public void onSeekProcessed() {
            Log.d("minhnq", "onSeekProcessed");
        }
    }
}
