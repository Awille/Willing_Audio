package com.example.wille.willing_audio;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static com.example.wille.willing_audio.MusicService.currentSong;
import static com.example.wille.willing_audio.MusicService.mp;
import static com.example.wille.willing_audio.SongListDetail.getSongList;

public  class Player extends AppCompatActivity {
    private SeekBar sb;
    private Button playMod, pre, play, next, playList, back;
    private ServiceConnection mConnection;
    private IBinder mBinder;
    private TextView pos, dur, songName, singer;
    private Parcel data=Parcel.obtain();
    private Parcel reply=Parcel.obtain();
    private TextView listName;
    private ImageView dot1, dot2, dot3;
    private ListView lv;
    private int m1,m2,s1,s2, t1, t2;
    private int flag=0;
    private int songDur;
    private Button comment;
    final private int MaxNo=4;
    private Thread mThread;
    private BroadcastReceiver br;
    public static int pageNum=0;
    private int state=0;
    private boolean showPlayList=false;
    public MyAdapter myAdapter;
    public static List<Map<String, Object>> list;
    public static MySQLiteOpenHelper mySQLiteOpenHelper;
    public static AlertDialog.Builder builder;
    public static String TAG="DEBUGING";
    final private String [] song={"", "Melt", "SoundOfSilence", "@.@", "Stressed Out"};

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    static final int NUM_ITEMS = 3;
    private MyFragAdapter  mAdapter;
    private ViewPager mPager;
    private int nowPage;

    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context,Intent intent)
        {
            if (intent.getAction().equals("pre")||intent.getAction().equals("next")) {
                state=1;
                play.setBackgroundResource(R.drawable.pause);
                songName.setText(getSongList().get().songName);
                singer.setText(getSongList().get().singer);
                flag=0;
            }
            else if (intent.getAction().equals("statePlay")) {
                play.setBackgroundResource(R.drawable.pause);
                state=1;
            }
            else if (intent.getAction().equals("statePause")) {
                System.out.println("statePause received");
                play.setBackgroundResource(R.drawable.play);
                state=0;
            }
            else if (intent.getAction().equals("mod")) {
                Bundle bundle=intent.getExtras();
                Intent intentss=new Intent("addSong");
                Bundle bundle1=new Bundle();
                int mod=bundle.getInt("mod");
                switch (mod) {
                    case 0:
                        playMod.setBackgroundResource(R.drawable.sequential);
                        break;
                    case 1:
                        playMod.setBackgroundResource(R.drawable.random);
                        break;
                    case 2:
                        playMod.setBackgroundResource(R.drawable.loop);
                        break;
                    case 3:
                        playMod.setBackgroundResource(R.drawable.single);
                        break;
                }
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player);
        System.out.println("Player onCreate");
        verifyStoragePermissions(this);
        builder=new  AlertDialog.Builder(Player.this);
        state=0;
        mAdapter = new MyFragAdapter(getSupportFragmentManager() );
        mPager = (ViewPager)findViewById(R.id.vp);
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(1);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                dot1.setImageResource(R.drawable.emptydot);
                dot2.setImageResource(R.drawable.emptydot);
                dot3.setImageResource(R.drawable.emptydot);
                if (position==0) dot1.setImageResource(R.drawable.fulldot);
                else if (position==1) dot2.setImageResource(R.drawable.fulldot);
                else if (position==2) dot3.setImageResource(R.drawable.fulldot);
            }

            @Override
            public void onPageSelected(int position) {
                dot1.setImageResource(R.drawable.emptydot);
                dot2.setImageResource(R.drawable.emptydot);
                dot3.setImageResource(R.drawable.emptydot);
                if (position==0) dot1.setImageResource(R.drawable.fulldot);
                else if (position==1) dot2.setImageResource(R.drawable.fulldot);
                else if (position==2) dot3.setImageResource(R.drawable.fulldot);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        init();
        final Handler mHandler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 123:
                            sb.setProgress(msg.arg1);
                            t1=msg.arg1/1000;
                            m1=t1/60;
                            s1=t1%60;
                            String time1=((m1<10)?"0":"")+String.valueOf(m1)+":"+((s1<10)?"0":"")+String.valueOf(s1);

                            pos.setText(time1);
                            if (flag==1) {
                                sb.setMax(songDur);
                                t2=songDur/1000;
                                m2=t2/60;
                                s2=t2%60;
                                String time2=((m2<10)?"0":"")+String.valueOf(m2)+":"+((s2<10)?"0":"")+String.valueOf(s2);
                                dur.setText(time2);
                                flag=2;
                            }
                        break;
                }
            }
        };

        mThread=new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Message m=mHandler.obtainMessage(123);
                        int code=104;
                        mBinder.transact(code, data, reply, 0);
                        reply.setDataPosition(0);
                        m.arg1=reply.readInt();
                        if (flag==0) {
                            code=106;
                            mBinder.transact(code, data, reply, 0);
                            reply.setDataPosition(0);
                            m.arg2=reply.readInt();
                            songDur=m.arg2;
                            flag=1;
                        }
                        mHandler.sendMessage(m);
                        Thread.sleep(100);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        mThread.start();

    }


    public void validatePlayList() {
        //本地列表填充
        list.clear();
        for (int i=0;i<getSongList().size();++i) {
            Map<String ,Object> map=new HashMap<>();
            map.put("songName", getSongList().getI(i).songName);
            list.add(map);
        }
        listName.setText("全部歌曲");
        myAdapter.notifyDataSetChanged();
    }

    void init() {
        //mySQLiteOpenHelper=new MySQLiteOpenHelper(this, "HeroDatabase.db", null, 2);
        br= new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("statePlay");
        filter.addAction("statePause");
        filter.addAction("mod");
        filter.addAction("pre");
        filter.addAction("next");
        filter.addAction("addSong");
        registerReceiver(br,filter);
        Log.d(TAG, "init: ");
        pos=(TextView) findViewById(R.id.pos);
        dur=(TextView) findViewById(R.id.dur);
        songName=(TextView) findViewById(R.id.songName);
        sb=(SeekBar) findViewById(R.id.sb);
        dot1=(ImageView) findViewById(R.id.dot1);
        dot2=(ImageView) findViewById(R.id.dot2);
        dot3=(ImageView) findViewById(R.id.dot3);
        listName=(TextView) findViewById(R.id.listName);
        playMod=(Button) findViewById(R.id.playMod);
        comment=(Button) findViewById(R.id.comment);
        pre=(Button) findViewById(R.id.pre);
        play=(Button) findViewById(R.id.play);
        next=(Button) findViewById(R.id.next);
        playList=(Button) findViewById(R.id.playList);
        back=(Button) findViewById(R.id.back);
        lv=(ListView) findViewById(R.id.lv);
        singer=(TextView) findViewById(R.id.singer);
        songName.setText(getSongList().get().songName);
        singer.setText(getSongList().get().singer);

        list=new ArrayList<>();

        for (int i=0;i<getSongList().size();++i) {
            Map<String ,Object> map=new HashMap<>();
            map.put("songName", getSongList().getI(i).songName);
            list.add(map);
        }
        if (mp.isPlaying()) {
            play.setBackgroundResource(R.drawable.pause);
            state=1;
        }
//        Cursor cursor=mySQLiteOpenHelper.queryLocal();
//        System.out.println("cnt="+cursor.getCount());
//        while (cursor.moveToNext()) {
//            Map<String ,Object> map=new HashMap<>();
//            map.put("songName", cursor.getString(cursor.getColumnIndex("songName")));
//            System.out.println("songNameMaps="+cursor.getString(cursor.getColumnIndex("songName")));
//            list.add(map);
//        }

        myAdapter=new MyAdapter(list,getApplicationContext(), R.layout.playlist_item);
        lv.setAdapter(myAdapter);
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
        Intent intent = new Intent(this,MusicService.class);
        startService(intent); // 开启服务
// 绑定activity和服务
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "bindservice");
        playMod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBinder.transact(108, data, reply, 0);
                } catch (Exception e) {e.printStackTrace();}
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClickFilter.filter()) return;
                try {
                    String s="";
                   if (state==1) {
                       state=0;
                       s="pause";
                       play.setBackgroundResource(R.drawable.play);
                   }
                   else {
                       state=1;
                       s="play";
                       play.setBackgroundResource(R.drawable.pause);
                   }
                    Intent intent=new Intent(s);
                    sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mBinder.transact(102, data, reply ,0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    System.out.println("Press next");
                    data.setDataPosition(0);
                    data.writeInt(0);
                    data.writeInt(0);
                    mBinder.transact(103, data, reply ,0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        playList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showPlayList) {
                    showPlayList=false;
                    lv.setVisibility(GONE);
                    listName.setVisibility(GONE);
                }
                else {
                    showPlayList=true;
                    lv.setVisibility(View.VISIBLE);
                    listName.setText(getSongList().listName);
                    listName.setVisibility(View.VISIBLE);
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                try {
                    int code=105;
                    data.setDataPosition(0);
                    data.writeInt(seekBar.getProgress());
                    mBinder.transact(code, data, reply, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Player.this, CommentDetail.class);
                Bundle bundle=new Bundle();
                bundle.putString("songId", String.valueOf(currentSong.songId));
                Log.d("willingee", String.valueOf(currentSong.songId));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }



    public static void verifyStoragePermissions(Activity activity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResult) {

    }

    @Override
    public void onDestroy() {
//        unbindService(mConnection);
//        unregisterReceiver(br);
        super.onDestroy();
    }


//    public static class MyAdapter extends FragmentPagerAdapter {
//        public MyAdapter(FragmentManager fm) {
//            super(fm);
//        }
//        private View mCurrentView;
//
//        @Override
//        public void setPrimaryItem(ViewGroup container, int position, Object object) {
//            mCurrentView = (View)object;
//        }
//
//        public View getPrimaryItem() {
//            return mCurrentView;
//        }
//
//        @Override
//        public int getCount() {
//            return NUM_ITEMS;
//        }
//
//        //得到每个item
//        @Override
//        public Fragment getItem(int position) {
//            System.out.println("getItem:"+String.valueOf(position));
//            return ArrayFragment.newInstance(position);
//        }
//
//
//        // 初始化每个页卡选项
//        @Override
//        public Object instantiateItem(ViewGroup arg0, int arg1) {
//            // TODO Auto-generated method stub
//            System.out.println("instantiate:"+String.valueOf(arg1));
//            return super.instantiateItem(arg0, arg1);
//        }
//
//
//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            System.out.println( "position Destory" + position);
//            super.destroyItem(container, position, object);
//        }
//
//    }


    /**
     * 无状态的 会全部加载着， 这个适合少量的 特别多的图片啊啥的 还是用 FragmentStatePagerAdapter
     * @author lilei
     */
  public static class MyFragAdapter extends FragmentPagerAdapter {
   public MyFragAdapter(FragmentManager fm ) {
      super(fm);
    }

    @Override
    public int getCount() {
      return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
     // 返回相应的 fragment
        if (position==1) return Surface.newInstance();
        else return LyricDisplay.newInstance();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
     System.out.println( "position Destory" + position);
     super.destroyItem(container, position, object);
    }
  }


    /**
     * 所有的 每个Fragment
     */
    public static Bitmap getImage(String path){

        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            System.out.println("tdw1");
            if(conn.getResponseCode() == 200){
                InputStream inputStream = conn.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


