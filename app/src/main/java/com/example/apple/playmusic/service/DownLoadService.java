package com.example.apple.playmusic.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.apple.playmusic.activity.SongListActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownLoadService extends IntentService {

    public DownLoadService() {
        super("DownLoadService");
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {

            String strurl = intent.getStringExtra(SongListActivity.EXTRA_DOWNLOAD_URL);
            String filename = intent.getStringExtra(SongListActivity.EXTRA_DOWNLOAD_FILE_NAME);
            int count;
            File file = getPublicAlbumStorageDir(filename);
            Intent intentDownload = new Intent(SongListActivity.ACTION_DOWNLOAD_PROCESS);

            try {
                if (file.exists()) {
                    file.delete();
                    file.createNewFile();
                }
                URL url = new URL(strurl);
                URLConnection connection = url.openConnection();
                connection.connect();
                // this will be useful so that you can show a tipical 0-100% progress bar
                int lenghtOfFile = connection.getContentLength();

                // downlod the file
                InputStream input = new BufferedInputStream(url.openStream());
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;
                int current = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    int newValue= (int) (total * 100 / lenghtOfFile);
                    if(newValue>current){
                        current =newValue;
                        intentDownload.putExtra(SongListActivity.EXTRA_DOWNLOAD_PROCESS,current);
                        sendBroadcast(intentDownload);
                    }
                    output.write(data, 0, count);

                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public File getPublicAlbumStorageDir(String songname) {
        // Get the directory for the user's public music directory.
        // need add .mp3 to devide detech file
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC), songname + ".mp3");
        if (!file.mkdirs()) {
            Log.e("minhnqq", "Directory not created");
        }
        return file;
    }
}
