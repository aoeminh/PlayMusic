package com.example.apple.playmusic.fragment;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadata;
import android.media.session.MediaSession;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaDescriptionCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector;
import com.google.android.exoplayer2.ext.mediasession.TimelineQueueNavigator;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Arrays;



public class PlayMusicFragment extends Fragment implements GetImageFromUrl.IOnGetBitmap {

    public static final String TAG = "PlayMusicFragment";
    PlayerView playerView;
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_POSITION = "positio";
    private SimpleExoPlayer player;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    private boolean playWhenReady = false;

    private boolean isVisible = false;
    private boolean shouldAutoPlay = true;
    private DefaultTrackSelector trackSelector = null;
    private TrackGroupArray lastSeenTrackGroupArray = null;
    private DataSource.Factory mediaDataSourceFactory;
    private BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
    MediaSessionCompat mediaSession;
    private ProgressBar mProgressBar;
    private TextView tvSongName, tvSinger;
    private Song song;
    private int position;
    private RelativeLayout mFrameLayout;
    private ViewPager viewPager;
    int current =0;
    Bitmap bitmap1;

    private PlayerNotificationManager playerNotificationManager;
    public static PlayMusicFragment newInstance(Song song, int position) {
        PlayMusicFragment fragment = new PlayMusicFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("song", song);
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            song = getArguments().getParcelable("song");
            position = getArguments().getInt("position");
        }
        isVisible = getUserVisibleHint();


        Log.d(TAG, "onCreate " + position);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        if (savedInstanceState != null) {
            playWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);
            currentWindow = savedInstanceState.getInt(KEY_WINDOW);
            playbackPosition = savedInstanceState.getLong(KEY_POSITION);
        }
        View view = inflater.inflate(R.layout.fragment_play_music, container, false);
        initView(view);

        //init
            if (playerView.getPlayer() ==null){
                initializePlayer(song.getSonglink());
                player.setPlayWhenReady(true);

                Log.d(TAG," init in onCreateView " + position);

            }

            if(isVisible){
                player.setPlayWhenReady(true);
                initPlayerNoty();
            }else {
                player.setPlayWhenReady(false);
            }


        if (getActivity() instanceof PlayMusicActivity) {
            current = ((PlayMusicActivity)getActivity()).getCurrentPosition();
        }


        return view;
    }

    private void initView(View view) {
        playerView = view.findViewById(R.id.video_view);
        tvSongName = view.findViewById(R.id.tv_songname_playmusic);
        tvSinger = view.findViewById(R.id.tv_singer_playmusic);
        mFrameLayout = view.findViewById(R.id.fl_play_music_fragment);

        tvSinger.setText(song.getSinger());
        tvSongName.setText(song.getSongName());
    }

    private void initializePlayer(String url) {

        GetImageFromUrl getImageFromUrl = new GetImageFromUrl(this);
        getImageFromUrl.execute(song.getSongImage());
        playerView.requestFocus();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "mediaPlayerSample"),
                (TransferListener) bandwidthMeter);
        AdaptiveTrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);
        lastSeenTrackGroupArray = null;

        player = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

        if (player != null) {
            player.addListener(new PlayerEventListener());
            player.addListener(new ExoPlayerListerner());
        }
        MediaSource mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory)
                .createMediaSource(Uri.parse(url));
        boolean haveStartPosition = currentWindow != C.INDEX_UNSET;
//            if (haveStartPosition) {
//                player.seekTo(currentWindow, playbackPosition);
//            }

        player.prepare(mediaSource, !haveStartPosition, false);
        playerView.setPlayer(player);
        playerView.setControllerHideOnTouch(false);

        }

    public ConcatenatingMediaSource setDataForConcatenatingMediaSource() {
        Uri[] uris = new Uri[2];
        uris[0] = Uri.parse("https://aoeminh1993.000webhostapp.com/Song/Album/Bolero%20Tinh%20yeu/Anh-Da-Thay-Long-Duong-Hong-Loan.mp3");
        uris[1] = Uri.parse("https://aoeminh1993.000webhostapp.com/Song/Album/NgayChuaGiongBaoNguoiBatTuOst-BuiLanHuong-5708274.mp3");
        ConcatenatingMediaSource concatenatingMediaSource = new ConcatenatingMediaSource();
        MediaSource[] mediaSources = new MediaSource[uris.length];
        for (int i = 0; i < mediaSources.length; i++) {
            mediaSources[i] = new ExtractorMediaSource.Factory(mediaDataSourceFactory).createMediaSource(uris[i]);
        }

        concatenatingMediaSource.addMediaSources(0, Arrays.asList(mediaSources));
        return concatenatingMediaSource;
    }

    @Override
    public void onStart() {
        super.onStart();
//        if (player == null && song != null) {
//            initializePlayer(song.getSonglink());
//        }


        Log.d(TAG, "onStart " + position);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume " + position);
        hideSystemUi();
//        if (player == null && song != null) {
//            initializePlayer(song.getSonglink());
//        }
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause " + position);
        super.onPause();

//            releasePlayer();

    }

    private void releasePlayer() {

        if (player != null) {
            updateStartPosition();
            shouldAutoPlay = player.getPlayWhenReady();
            player.release();
            player = null;
            trackSelector = null;
            playerView.setPlayer(null);
            Log.d(TAG, "releasePlayer " + position);
        }
    }

    @Override
    public void onStop() {
        super.onStop();

//        releasePlayer();
        Log.d(TAG, "onStop " + position);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy " + position);
        releasePlayer();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView " + position);
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

        if (player != null) {
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
        if(playerView.getDefaultArtwork()== null){
            bitmap1= bitmap;
            playerView.setDefaultArtwork(bitmap);
        }

    }


    class PlayerEventListener extends Player.DefaultEventListener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState){
                case Player.STATE_ENDED:
                    if(getActivity() instanceof PlayMusicActivity){
                        PlayMusicActivity playMusicActivity = (PlayMusicActivity)getActivity();
                        current = playMusicActivity.getCurrentPosition();
                        if(current < playMusicActivity.getSongSize()){
                            playMusicActivity.setNextFragment(current);
                        }
                    }

                    break;

            }
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



    //if fragment not visible -> pause music
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser) {
            releasePlayer();
        } else {
            if (song != null) {
                if( playerView.getPlayer() ==null){
                    initializePlayer(song.getSonglink());
                    Log.d(TAG," init in setUserVisibleHint " + position);

                }
                player.setPlayWhenReady(true);
                initPlayerNoty();
            }

        }
    }

    private void initPlayerNoty(){
        playerNotificationManager = PlayerNotificationManager.createWithNotificationChannel(getActivity(), "1",
                R.string.bottom_sheet_behavior, 1, new PlayerNotificationManager.MediaDescriptionAdapter() {
                    @Override
                    public String getCurrentContentTitle(Player player) {
                        return song.getSongName();
                    }

                    @Nullable
                    @Override
                    public PendingIntent createCurrentContentIntent(Player player) {
                        Intent intent = new Intent(getActivity(),PlayMusicActivity.class);
                        Bundle bundle = new Bundle();
                        ArrayList<Song> songs = new ArrayList<>();
                        songs.add(song);
                        bundle.putParcelableArrayList("song",songs);
                        bundle.putInt("position",position);
                        intent.putExtras(bundle);
                        return PendingIntent.getActivity(getActivity(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    }

                    @Nullable
                    @Override
                    public String getCurrentContentText(Player player) {
                        return song.getSinger();
                    }

                    @Nullable
                    @Override
                    public Bitmap getCurrentLargeIcon(Player player, PlayerNotificationManager.BitmapCallback callback) {
                        return bitmap1;
                    }
                });

        playerNotificationManager.setPlayer(player);
        playerNotificationManager.setVisibility(View.VISIBLE);
        if(player.getPlayWhenReady()){
            playerNotificationManager.setOngoing(false);
        }else {
            playerNotificationManager.setOngoing(false);
        }
        playerNotificationManager.setUseNavigationActions(true);
        playerNotificationManager.setUsePlayPauseActions(true);
         mediaSession = new MediaSessionCompat(getActivity(), "TestAudio");
        mediaSession.setActive(true);

        MediaSessionConnector mediaSessionConnector = new MediaSessionConnector(mediaSession);
        mediaSessionConnector.setQueueNavigator(new TimelineQueueNavigator(mediaSession) {
            @Override
            public MediaDescriptionCompat getMediaDescription(int windowIndex) {
                return updateMediaSessionMetaData();
            }
        });
        mediaSessionConnector.setPlayer(player, null);

        PlaybackStateCompat playbackStateCompat = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_SEEK_TO |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                                PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE
                )
                .setState(
                        PlaybackStateCompat.STATE_PAUSED,
                        0,
                        1.0f)
                .build();
        mediaSession.setPlaybackState(playbackStateCompat);
        playerNotificationManager.setMediaSessionToken(mediaSession.getSessionToken());

    }

    private MediaDescriptionCompat updateMediaSessionMetaData() {
        MediaMetadataCompat.Builder metaDataBuilder = new MediaMetadataCompat.Builder();
        Bitmap albumArtBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_launcher_background);
        metaDataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArtBitmap);
        metaDataBuilder.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, albumArtBitmap);

        mediaSession.setMetadata(metaDataBuilder.build());

        MediaDescriptionCompat description = metaDataBuilder.build().getDescription();
        return description;
    }


}
