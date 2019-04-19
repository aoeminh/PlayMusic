package com.example.apple.playmusic.fragment;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaButtonReceiver;
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

import com.bumptech.glide.Glide;
import com.example.apple.playmusic.R;
import com.example.apple.playmusic.Ultils.GetImageFromUrl;
import com.example.apple.playmusic.activity.PlayMusicActivity;
import com.example.apple.playmusic.model.Song;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ControlDispatcher;
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
import com.google.android.exoplayer2.ui.PlayerNotificationManager;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Context.NOTIFICATION_SERVICE;


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

    private static final String CHANNEL_ID = "media_playback_channel";

    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    private static MediaSessionCompat mMediaSession;
    private MediaSessionCompat.Token token;

    private boolean isVisible = false;
    private boolean shouldAutoPlay = true;
    private DefaultTrackSelector trackSelector = null;
    private TrackGroupArray lastSeenTrackGroupArray = null;
    private DataSource.Factory mediaDataSourceFactory;
    private BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

    private TextView tvSongName, tvSinger;
    private Song song;
    private int position;
    private RelativeLayout mFrameLayout;
    int current = 0;
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
        if (playerView.getPlayer() == null) {

            Log.d(TAG, " init in onCreateView " + position);

        }

        if (isVisible) {
            initPlayerNoty();

            initializePlayer(song.getSonglink());
            player.setPlayWhenReady(true);
            showNotification(mStateBuilder.build());

        } else {
            if(player !=null){
                player.setPlayWhenReady(false);

            }
        }


        if (getActivity() instanceof PlayMusicActivity) {
            current = ((PlayMusicActivity) getActivity()).getCurrentPosition();
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

        playerView.requestFocus();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getActivity(), Util.getUserAgent(getActivity(), "mediaPlayerSample"),
                (TransferListener<? super DataSource>) bandwidthMeter);
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

        player.prepare(mediaSource, !haveStartPosition, false);
        playerView.setPlayer(player);
        playerView.setControllerHideOnTouch(false);

        GetImageFromUrl getImageFromUrl = new GetImageFromUrl(this);
        getImageFromUrl.execute(song.getSongImage());
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart " + position);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume " + position);
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
        mMediaSession.setActive(false);
        if(mNotificationManager !=null){
            mNotificationManager.cancel(song.getSongId());
        }

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

    // Shows the system bars by removing all the flags
// except for the ones that make the content appear under the system bars.

    private void showSystemUI() {
        mFrameLayout.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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
        if (playerView.getDefaultArtwork() == null) {
            bitmap1 = bitmap;
            playerView.setDefaultArtwork(bitmap);
        }

    }


    class PlayerEventListener extends Player.DefaultEventListener {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            switch (playbackState) {
                case Player.STATE_ENDED:
                    if (getActivity() instanceof PlayMusicActivity) {
                        PlayMusicActivity playMusicActivity = (PlayMusicActivity) getActivity();
                        current = playMusicActivity.getCurrentPosition();
                        if (current < playMusicActivity.getSongSize()) {
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
            if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
                mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                        player.getCurrentPosition(), 1f);
                Log.d("onPlayerStateChanged:", "PLAYING");
            } else if((playbackState == ExoPlayer.STATE_READY)){
                mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        player.getCurrentPosition(), 1f);
                Log.d("onPlayerStateChanged:", "PAUSED");
            }else if(playbackState ==ExoPlayer.STATE_ENDED){
                mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                        player.getCurrentPosition(), 1f);
            }
            if(isVisible){
                mMediaSession.setPlaybackState(mStateBuilder.build());
                showNotification(mStateBuilder.build());
            }


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
        isVisible = isVisibleToUser;

        if (!isVisibleToUser) {
            releasePlayer();
            if (song!=null) mNotificationManager.cancel(song.getSongId());
        } else {
            if (song != null) {
                if (playerView.getPlayer() == null) {
                    initPlayerNoty();
                    initializePlayer(song.getSonglink());
                    Log.d(TAG, " init in setUserVisibleHint " + position);

                }
                player.setPlayWhenReady(true);

            }

        }
    }

    private void initPlayerNoty() {
        mMediaSession = new MediaSessionCompat(getActivity(), getActivity().getClass().getSimpleName());
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        token = mMediaSession.getSessionToken();
        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);
        mStateBuilder = new PlaybackStateCompat.Builder().setActions(
                PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT);

        mMediaSession.setPlaybackState(mStateBuilder.build());
        mMediaSession.setCallback(new MySessionCallback());
        mMediaSession.setActive(true);

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {

            player.setPlayWhenReady(true);

        }

        @Override
        public void onPause() {
                player.setPlayWhenReady(false);

        }

        @Override
        public void onSkipToPrevious() {

            PlayMusicActivity playMusicActivity = (PlayMusicActivity) getActivity();
            current = playMusicActivity.getCurrentPosition();
            if (current > 0) {
                playMusicActivity.setPrevioudFragment(current);
            }

        }

        @Override
        public void onSkipToNext() {

            PlayMusicActivity playMusicActivity = (PlayMusicActivity) getActivity();
            current = playMusicActivity.getCurrentPosition();
            if (current < playMusicActivity.getSongSize()) {
                playMusicActivity.setNextFragment(current);
            }

        }
    }


    private void showNotification(PlaybackStateCompat state) {

        boolean isCancel = false;
        Log.d("noti", song.getSongName());
        // You only need to create the channel on API 26+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), CHANNEL_ID);

        int icon;
        String play_pause;
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            play_pause = "Pause";
            isCancel = true;
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause ="Play";
            isCancel = false;
        }

        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(getActivity(),
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action previous = new NotificationCompat.Action(
                R.drawable.exo_controls_previous, "Previou",
                MediaButtonReceiver.buildMediaButtonPendingIntent(getActivity(),
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));


        NotificationCompat.Action nextAction = new NotificationCompat.Action(
                R.drawable.exo_controls_next, "Next",
                MediaButtonReceiver.buildMediaButtonPendingIntent(getActivity(),
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (getActivity(), 0, new Intent(getActivity(), PlayMusicActivity.class), 0);



        builder.setContentTitle(song.getSongName())
                .setContentText(song.getSinger())
                .setContentIntent(contentPendingIntent)
                .setSmallIcon(R.drawable.exo_notification_play)
                .setLargeIcon(bitmap1)
                .setOngoing(isCancel)
                .setColorized(true)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(previous)
                .addAction(playPauseAction)
                .addAction(nextAction)
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                        .setMediaSession(token)
                        .setShowActionsInCompactView(0, 1, 2));

        mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(song.getSongId(), builder.build());
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients
     */

    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("minhnqq","MediaReceiver");
            MediaButtonReceiver.handleIntent(mMediaSession, intent);
        }
    }

    /**
     * The NotificationCompat class does not create a channel for you. You still have to create a channel yourself
     */

    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        // The id of the channel.
        String id = CHANNEL_ID;
        // The user-visible name of the channel.
        CharSequence name = "Media playback";
        // The user-visible description of the channel.
        String description = "Media playback controls";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.setShowBadge(false);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        mNotificationManager.createNotificationChannel(mChannel);
    }


    public void cancelAllNoit(){
        if(mNotificationManager != null){
            mNotificationManager.cancelAll();
        }
    }
}
