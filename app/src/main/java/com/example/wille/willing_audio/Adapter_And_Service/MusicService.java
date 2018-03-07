package com.example.wille.willing_audio.Adapter_And_Service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class MusicService extends Service {
    public static MediaPlayer mp=new MediaPlayer();
    private MyBinder mBinder = new MyBinder();//实例化MyBinder类
    public class MyBinder extends Binder {//在Service类里定义一个Binder类,类中的方法就是我们可以控制MusicService执行的方法
        @Override
        protected boolean onTransact(int code, Parcel data,Parcel reply,int flags)throws RemoteException{
            switch (code)
            {
                case 101://播放按钮
                    mp.start();
                    break;
                case 102://暂停按钮
                    mp.pause();
                    break;
                case 103://停止按钮
                    mp.stop();
                    try{
                        mp.prepare();
                        mp.seekTo(0);
                    }catch (Exception e){
                        e.printStackTrace();//public void printStackTrace()将此 throwable 及其追踪输出至标准错误流
                    }
                    break;
                case 104://获取更新时间
                    try{
                        int curtime=mp.getCurrentPosition();
                        int totaltime=mp.getDuration();
                        reply.writeInt(curtime);
                        reply.writeInt(totaltime);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case 105://拖动进度条
                    try{
                        int i=data.readInt();
                        mp.seekTo(i);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
            }
            return super.onTransact(code,data,reply,flags);
        }
    }
    public MusicService() {//创建mediaplayer并初始化
        try{
            System.out.println(Environment.getExternalStorageDirectory());
            mp.setDataSource(Environment.getExternalStorageDirectory()+"/MusicBox/only my railgun.mp3");
            //mp.setDataSource("/storage/3461-6430/Android/data/com.netease.cloudmusic/files/Documents/Music/五河琴里 - Lollipop Baby.mp3");
            mp.prepare();
            mp.setLooping(true);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    @Override
    public IBinder onBind(Intent intent) {//onBind返回的对象操作Service控制MediaPlayer
        return mBinder;
    }
    //释放服务中使用的资源，不要造成内存泄露
    @Override
    public boolean onUnbind(Intent intent){
        mp.stop();
        mp.release();
        return false;
    }
    @Override
    public void onDestroy() {
        mp.stop();
        mp.release();
        super.onDestroy();
    }
}
