package com.example.wille.willing_audio;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;

import com.example.wille.willing_audio.Model.Song;
import com.example.wille.willing_audio.Model.SongList;

import static com.example.wille.willing_audio.SongListDetail.getSongList;

/**
 * Created by Administrator on 2017\11\14 0014.
 */

public class MusicService extends Service {
    private IBinder mbinder=new MyBinder();
    public static MediaPlayer mp=new MediaPlayer();
    //private String [] path = {"", "data/melt.mp3","data/thesoundofsilence.mp3", "data/1464007428.mp3"};
    private String [] path = {"", "123.mp3","thesoundofsilence.mp3", "k.mp3", "stressedOut.mp3"};
    String s= Environment.getExternalStorageDirectory()+"/data/";
    public enum MOD{Sequential,Random,Loop,Single};
    String base_Uri=new String("http://192.168.199.234:8088/uploads/Song/");
    private MOD mod;
    public static Song currentSong;
    public static String encodeUrl(String url) {
        return Uri.encode(url, "-![.:/,%?&=]");
    }
    @Override
    public void onCreate() {
        try {
            currentSong=getSongList().get();
            System.out.println("initSongURL="+prefix(currentSong.URL));
            if (prefix(currentSong.URL).startsWith("http")) mp.setDataSource(encodeUrl(prefix(currentSong.URL)));
            else mp.setDataSource(currentSong.URL);
            mp.prepare();
            mp.setVolume(1f, 1f);
            mod= MOD.Sequential;
        } catch (Exception e) {
            e.printStackTrace();
        }
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                //next();
            }
        });
    }
    private String prefix(String name) {
        return base_Uri+name;
    }

    private void play() {
        mp.start();
        Intent intent=new Intent("statePlay");
        sendBroadcast(intent);
    }
    private void pause() {
        if (mp.isPlaying()) mp.pause();
        Intent intent=new Intent("statePause");
        sendBroadcast(intent);
        System.out.println("send intent statePause");
    }
    private void playOrPause() {
        if (mp.isPlaying()) {
            mp.pause();
            Intent intent=new Intent("statePause");
            sendBroadcast(intent);
        }
        else {
            mp.start();
            Intent intent=new Intent("statePlay");
            sendBroadcast(intent);
        }
    }
    private void setPos(int i) {
        if (mp!=null) {
            mp.seekTo(i);
        }
    }
    private int getPos() {
        if (mp!=null)
            return mp.getCurrentPosition();
        else return 0;
    }
    private int getDur() {
        if (mp!=null)
            return mp.getDuration();
        else return 0;
    }
    private void changeMod() {
        int i=0;
        switch (mod) {
            case Sequential:
                mod= MOD.Random;
                i=1;
                break;
            case Random:
                mod= MOD.Loop;
                i=2;
                break;
            case Loop:
                mod= MOD.Single;
                i=3;
                break;
            case Single:
                mod= MOD.Sequential;
                i=0;
                break;
        }
        Intent intent=new Intent("mod");
        Bundle bundle=new Bundle();
        bundle.putInt("mod", i);
        intent.putExtras(bundle);
        sendBroadcast(intent);
    }
    private void pre() {
        try {
            //Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
            mp.reset();
            currentSong=getSongList().pre(mod);
            if (prefix(currentSong.URL).startsWith("http")) {
                mp.setDataSource(encodeUrl(prefix(currentSong.URL)));
            }
            else {
                mp.setDataSource(currentSong.URL);
            }
            mp.prepare();
            Intent intent=new Intent("pre");
            System.out.println("preSongURL="+currentSong.URL);
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void next(int mark) {
        try {
            //Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
            System.out.println("music service next received");
            mp.reset();
            currentSong=getSongList().next(mod);
            if (prefix(currentSong.URL).startsWith("http")) {
                mp.setDataSource(encodeUrl(prefix(currentSong.URL)));
            }
            else {
                mp.setDataSource(currentSong.URL);
            }
            mp.prepare();
            if (mark==-1) mp.start();
            Intent intent=new Intent("next");
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void playI(int i, int mark) {
        try {
            //Toast.makeText(getApplicationContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
            mp.reset();
            currentSong=getSongList().playI(i);
            System.out.println(i+" song URL="+prefix(currentSong.URL));
            if (currentSong.URL.startsWith(Environment.getExternalStorageDirectory().toString())) {
                mp.setDataSource(currentSong.URL);
                System.out.println("is local="+currentSong.URL);
            }
            else {
                mp.setDataSource(encodeUrl(prefix(currentSong.URL)));
                System.out.println("is net="+encodeUrl(prefix(currentSong.URL)));
            }
            mp.prepare();
            if (mark==-1) mp.start();
            Intent intent=new Intent("next");
            sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mbinder;
    }

    public class MyBinder extends Binder{
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            switch (code) {
                //play or pause
                case 100:
                    reply.setDataPosition(0);
                    reply.writeInt(mp.isPlaying()?1:0);
                    break;
                case 101:
                    play();
                    break;
                case 107:
                    pause();
                    break;
                //previous song
                case 102:
                    pre();
                    break;
                //next song
                case 103:
                    System.out.println("onTransact next");
                    data.setDataPosition(0);
                    next(data.readInt());
                    break;
                //get song position
                case 104:
                    reply.setDataPosition(0);
                    reply.writeInt(getPos());
                    break;
                //set song Duration
                case 105:
                    data.setDataPosition(0);
                    setPos(data.readInt());
                    break;
                //get song duration
                case 106:
                    reply.setDataPosition(0);
                    reply.writeInt(getDur());
                    break;
                case 108:
                    changeMod();
                    break;
                case 109:
                    int i=0;
                    data.setDataPosition(0);
                    i=data.readInt();
                    playI(i, data.readInt());
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
