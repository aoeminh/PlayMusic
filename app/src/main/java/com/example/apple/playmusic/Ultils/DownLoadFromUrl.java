package com.example.apple.playmusic.Ultils;

import android.app.NotificationManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.apple.playmusic.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownLoadFromUrl extends AsyncTask<String, Integer, Boolean> {
    Context mContext;
    NotificationManagerCompat notificationManagerCompat;
    NotificationCompat.Builder builder;
    String filename="";
    boolean isDownload = false;

    int MAX = 100;
    int CURRENT = 0;
    public DownLoadFromUrl(Context context,String filename) {
        this.mContext = context;
        this.filename =filename;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Toast.makeText(mContext," Bắt đầu tải xuống",Toast.LENGTH_SHORT).show();
        notificationManagerCompat = NotificationManagerCompat.from(mContext);
        builder = new NotificationCompat.Builder(mContext, "a");
        builder.setContentTitle("Download "+ filename)
                .setOngoing(false)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.drawable.exo_icon_play);


        builder.setProgress(MAX, CURRENT, false);
        notificationManagerCompat.notify(1, builder.build());

    }

    @Override
    protected Boolean doInBackground(String... urlParams) {
        int count;
        File file = getPublicAlbumStorageDir(filename);

        try {
            if(file.exists()){
                file.delete();
                file.createNewFile();
            }
            URL url = new URL(urlParams[0]);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a tipical 0-100% progress bar
            int lenghtOfFile = connection.getContentLength();

            // downlod the file
            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                publishProgress((int) (total * 100 / lenghtOfFile));
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        Log.d("minhnqq",""+ values[0]);
        int newValue = values[0];
        if(newValue > CURRENT){
            CURRENT=newValue;
            builder.setProgress(MAX, CURRENT, false);
            notificationManagerCompat.notify(1, builder.build());
        }

    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(builder !=null){
            notificationManagerCompat.cancel(1);
        }
        if(aBoolean){
            Toast.makeText(mContext,"Tải xuống thành công",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(mContext,"Tải xuống thất bại",Toast.LENGTH_SHORT).show();
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
