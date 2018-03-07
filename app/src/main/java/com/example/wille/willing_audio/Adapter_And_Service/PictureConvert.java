package com.example.wille.willing_audio.Adapter_And_Service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2018/1/1.
 */

public class PictureConvert {
    public static Bitmap ConvertUrltoBitmap(String url){
        URL myFileUrl = null;
        Bitmap bitmap = null;
        try {
            myFileUrl = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            Log.e("pic","here1");
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            Log.e("pic","here1.5");
            conn.setDoInput(true);
            Log.e("pic","here1.7");
            conn.connect();
            Log.e("pic","here2");
            InputStream is = conn.getInputStream();
            Log.e("pic","here3");
            bitmap = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("pic","haha"+bitmap.toString());
        return bitmap;
    }
}
