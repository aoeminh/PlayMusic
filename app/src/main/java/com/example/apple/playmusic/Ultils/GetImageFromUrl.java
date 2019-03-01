package com.example.apple.playmusic.Ultils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.BitSet;

public class GetImageFromUrl extends AsyncTask<String,Void, Bitmap> {
    private IOnGetBitmap mIOnGetBitmap;
    public GetImageFromUrl(IOnGetBitmap i){
        this.mIOnGetBitmap =i;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        String strUrl = strings[0];
        Bitmap bitmap = null;
        try {
            URL url = new URL(strUrl);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        mIOnGetBitmap.getBitmap(bitmap);
    }

    public interface IOnGetBitmap{
        void getBitmap(Bitmap bitmap);
    }
}
