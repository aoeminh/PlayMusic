package com.example.apple.playmusic.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class DownLoadService extends IntentService {

    public DownLoadService() {
        super("DownLoadService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        String url  = intent.getStringExtra("url");
    }
}
