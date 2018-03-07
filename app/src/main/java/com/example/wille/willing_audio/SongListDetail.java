package com.example.wille.willing_audio;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wille.willing_audio.InterfaceService.InterfaceService;
import com.example.wille.willing_audio.MessageModel.Message_SongList;
import com.example.wille.willing_audio.MessageModel.Message_The_File;
import com.example.wille.willing_audio.Model.Song;
import com.example.wille.willing_audio.Model.SongList;
import com.example.wille.willing_audio.ZZH.MainActivity;
import com.example.wille.willing_audio.factory.ServiceFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.GetLists;
import static com.example.wille.willing_audio.MusicService.currentSong;
import static com.example.wille.willing_audio.MusicService.encodeUrl;
import static com.example.wille.willing_audio.MusicService.mp;

public class SongListDetail extends AppCompatActivity {

    private ImageView album;
    private TextView singer, songName;
    private Button play, next;
    private BroadcastReceiver br;
    private ImageView song_list_img;
    private TextView  song_list_name;
    private ImageView user_img;
    private TextView user_name;
    private ImageButton collect;
    private TextView song_number;
    private RecyclerView song_list_recyclerview;
    private LinearLayout bottomPlayer;

    private ImageView back;

    private Message_The_File uplaod_file_name=new Message_The_File();

    private String image_path;

    private static int RESULT_LOAD_IMAGE = 1;

    private Parcel data=Parcel.obtain();
    private Parcel reply=Parcel.obtain();
    private ServiceConnection mConnection;
    private IBinder mBinder;
    private Handler mHandler;

    private CommonAdapter<Song> commonAdapter;

    public static List<Song> for_adapter_list;

    public static Song playing_song=new Song();

    private Retrofit retrofit;
    private Bitmap b;
    private InterfaceService interfaceService;




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode+"");
        if(requestCode==1)
        {
            if(data!=null){
                //获得图片的uri
                Uri uri = data.getData();
                //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
                ContentResolver cr = this.getContentResolver();
                System.out.println(uri);
                Bitmap bitmap;
                try
                {
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    song_list_img.setImageBitmap(bitmap);
                    System.out.println("GOOD");
                    //第二种方式去读取路径
                    Cursor cursor =this.getContentResolver().query(uri, null, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    image_path=path;
                    Log.d("willingee",image_path);

                    File upload_file=new File(image_path);//根据路径获取文件
                   // File upload_file=compressImage(bitmap);
                    RequestBody requestfile=RequestBody.create(MediaType.parse("multipart/form-data"),upload_file);
                    MultipartBody.Part body1=MultipartBody.Part.createFormData("file",upload_file.getName(),requestfile);
                    Observable<Message_The_File> observable=interfaceService.upload_file("modify_list_avator",String.valueOf(message_songList.data.listId),body1);
                    observable.subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Message_The_File>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getApplicationContext(),"请确认用户存在",Toast.LENGTH_SHORT).show();
                                    Log.e("willingee","what error");
                                    Log.e("willingee",e.getMessage());
                                }
                                @Override
                                public void onNext(Message_The_File mes) {
                                    Log.d("willingee","have recieve");
                                    Log.d("willingee",mes.data.filename);
                                    uplaod_file_name=mes;
                                }
                                @Override
                                public void onStart(){
                                    Log.d("willingee","start");
                                }
                            });



                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("BAD");
                }
            }
        }

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

    String base_Uris=new String("http://192.168.199.234:8088/uploads/SongAvator/");
    private String prefixs(String name) {
        return base_Uris+name;
    }

    private class Receiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (intent.getAction().equals("pre")||intent.getAction().equals("next")) {
                play.setBackgroundResource(R.drawable.pause);
                songName.setText(getSongList().get().songName);
                singer.setText(getSongList().get().singer);
                if (!currentSong.songAvator.startsWith(Environment.getExternalStorageDirectory().toString())) {
                    new Thread() {
                        @Override
                        public void run() {
                            if (currentSong.songAvator.startsWith(Environment.getExternalStorageDirectory().toString())) {
                                b = getUrlImage(currentSong.songAvator);
                                System.out.println("localsongAvator="+ currentSong.songAvator);
                            }
                            else {
                                b=getUrlImage(encodeUrl(prefixs(currentSong.songAvator)));
                                System.out.println("NetsongAvator="+ encodeUrl(prefixs(currentSong.songAvator)));
                            }
                            Message msg = mHandler.obtainMessage();
                            msg.what = 111;
                            msg.obj = b;
                            mHandler.sendMessage(msg);
                        }
                    }.start();
                }
                else {
                    album.setImageURI(Uri.fromFile(new File(currentSong.songAvator)));
                }
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

    public static Message_SongList message_songList;
    public static SongList getSongList() {
        return message_songList.data;
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MOUNT_UNMOUNT_FILESYSTEMS"};



    public void verifyStoragePermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
            else {  }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int code, @NonNull String[] request, @NonNull int[] grant) {
        if (grant.length>0
                && grant[0]==PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"获取权限成功",Toast.LENGTH_SHORT).show();
        }
        else {
            System.exit(0);
        }
    }



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected())
            {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED)
                {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }



    private static final int REQUEST_INTERNET= 1;
    //    private static String[] PERMISSIONS_STORAGE = {
//            "android.permission.READ_EXTERNAL_STORAGE",
//            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private static String[] PERMISSIONS_INTERNET= {
            "android.permission.INTERNET","android.permission.ACCESS_NETWORK_STATE"};

    public void verifyInternetPermissions(Activity activity) {

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(activity,
                    "android.permission.ACCESS_NETWORK_STATE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(activity, PERMISSIONS_INTERNET,REQUEST_INTERNET);
            }
            else {  }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_list_detail);
        verifyInternetPermissions(this);
        verifyStoragePermissions(this);
        mHandler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 111:
                        album.setImageBitmap(b);
                        break;
                }
            }
        };
        message_songList=new Message_SongList();
        for_adapter_list=new ArrayList<Song>();

        Log.d("willingee","okkk");
        song_list_img=(ImageView) findViewById(R.id.song_list_img);
        song_list_name=(TextView) findViewById(R.id.list_name);
        user_img=(ImageView) findViewById(R.id.user_image);
        user_name=(TextView) findViewById(R.id.user_name);
        collect=(ImageButton) findViewById(R.id.collect);
        song_number=(TextView) findViewById(R.id.song_number);
        song_list_recyclerview=(RecyclerView) findViewById(R.id.song_list_recyclerview);
        bottomPlayer=(LinearLayout) findViewById(R.id.bottomPlayer);
        back=(ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetLists(getApplicationContext());
                Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                intent.putExtra("data","refresh");
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                getApplicationContext().sendBroadcast(intent);
               finish();
            }
        });
        bottomPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), Player.class);
                startActivity(intent);
            }
        });

        album=(ImageView) findViewById(R.id.album);
        singer=(TextView) findViewById(R.id.singer);
        songName=(TextView) findViewById(R.id.songName);
        play=(Button ) findViewById(R.id.play);
        next=(Button) findViewById(R.id.next);
        br= new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("statePlay");
        filter.addAction("statePause");
        filter.addAction("pre");
        filter.addAction("next");
        registerReceiver(br,filter);


        song_list_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        //song_list_recyclerview.setNestedScrollingEnabled(false);
       // song_list_recyclerview.getLayoutParams().height=240*for_adapter_list.size();

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


        Log.d("willingee","hehehehehehhe");
        commonAdapter= new CommonAdapter<Song>(SongListDetail.this, R.layout.song_item,for_adapter_list) {
            @Override
            public void convert(ViewHolder holder, Song song) {
                TextView song_order=holder.getView(R.id.song_order);
                TextView song_name=holder.getView(R.id.song_name);
                TextView singer=holder.getView(R.id.singer);
                song_order.setText(String.valueOf(for_adapter_list.indexOf(song)+1));
                song_name.setText(song.songName);
                singer.setText(song.singer);
            }

        };
        Log.d("willingee","okkk");

        song_list_recyclerview.setAdapter(commonAdapter);


        String base_Uri=new String("http://192.168.199.234:8088/");
        retrofit=ServiceFactory.createRetrofit(base_Uri);

//        Intent intent=new Intent("Action");
//        sendBroadcast();

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

        Intent intent1=getIntent();
        final String listID=intent1.getStringExtra("listID");
        Log.d("LLLI",listID);

        final JSONObject requestData=new JSONObject();
        try {
            requestData.put("listId",listID);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requestData.toString());

        interfaceService =retrofit.create(InterfaceService.class);

        Observable<Message_SongList> observable=interfaceService.get_songlist_byId("get_list_by_id",requestBody);
        observable.subscribeOn(Schedulers.newThread())
//                            .map(new Func1<ResponseBody, Bitmap>() {
//                                @Override
//                                public Bitmap call(ResponseBody arg0){
//                                    boolean isdown=ServiceFactory.DownloadImage(arg0);
//                                    if(isdown) Log.d("willing","okkkk");
//                                    Bitmap bitmap= BitmapFactory.decodeStream(arg0.byteStream());
//                                    return bitmap;
//                                }
//                            })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_SongList>() {
                    @Override
                    public void onCompleted() {
                        get_ListImg(message_songList.data.listAvator);
                        get_UserImg(com.example.wille.willing_audio.Adapter_And_Service.Message.message_user.data.avator);
                        user_name.setText(com.example.wille.willing_audio.Adapter_And_Service.Message.message_user.data.nickname);

                        Intent intent = new Intent(getApplicationContext(),MusicService.class);
                        startService(intent); // 开启服务
// 绑定activity和服务
                        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(),"请确认用户存在",Toast.LENGTH_SHORT).show();
                        Log.e("willingee",e.getMessage());
                    }
                    @Override
                    public void onNext(Message_SongList tmp) {
                        message_songList=tmp;
                        for_adapter_list.clear();
                        for(int i=0;i<tmp.data.songs.songs.size();i++){
                            Log.d("willingee",tmp.data.songs.songs.get(i).singer);
                            for_adapter_list.add(tmp.data.songs.songs.get(i));
                        }
                        commonAdapter.notifyDataSetChanged();
                        song_list_name.setText(tmp.data.listName);
                        song_number.setText("(共"+tmp.data.songs.songs.size()+"首)");

                    }
                    @Override
                    public void onStart(){
                        Log.d("willingee","start....");
                    }

                });
        commonAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                data.setDataPosition(0);
                data.writeInt(position);
                data.writeInt(-1);
                try {
                    mBinder.transact(109, data, reply, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(getApplicationContext(), Player.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });


        song_list_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent for_album=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI); //打开本地相册
                startActivityForResult(for_album,RESULT_LOAD_IMAGE);
            }
        });

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final JSONObject requestComment=new JSONObject();
                try {
                    requestComment.put("account", com.example.wille.willing_audio.Adapter_And_Service.Message.message_user.data.account);
                    requestComment.put("listId",listID);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requestComment.toString());

                Observable<com.example.wille.willing_audio.MessageModel.Message> comment_observable=interfaceService.add_comment("collect_list",requestBody);

                comment_observable.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<com.example.wille.willing_audio.MessageModel.Message>() {
                            @Override
                            public void onCompleted() {
                                Toast.makeText(getApplicationContext(),"已收藏",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(getApplicationContext(),"收藏失败",Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNext(com.example.wille.willing_audio.MessageModel.Message message) {
                            }
                        });
            }
        });

    }

    void get_ListImg(String load_url){
        Observable<ResponseBody> observable_listImg=interfaceService.get_file("uploads/ListAvator/"+load_url);
        observable_listImg.subscribeOn(Schedulers.newThread())
                            .map(new Func1<ResponseBody, Bitmap>() { //将获取文件转为bitmap
                                @Override
                                public Bitmap call(ResponseBody arg0){
                                    Bitmap bitmap= BitmapFactory.decodeStream(arg0.byteStream());
                                    return bitmap;
                                }
                            })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(),"请确认用户存在",Toast.LENGTH_SHORT).show();
                        Log.e("willingww",e.getMessage());
                    }
                    @Override
                    public void onNext(Bitmap tmp) {
                        Log.d("willingww","have recieve");
                        song_list_img.setImageBitmap(tmp);
                    }
                    @Override
                    public void onStart(){
                        Log.d("willingww","start ...");
                    }
                });
    }

    void get_UserImg(String load_url){
        Observable<ResponseBody> observable_userImg=interfaceService.get_file("uploads/UserAvator/"+load_url);
        observable_userImg.subscribeOn(Schedulers.newThread())
                .map(new Func1<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap call(ResponseBody arg0){
//                                    boolean isdown=ServiceFactory.DownloadImage(arg0);
//                                    if(isdown) Log.d("willing","okkkk");
                        Bitmap bitmap= BitmapFactory.decodeStream(arg0.byteStream());
                        return bitmap;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(),"请确认用户存在",Toast.LENGTH_SHORT).show();
                        Log.e("willingww",e.getMessage());
                    }
                    @Override
                    public void onNext(Bitmap tmp) {
                        Log.d("willingww","have recieve");
                        user_img.setImageBitmap(tmp);
                    }
                    @Override
                    public void onStart(){
                        Log.d("willingww","start ...");
                    }
                });
    }

}
