package com.example.wille.willing_audio.Adapter_And_Service;

import android.app.Application;

/**
 * Created by Administrator on 2017/12/28.
 */

public class MyApplication extends Application {
    private String Parameter1;
    public void setParameter1(String str){
        Parameter1=str;
    }
    public String getParameter1(){
        return Parameter1;
    }
}
