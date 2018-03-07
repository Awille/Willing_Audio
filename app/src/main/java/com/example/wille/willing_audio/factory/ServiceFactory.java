package com.example.wille.willing_audio.factory;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wille on 2017/12/14.
 */

public class ServiceFactory {
    public static OkHttpClient createOkHttp(){
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//连接超时
                .readTimeout(30,TimeUnit.SECONDS) //读超时
                .writeTimeout(10,TimeUnit.SECONDS) //写超时
                .build();
        return okHttpClient;
    }

    public static Retrofit createRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create()) //添加GSON Converter
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttp())
                .build();
    }

    public static boolean DownloadImage(ResponseBody responseBody){
        try {
            InputStream in=null;
            FileOutputStream out=null;
            try {
                in=responseBody.byteStream();
                out = new FileOutputStream(Environment.getExternalStorageDirectory()+File.separator  + "AndroidTutorialPoint.jpg");
                Log.d("willingee",Environment.getExternalStorageDirectory() +File.separator + "AndroidTutorialPoint.jpg");
                int c;

                while ((c = in.read()) != -1) {
                    out.write(c);
                }
                Log.d("willingee","hhhhh");

            }
            catch (Exception e){
                e.printStackTrace();
                Log.d("willingee","eee");
                return false;
            }
            finally {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            }
            return true;

        }
        catch (Exception e){
            Log.d("willingee","error1");
            e.printStackTrace();
        }
        return false;
    }


}
