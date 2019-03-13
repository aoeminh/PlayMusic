package com.example.apple.playmusic.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

public class ExoPlayerService extends Service {

    private Binder binder = new ExoplayerBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    class ExoplayerBinder extends Binder {
        ExoPlayerService getExoPlayerService(){
            return ExoPlayerService.this;
        }
    }
}
