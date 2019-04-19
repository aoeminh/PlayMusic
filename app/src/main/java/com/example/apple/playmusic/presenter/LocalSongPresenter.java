package com.example.apple.playmusic.presenter;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.apple.playmusic.contract.ILocalPresenter;
import com.example.apple.playmusic.contract.IlocalView;
import com.example.apple.playmusic.model.Song;

import java.util.ArrayList;

public class LocalSongPresenter implements ILocalPresenter {
    IlocalView view;
    Context mContext;
    ArrayList<Song> songs = new ArrayList<>();

    public LocalSongPresenter(IlocalView ilocalView,Context context){
        this.view =ilocalView;
        this.mContext = context;
    }

    @Override
    public void getAllSongFromPhone(boolean isCheckPermision) {
        if(isCheckPermision){
            view.responseAllSongFromPhone(scanDeviceForMp3Files());
        }

    }

    private ArrayList<Song> scanDeviceForMp3Files(){
        ArrayList<Song> songs = new ArrayList<>();
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DURATION,

        };
        final String sortOrder = MediaStore.Audio.AudioColumns.TITLE + " COLLATE LOCALIZED ASC";
        Cursor cursor = null;
        try {
            Uri uri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            cursor = mContext.getContentResolver().query(uri, projection, selection, null, sortOrder);
            if( cursor != null){
                cursor.moveToFirst();

                while( !cursor.isAfterLast() ){
                    String title = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String path = cursor.getString(2);
                    String displayName  = cursor.getString(3);
                    String songDuration = cursor.getString(4);
                    cursor.moveToNext();
                    if(path != null && path.endsWith(".mp3")) {
                        Song song =new Song();
                        song.setSinger(artist);
                        song.setSongName(title);
                        song.setSonglink(path);
                        songs.add(song);
                    }
                }
                Log.e("minhnhnh", songs.get(0).getSonglink());
            }

        } catch (Exception e) {
            Log.e("minhnhnh", e.toString());
        }finally{
            if( cursor != null){
                cursor.close();
            }
        }
        return songs;
    }
}
