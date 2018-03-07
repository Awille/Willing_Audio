package com.example.wille.willing_audio.ZZH;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server;
import com.example.wille.willing_audio.Adapter_And_Service.MyFragmentPagerAdapter;
import com.example.wille.willing_audio.Adapter_And_Service.NetWork;
import com.example.wille.willing_audio.ClickFilter;
import com.example.wille.willing_audio.Factory2.ServiceFactory2;
import com.example.wille.willing_audio.Interfaces2.Interfaces2;
import com.example.wille.willing_audio.MessageModel.Message_User;
import com.example.wille.willing_audio.Player;
import com.example.wille.willing_audio.R;
import com.example.wille.willing_audio.SongListDetail;
import com.example.wille.willing_audio.ZLG.alter;
import com.example.wille.willing_audio.ZLG.homepage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.GetLists;
import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.GetRandomList;
import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.GetRandomSongs;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.base_Uri;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.message_user;
import static com.example.wille.willing_audio.MusicService.currentSong;
import static com.example.wille.willing_audio.MusicService.encodeUrl;
import static com.example.wille.willing_audio.MusicService.mp;
import static com.example.wille.willing_audio.SongListDetail.getSongList;


public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    TextView nickname_text;
    NavigationView navigationView;
    Toolbar toolbar;
    DrawerLayout drawlayout;
    CircleImageView personphoto;
    View headview;
    private LinearLayout bottomPlayer;
    private Parcel data=Parcel.obtain();
    private Parcel reply=Parcel.obtain();
    private ServiceConnection mConnection;
    private IBinder mBinder;
    private BroadcastReceiver br;

    private TextView songName, singer;
    private Button play, next;

    static int flag=1;

    private int[] tabIcons_white = {
            R.mipmap.music_left,
            R.mipmap.music_mid,
            R.mipmap.people_right
    };
    private int[] tabIcons_grey = {
            R.mipmap.music_left_grey,
            R.mipmap.music_mid_grey,
            R.mipmap.people_right_grey
    };

    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals("pre")||intent.getAction().equals("next")) {
                play.setBackgroundResource(R.drawable.pause);
                songName.setText(getSongList().get().songName);
                singer.setText(getSongList().get().singer);
            }
            else if (intent.getAction().equals("statePlay")) {
                play.setBackgroundResource(R.drawable.pause);
            }
            else if (intent.getAction().equals("statePause")) {
                System.out.println("statePause received");
                play.setBackgroundResource(R.drawable.play);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyStoragePermissions(this);

    }

    private void main(){
        final SharedPreferences setting = getSharedPreferences("share",0);
        String user_account = setting.getString("user_account","default");
        String user_password = setting.getString("user_password","default");
        Log.e("ZLG","shareaccount"+user_account);
        Log.e("ZLG","sharepassword"+user_password);
        if(!NetWork.isNetworkAvailable(this)){
            InitDrawLayout();
            InitTabLayout();
        }
        else{
            if(user_account.equals("default")){//没有登录过，要跳转登录界面user_account.equals("default")
                Log.e("ZLG","jump_to_homepage");
                Intent intent1=new Intent(MainActivity.this,homepage.class);
                startActivity(intent1);
            }
            else{
                GetUser(user_account,user_password);
                Log.e("ZLG","back"+user_account);
            }
        }


        //InitBottomPlayer();

    }
    private void InitBottomPlayer(){
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
        bottomPlayer=(LinearLayout) findViewById(R.id.bottomPlayer);

        bottomPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Player.class);
                startActivity(intent);
            }
        });
        play=(Button ) findViewById(R.id.play);
        next=(Button) findViewById(R.id.next);
        songName=(TextView) findViewById(R.id.songName);
        singer=(TextView) findViewById(R.id.singer);
        br= new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("statePlay");
        filter.addAction("statePause");
        filter.addAction("pre");
        filter.addAction("next");
        registerReceiver(br,filter);
        final Button play=(Button)findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ClickFilter.filter()) return;
                try {
                    if (mp.isPlaying()) {
                        mBinder.transact(107, data, reply, 0);
                        play.setBackgroundResource(R.drawable.play);
                    }
                    else {
                        mBinder.transact(101, data, reply, 0);
                        play.setBackgroundResource(R.drawable.pause);
                    }
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
                    data.writeInt(-1);
                    mBinder.transact(103, data, reply ,0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void InitTabLayout(){

        Toast.makeText(getApplicationContext(),"main",Toast.LENGTH_SHORT).show();
        GetLists(getApplicationContext());//获取我创建的歌单和我收藏的歌单
        Log.e("ZLG","gettinglist");
//        Intent intent = new Intent(this, MusicService.class);
//        startService(intent);
//        bindService(intent, sc, Context.BIND_AUTO_CREATE);//调用bindService保存与Service的通信，Activity启动时绑定Service

        List<Fragment> fragments=new ArrayList<Fragment>();
        Fragment2 fragment2=new Fragment2();
        fragments.add(new Fragment1());
        fragments.add(fragment2);
        fragments.add(new Fragment3());

        ViewPager viewPager=(ViewPager)findViewById(R.id.viewPager);
        MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments,this);
        viewPager.setAdapter(adapter);

        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(tabIcons_white[0]).setText("");
        tabLayout.getTabAt(1).setIcon(tabIcons_grey[1]).setText("");//.setText("1")  tabLayout.setTabTextColors(Color.parseColor("#666666"),Color.parseColor("#ff6b00"));
        tabLayout.getTabAt(2).setIcon(tabIcons_grey[2]).setText("");

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("ZZH",tab.toString());
                for(int i=0;i<3;i++){
                    if(tabLayout.getTabAt(i)==tab){
                        tab.setIcon(tabIcons_white[i]);
                    }
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                for(int i=0;i<3;i++){
                    if(tabLayout.getTabAt(i)==tab){
                        tab.setIcon(tabIcons_grey[i]);
                    }
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

private void InitDrawLayout(){

        Log.e("ZLG","DRAWSTART");
       navigationView=(NavigationView) findViewById(R.id.nav_view);
        toolbar=(Toolbar)findViewById(R.id.mytoolbar);
        drawlayout=(DrawerLayout)findViewById(R.id.drawerlayout);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBarDrawerToggle abt=new ActionBarDrawerToggle(this,drawlayout,toolbar,R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        headview=navigationView.inflateHeaderView(R.layout.head);

        nickname_text=(TextView)headview.findViewById(R.id.nickname_text);
        personphoto=(CircleImageView) headview.findViewById(R.id.personphoto);
        if(message_user!=null&&message_user.data.nickname!=null) nickname_text.setText(message_user.data.nickname);
        if(message_user!=null&&message_user.data.nickname!=null&&NetWork.isNetworkAvailable(getApplicationContext())==true)
        {
            Log.e("ZLGG","UserAvator/"+message_user.data.avator);
             Communicate_with_Server.GetNetPic("UserAvator/"+message_user.data.avator,personphoto);
        }
        //if(change_image==1) personphoto.setImageURI(Uri.fromFile(new File(imgpath)));
        personphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,alter.class);
                startActivityForResult(intent,1);
            }
        });

        Log.e("ZLG","DRAWMID");

        Menu menu1=navigationView.getMenu();
        MenuItem menuItem1=menu1.findItem(R.id.sex);
        MenuItem menuItem2=menu1.findItem(R.id.signname);
        MenuItem menuItem3=menu1.findItem(R.id.singername);
        MenuItem menuItem4=menu1.findItem(R.id.songname);
        if(message_user!=null&&message_user.data.sex==null){
            menuItem1.setTitle("无");
            menuItem2.setTitle("无");
            menuItem3.setTitle("无");
            menuItem4.setTitle("无");
        }
        else if(message_user!=null){
            Log.e("ZLG","insideheree");
            String sex=message_user.data.sex;
            String signname=message_user.data.signature;
            String favoritesinger=message_user.data.favourite_singer;
            String favoritesong=message_user.data.favourite_song;
            menuItem1.setTitle(sex);
            menuItem2.setTitle(signname);
            menuItem3.setTitle(favoritesinger);
            menuItem4.setTitle(favoritesong);
            Log.e("ZLG","insideheree11111111");
        }
    Log.e("ZLG","DRAWNext");
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                String str=item.getTitle().toString();
                if(str.equals("注销")){
                    SharedPreferences setting = getSharedPreferences("share",0);
                    SharedPreferences.Editor editor=setting.edit();
                    editor.putString("user_account","default");
                    editor.putString("user_password","default");
                    editor.commit();
                    Intent intent=new Intent(MainActivity.this,homepage.class);
                    startActivityForResult(intent,1);
                }
                else if(str.equals("退出")){
                    setResult(100, new Intent());
                    finish();
                    //System.exit(0);
                }
                return true;
            }
        });
        Log.e("ZLG","DRAWFINISH");
        flag=0;

}


    public void GetUser(final String user_account,final String user_password){
        ServiceFactory2 serviceFactory2 =new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces2 = serviceFactory2.getRetrofit().create(Interfaces2.class);
        final JSONObject requetData=new JSONObject();
        try {
            requetData.put("account",user_account);
            requetData.put("password",user_password);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        Log.e("ZLG","user_password"+user_password);


        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requetData.toString());
        interfaces2.PostUser("sign_in",requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_User>() {
                    @Override
                    public void onCompleted() {
                        Log.e("LOL","获得用户信息");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(),"login用户不存在",Toast.LENGTH_SHORT).show();
                        Log.d("LOL",e.getMessage());
                    }
                    @Override
                    public void onNext(Message_User tem) {
                        Log.e("ZLG","gettem");
                        Log.e("ZLG",tem.data.password);
                        //Message_User message_user=tem;
                        message_user=tem;
                        String real_passwprd=message_user.data.password;
                        if(real_passwprd.equals(user_password)){
                            Log.e("ZLG","password_right");
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                        }
                        InitDrawLayout();
                        Log.e("ZLG","Draw");
                        InitTabLayout();
                        Log.e("ZLG","Tab");
                    }
                });
    }







    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    public void  verifyStoragePermissions(Activity activity)//新装app时会弹出框询问是否给权限，并且回调onresult函数
    {
        Log.e("LOL","comein");
        try{
            int permission= ActivityCompat.checkSelfPermission(activity,"android.permission.READ_EXTERNAL_STORAGE");
            if(permission!= PackageManager.PERMISSION_GRANTED){
                Log.e("LOL","failgetright");
                ActivityCompat.requestPermissions(activity,PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
            else{
                Log.e("LOL","getright");
                main();
            }
        }catch(Exception e){
            Log.e("LOL","exception");
            e.printStackTrace();
        }
    };
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults){//第一次回调，做出反应，但是要退出app再进去才有了权限
        if(grantResults.length>0&&grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.e("LOL","reallygetright");
            main();
        }
        else{
            Log.e("LOL","reallyfailgetright");
            this.finish();
        }
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            unbindService(sc);
//            sc=null;
//            try{
//                MainActivity.this.finish();
//                System.exit(0);
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.e("ZZH","onResume");//未初始化控件
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==100){
            Log.e("ZLG","MainActivityOnresult");
            Toast.makeText(getApplicationContext(),"exit_Main",Toast.LENGTH_SHORT).show();
            setResult(100,new Intent());
            finish();
        }
        if(resultCode==200){
            if(flag==0) {
            if (message_user != null && message_user.data != null&&message_user.data.nickname!=null
                    &&message_user.data.sex!=null&&message_user.data.signature!=null
                    &&message_user.data.favourite_song!=null&&message_user.data.favourite_singer!=null
                    &&message_user.data.account!=null&&message_user.data.password!=null) {
                Log.e("ZLG","intoresume");
                nickname_text = (TextView) headview.findViewById(R.id.nickname_text);
                nickname_text.setText(message_user.data.nickname);
                personphoto=(CircleImageView)headview.findViewById(R.id.personphoto);
                Communicate_with_Server.GetNetPic("UserAvator/"+message_user.data.avator,personphoto);
                Menu menu1 = navigationView.getMenu();
                MenuItem menuItem1 = menu1.findItem(R.id.sex);
                MenuItem menuItem2 = menu1.findItem(R.id.signname);
                MenuItem menuItem3 = menu1.findItem(R.id.singername);
                MenuItem menuItem4 = menu1.findItem(R.id.songname);

                if (message_user != null && message_user.data.sex == null) {
                    menuItem1.setTitle("无");
                    menuItem2.setTitle("无");
                    menuItem3.setTitle("无");
                    menuItem4.setTitle("无");
                } else if (message_user != null) {
                    Log.e("ZLG", "insideheree");
                    String sex = message_user.data.sex;
                    String signname = message_user.data.signature;
                    String favoritesinger = message_user.data.favourite_singer;
                    String favoritesong = message_user.data.favourite_song;
                    if(message_user.data.sex!=null)
                    menuItem1.setTitle(sex);
                    menuItem2.setTitle(signname);
                    menuItem3.setTitle(favoritesinger);
                    menuItem4.setTitle(favoritesong);
                    Log.e("ZLG", "insideheree11111111");
                }
            }
        }
        }
    }

}




