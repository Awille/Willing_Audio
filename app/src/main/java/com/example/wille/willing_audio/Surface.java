package com.example.wille.willing_audio;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import java.io.File;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.wille.willing_audio.MusicService.currentSong;
import static com.example.wille.willing_audio.MusicService.encodeUrl;
import static com.example.wille.willing_audio.MusicService.mp;

/**
 * Created by Administrator on 2017\12\29 0029.
 */

public class Surface extends Fragment {
    private int currentLevel;
    private View v;
    private ImageView album1,album2, album3;
    private ImageView circle1, circle2, circle3;
    private ImageView hander;
    private ObjectAnimator anim;
    private BroadcastReceiver br;
    private ServiceConnection mConnection;
    private IBinder mBinder;
    private Parcel data=Parcel.obtain();
    private Parcel reply=Parcel.obtain();
    private boolean mark=false;
    private ValueAnimator animator1;
    private ValueAnimator animator2;
    private final int Duration = 500;  // 动画时长
    private boolean isFirstAlbum=true;
    private Handler mHandler;
    private float initX1, initX2, initX3;
    private float cinitX1, cinitX2, cinitX3;
    private ContentResolver resolver;
    private Bitmap b;
    public static Surface newInstance() {
        Surface array= new Surface();
        return array;
    }

    class albumStateChangeReceiver extends BroadcastReceiver {
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("fragment1 received "+intent.getAction());
            if (intent.getAction().equals("pause")) {
                pause();
            }
            else if (intent.getAction().equals("play")) {
                plays();
            }
            else if (intent.getAction().equals("next")) {
                System.out.println("next received");
                nextSong();
            }
            else if (intent.getAction().equals("pre")) {
                preSong();
            }
            else if (intent.getAction().equals("changeAlbum")) {
                //album2.setImageResource();
                nextSong();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentLevel=0;
        System.out.println("br register");
        br= new albumStateChangeReceiver();
        IntentFilter filter = new IntentFilter();

        System.out.println("****->"+initX1+" "+initX2+" "+initX3);
        filter.addAction("play");
        filter.addAction("pause");
        filter.addAction("next");
        filter.addAction("pre");
        filter.addAction("changeAlbum");
        getContext().registerReceiver(br, filter);
        animator1=new ValueAnimator();
        animator2=new ValueAnimator();
        mHandler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 121:
                        shiftPre();
                        break;
                    case 122:
                        shiftNext();
                        break;
                    case 123:
                        reset();
                        plays();
                        break;
                    //从服务器下载图片完毕
                    case 130:
                        if (msg.obj!=null) {
                            album2.setImageBitmap((Bitmap)msg.obj);
                            System.out.println("bitmap="+msg.obj.toString());
                        }
                        break;
                    //从服务器下载图片完毕
                    case 131:
                        if (msg.obj!=null) {
                            album3.setImageBitmap((Bitmap)msg.obj);
                            System.out.println("bitmap="+msg.obj.toString());
                        }
                        break;
                    case 132:
                        if (msg.obj!=null) {
                            album1.setImageBitmap((Bitmap)msg.obj);
                            System.out.println("bitmap="+msg.obj.toString());
                        }
                        break;
                }
            }
        };
    }
    private boolean flag=false;

    private void reset() {
        if (!currentSong.songAvator.startsWith(Environment.getExternalStorageDirectory().toString())) {
            album1.setImageBitmap(b);
        }
        else album1.setImageURI(Uri.fromFile(new File(currentSong.songAvator)));
        album1.setX(initX1);
        album2.setX(initX2);
        album3.setX(initX3);
        circle1.setX(cinitX1);
        circle2.setX(cinitX2);
        circle3.setX(cinitX3);
        anim.end();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAnim() {
        if (anim.isStarted()) anim.resume();
        else anim.start();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pauseAnim(){
        anim.pause();
    }
    private ObjectAnimator a1, a2, a3, a4;
    private void shiftNext() {
        AnimatorSet a=new AnimatorSet();
        if (!flag) {
            initX2=album2.getX();
            initX1=album1.getX();
            initX3=album3.getX();
            cinitX1=circle1.getX();
            cinitX2=circle2.getX();
            cinitX3=circle3.getX();
            System.out.println("****->"+initX1+" "+initX2+" "+initX3);
            flag=true;
        }
        a1=ObjectAnimator.ofFloat(album1, "X", initX1, 2*initX1-initX2);//k-a1=a1-a2
        a2=ObjectAnimator.ofFloat(album2, "X", initX2, initX1);
        a3=ObjectAnimator.ofFloat(circle1, "X", cinitX1, 2*cinitX1-cinitX2);//k-a1=a1-a2
        a4=ObjectAnimator.ofFloat(circle2, "X", cinitX2, cinitX1);
        a.play(a1).with(a2).with(a3).with(a4);
        a.setDuration(300);
        a.start();
    }

    private void shiftPre() {
        AnimatorSet a=new AnimatorSet();
        if (!flag) {
            initX2=album2.getX();
            initX1=album1.getX();
            initX3=album3.getX();
            cinitX1=circle1.getX();
            cinitX2=circle2.getX();
            cinitX3=circle3.getX();
            System.out.println("****->"+initX1+" "+initX2+" "+initX3);
            flag=true;
        }
        a1=ObjectAnimator.ofFloat(album1, "X", initX1, 2*initX1-initX3);//k-a1=a1-a2
        a2=ObjectAnimator.ofFloat(album3, "X", initX3, initX1);
        a3=ObjectAnimator.ofFloat(circle1, "X", cinitX1, 2*cinitX1-cinitX3);//k-a1=a1-a2
        a4=ObjectAnimator.ofFloat(circle3, "X", cinitX3, cinitX1);
        a.play(a1).with(a2).with(a3).with(a4);
        a.setDuration(300);
        a.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在这里加载每个 fragment的显示的 View
        v = inflater.inflate(R.layout.surface, container, false);

        album1=(ImageView) v.findViewById(R.id.album1);
        album2=(ImageView) v.findViewById(R.id.album2);
        album3=(ImageView) v.findViewById(R.id.album3);
        circle1=(ImageView) v.findViewById(R.id.circle1);
        circle2=(ImageView) v.findViewById(R.id.circle2);
        circle3=(ImageView) v.findViewById(R.id.circle3);
        hander=(ImageView) v.findViewById(R.id.hander) ;
        anim=ObjectAnimator.ofFloat(album1, "rotation", 0f, 360f);
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setDuration(30000);
        anim.setInterpolator(new LinearInterpolator());

        if (mp.isPlaying()) plays();


        if (!currentSong.songAvator.startsWith(Environment.getExternalStorageDirectory().toString())) {
            new Thread() {
                @Override
                public void run() {
                    if (currentSong.songAvator.startsWith(Environment.getExternalStorageDirectory().toString())) {
                        b = getUrlImage(currentSong.songAvator);
                        System.out.println("localsongAvator="+ currentSong.songAvator);
                    }
                    else {
                        b=getUrlImage(encodeUrl(prefix(currentSong.songAvator)));
                        System.out.println("NetsongAvator="+ encodeUrl(prefix(currentSong.songAvator)));
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 132;
                    msg.obj = b;
                    mHandler.sendMessage(msg);
                }
            }.start();
        }
        else {
            album1.setImageURI(Uri.fromFile(new File(currentSong.songAvator)));
        }



        mConnection = new ServiceConnection(){ // ServiceConnection实例化
            @Override
            public void onServiceConnected(ComponentName name, IBinder
                    service) {
                mBinder = service;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mConnection = null;
            }
        };
        Intent intent = new Intent(getContext(),MusicService.class);
// 绑定activity和服务
        getContext().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        getContext().unregisterReceiver(br);
    }

    @Override
    public void onDestroy(){
        Log.d("ZHFF", "onDestroy:F1 ");
        super.onDestroy();
       // getContext().unregisterReceiver(br);
    }


    private void plays() {
        if (animator2.isStarted()) animator2.cancel();
        //animator2.cancel();
       animator1 = ValueAnimator.ofInt(currentLevel, 10000);
        animator1.setInterpolator(new DecelerateInterpolator());
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentLevel = (int) animation.getAnimatedValue();
                hander.getDrawable().setLevel(currentLevel);
            }
        });
        animator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onAnimationEnd(Animator animation) {
                startAnim();
                int code=101;
                try {
                    mBinder.transact(code, data, reply, 0);
                } catch (Exception e) {e.printStackTrace();}
            }
        });
        animator1.setDuration(Duration*(10000-currentLevel)/10000);
        animator1.start();
    }

    /**
     * 暂停动画
     * */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void pause() {
        if (animator1.isStarted()) animator1.cancel();
        pauseAnim();
        int code=107;
        try {
            mBinder.transact(code, data, reply, 0);
        } catch (Exception e) {e.printStackTrace();}
        animator2 = ValueAnimator.ofInt(currentLevel, 0);
        animator2.setInterpolator(new DecelerateInterpolator());
        animator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentLevel = (int) animation.getAnimatedValue();
                hander.getDrawable().setLevel(currentLevel);
            }
        });
        animator2.setDuration(Duration*currentLevel/10000);
        animator2.start();
    }

    //加载图片
    public Bitmap getUrlImage(String url) {
        Bitmap img = null;
        try {
            URL picurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection)picurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            img = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }


    String base_Uri=new String("http://192.168.199.234:8088/uploads/SongAvator/");
    private String prefix(String name) {
        return base_Uri+name;
    }
    /**
     * 停止动画 ， 主要用于切歌
     * */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void nextSong() {

        final Thread mThread=new Thread() {
            @Override
            public void run() {
                try {
                    sleep(300);
                    mHandler.obtainMessage(122).sendToTarget();
                    sleep(400);
                    mHandler.obtainMessage(123).sendToTarget();

                } catch (Exception e){e.printStackTrace();System.out.println(e.getMessage());}
            }
        };
        System.out.println("songAvator="+ currentSong.songAvator);
        pause();
        if (!currentSong.songAvator.startsWith(Environment.getExternalStorageDirectory().toString())) {
            new Thread() {
                @Override
                public void run() {
                    if (currentSong.songAvator.startsWith(Environment.getExternalStorageDirectory().toString())) {
                        b = getUrlImage(currentSong.songAvator);
                        System.out.println("localsongAvator="+ currentSong.songAvator);
                    }
                    else {
                        b=getUrlImage(encodeUrl(prefix(currentSong.songAvator)));
                        System.out.println("NetsongAvator="+ encodeUrl(prefix(currentSong.songAvator)));
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 130;
                    msg.obj = b;
                    mHandler.sendMessage(msg);
                    mThread.start();
                }
            }.start();
        }
        else {
            album2.setImageURI(Uri.fromFile(new File(currentSong.songAvator)));
            mThread.start();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void preSong() {
        final Thread mThread=new Thread() {
            @Override
            public void run() {
                try {
                    sleep(300);
                    mHandler.obtainMessage(121).sendToTarget();
                    sleep(400);
                    mHandler.obtainMessage(123).sendToTarget();

                } catch (Exception e){e.printStackTrace();System.out.println(e.getMessage());}
            }
        };
        System.out.println("songAvator="+ currentSong.songAvator);
        pause();
        if (!currentSong.songAvator.startsWith(Environment.getExternalStorageDirectory().toString())) {
            new Thread() {
                @Override
                public void run() {
                    if (currentSong.songAvator.startsWith(Environment.getExternalStorageDirectory().toString())) {
                        b = getUrlImage(currentSong.songAvator);
                        System.out.println("localsongAvator="+ currentSong.songAvator);
                    }
                    else {
                        b=getUrlImage(encodeUrl(prefix(currentSong.songAvator)));
                        System.out.println("NetsongAvator="+ encodeUrl(prefix(currentSong.songAvator)));
                    }
                    Message msg = mHandler.obtainMessage();
                    msg.what = 131;
                    msg.obj = b;
                    mHandler.sendMessage(msg);
                    mThread.start();
                }
            }.start();
        }
        else {
            album3.setImageURI(Uri.fromFile(new File(currentSong.songAvator)));
            mThread.start();
        }
    }
}
