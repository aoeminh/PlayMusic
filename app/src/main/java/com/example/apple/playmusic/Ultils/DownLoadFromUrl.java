package com.example.apple.playmusic.Ultils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.apple.playmusic.R;
import com.example.apple.playmusic.activity.SongListActivity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DownLoadFromUrl extends AsyncTask<String, Integer, Boolean> {
    Context mContext;
    NotificationManagerCompat notificationManagerCompat;
    NotificationCompat.Builder builder;
    String filename="";
    File file;
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
        builder = new NotificationCompat.Builder(mContext, "d");
        builder.setContentTitle("Download "+ filename)
                .setOngoing(false)
                .addAction(R.drawable.exo_icon_play,"Cancel",createPendingIntent())
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setSmallIcon(R.drawable.exo_icon_play);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
        builder.setProgress(MAX, CURRENT, false);
        notificationManagerCompat.notify(SongListActivity.NOTIFY_ID, builder.build());

    }

    @Override
    protected Boolean doInBackground(String... urlParams) {
        int count;
         file = getPublicAlbumStorageDir(filename);

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
            notificationManagerCompat.notify(SongListActivity.NOTIFY_ID, builder.build());
        }

    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        Intent endDownloadIntent = new Intent("end");
        endDownloadIntent.putExtra("end",false);
        mContext.sendBroadcast(endDownloadIntent);
        if(builder !=null){
            notificationManagerCompat.cancel(1);
        }
        if(aBoolean){
            Toast.makeText(mContext,"Tải xuống thành công",Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(mContext,"Tải xuống thất bại",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCancelled() {

        super.onCancelled();
        if(file.exists()) file.delete();
        if(builder !=null){
            notificationManagerCompat.cancel(1);
        }
    }

    public File getPublicAlbumStorageDir(String songname) {
        // Get the directory for the user's public music directory.
        // need add .mp3 to devide detech file
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC), songname + ".mp3");

        return file;
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        // The id of the channel.
        String id = "d";
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

    public PendingIntent createPendingIntent(){
        Intent intent = new Intent(mContext,SongListActivity.class);
        intent.setAction("cancel");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        return pendingIntent;
    }
}
