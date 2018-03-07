package com.example.wille.willing_audio.Factory2;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Administrator on 2017/12/26.
 */

public class ServiceFactory2 {
    private Retrofit retrofit;
    public ServiceFactory2(String baseUrl){
        retrofit=createRetrofit(baseUrl);
    }
    public Retrofit getRetrofit(){
        return retrofit;
    }
    private static Retrofit createRetrofit(String baseUrl){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())//将Json结果并解析成model类的格式
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//Service接口默认返回Call<T> 要想返回Observable<T>,需得加上这一句
                .client(createOkHttp())
                .build();
    }
    private static OkHttpClient createOkHttp(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)//连接超时时间
                .readTimeout(30,TimeUnit.SECONDS)//写操作 超时时间
                .writeTimeout(10,TimeUnit.SECONDS)//读操作超时时间
                .build();
        return okHttpClient;
    }
}
