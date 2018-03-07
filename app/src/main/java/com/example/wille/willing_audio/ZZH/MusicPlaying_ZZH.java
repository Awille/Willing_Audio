package com.example.wille.willing_audio.ZZH;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wille.willing_audio.Adapter_And_Service.MusicService;
import com.example.wille.willing_audio.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;

public class MusicPlaying_ZZH extends AppCompatActivity {

    static int State;
    static int CurPos;
    static int Totaltime;
    static int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musicplaying);
        Log.d("LOL", "here");
       // main();
    }
//
//    private void main() {
//        //启动activity时就通过Context.bindService()方法启动service
//        Intent intent1=getIntent();
//        index=intent1.getIntExtra("position",1);
//        Bundle bundle = getIntent().getExtras();
//        ArrayList bundlelist = bundle.getParcelableArrayList("bundlelist");
//        //从List中将参数转回 List<Map<String, Object>>
//        final ArrayList<Map<String, Object>> list= (ArrayList<Map<String, Object>>)bundlelist.get(0);
//        String musicSource=list.get(index).get("uri").toString();
//        Toast.makeText(this,musicSource,Toast.LENGTH_SHORT).show();
//        try{
//            MusicService.mp.reset();
//            MusicService.mp.setDataSource(musicSource);
//            MusicService.mp.prepare();
//            MusicService.mp.start();
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//
//
//        ImageView img = (ImageView) findViewById(R.id.img);
//        final ObjectAnimator animator = ObjectAnimator.ofFloat(img, "rotation", 0, 359);
//        animator.setInterpolator(new LinearInterpolator());//线性匀速
//        animator.setRepeatCount(ValueAnimator.INFINITE);//无限循环
//        animator.setDuration(9000);
//        animator.start();
//        State = 1;
//
//        final Button state_btn = (Button) findViewById(R.id.state_btn);
//        Button stop_btn = (Button) findViewById(R.id.stop_btn);
//        Button quit_btn = (Button) findViewById(R.id.quit_btn);
//        final TextView statetext = (TextView) findViewById(R.id.statetext);
//        state_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (State == 0)//初始状态，图片为静止，点击按钮后图片开始旋转，按钮变为paused
//                {
//                    animator.start();
//                    State = 1;
//                    state_btn.setText("PAUSED");
//                    statetext.setText("Playing");
//                    try {
//                        int code = 101;
//                        Parcel data = Parcel.obtain();
//                        Parcel reply = Parcel.obtain();
//                        MainActivity.mBinder.transact(code, data, reply, 0);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                } else if (State == 1)//图片在旋转，点击按钮后图片暂停旋转，按钮变为play
//                {
//                    animator.pause();
//                    State = 2;
//                    state_btn.setText("PLAY");
//                    statetext.setText("Paused");
//                    try {
//                        int code = 102;
//                        Parcel data = Parcel.obtain();
//                        Parcel reply = Parcel.obtain();
//                        MainActivity.mBinder.transact(code, data, reply, 0);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                } else if (State == 2)//图片暂停旋转，点击按钮后图片恢复旋转，按钮变为paused
//                {
//                    animator.resume();
//                    State = 1;
//                    state_btn.setText("PAUSED");
//                    statetext.setText("Playing");
//                    try {
//                        int code = 101;
//                        Parcel data = Parcel.obtain();
//                        Parcel reply = Parcel.obtain();
//                        MainActivity.mBinder.transact(code, data, reply, 0);
//                    } catch (RemoteException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        stop_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                animator.end();
//                State = 0;
//                state_btn.setText("PLAY");
//                statetext.setText("Stopped");
//                try {
//                    int code = 103;
//                    Parcel data = Parcel.obtain();
//                    Parcel reply = Parcel.obtain();
//                    MainActivity.mBinder.transact(code, data, reply, 0);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
////        quit_btn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                unbindService(sc);
////                sc = null;
////                try {
////                    MusicPlaying_ZZH.this.finish();
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////            }
////        });
//        final SimpleDateFormat time = new SimpleDateFormat("mm:ss");
//        final MusicService ms = new MusicService();
//        final SeekBar seekBar = (SeekBar) findViewById(R.id.bar);
//        final TextView totalTime = (TextView) findViewById(R.id.total_time);
//        totalTime.setText(time.format(ms.mp.getDuration()));
//        final TextView currentTime = (TextView) findViewById(R.id.cur_time);
//        final Handler mHandler = new Handler() {
//            @Override
//            public void handleMessage(Message msg) {
//                super.handleMessage(msg);
//                switch (msg.what) {
//                    case 123:
//                        seekBar.setProgress(CurPos);
//                        seekBar.setMax(Totaltime);
//                        currentTime.setText(time.format(CurPos));
//                        break;
//                }
//            }
//        };
//
//        Thread mThread = new Thread() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        Thread.sleep(100);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    try {
//                        int code = 104;
//                        Parcel data = Parcel.obtain();
//                        Parcel reply = Parcel.obtain();
//                        MainActivity.mBinder.transact(code, data, reply, 0);
//                        CurPos = reply.readInt();
//                        Totaltime = reply.readInt();
//                        mHandler.obtainMessage(123).sendToTarget();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        };
//        mThread.start();//这句话666，在pdf上超级隐秘,无敌
//
//        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {//该方法拖动进度条进度改变的时候调用
//                if (fromUser == true) //加这个判断条件判断是否人为改变滑动条
//                {
//                    try {
//                        int code = 105;
//                        Parcel data = Parcel.obtain();
//                        Parcel reply = Parcel.obtain();
//                        data.writeInt(progress);
//                        MainActivity.mBinder.transact(code, data, reply, 0);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {//该方法拖动进度条开始拖动的时候调用。
//                try {
//                    int code = 102;
//                    Parcel data = Parcel.obtain();
//                    Parcel reply = Parcel.obtain();
//                    MainActivity.mBinder.transact(code, data, reply, 0);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {//该方法拖动进度条停止拖动的时候调用
//                try {
//                    int code = 101;
//                    Parcel data = Parcel.obtain();
//                    Parcel reply = Parcel.obtain();
//                    MainActivity.mBinder.transact(code, data, reply, 0);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        MusicService.mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//                Toast.makeText(getApplicationContext(),"播放完毕",Toast.LENGTH_SHORT).show();
//                index++;
//                try{
//                    MusicService.mp.reset();
//                    MusicService.mp.setDataSource(list.get(index).get("uri").toString());
//                    MusicService.mp.prepare();
//                    MusicService.mp.start();
//                    totalTime.setText(time.format(ms.mp.getDuration()));
//                }catch(Exception e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
}