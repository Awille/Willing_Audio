package com.example.wille.willing_audio;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wille.willing_audio.InterfaceService.InterfaceService;
import com.example.wille.willing_audio.MessageModel.Message;
import com.example.wille.willing_audio.MessageModel.Message_Comments;
import com.example.wille.willing_audio.MessageModel.Message_Song;
import com.example.wille.willing_audio.MessageModel.Message_User;
import com.example.wille.willing_audio.Model.Comment;
import com.example.wille.willing_audio.factory.ServiceFactory;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by wille on 2018/1/6.
 */

public class CommentDetail extends AppCompatActivity {

    private ImageView back;
    private TextView comment_num;
    private ImageView song_image;
    private TextView song_name;
    private TextView singer;
    private RecyclerView recyclerView;
    private EditText edit_comment;
    private ImageButton add_comment;
    private CommonAdapter<Comment> commentCommonAdapter;
    private List<Comment> comment_list;

    private Message_User message_user;


    private Retrofit retrofit;
    private InterfaceService interfaceService;

    private int total_height=0;

    private static Message_Comments message_comments=new Message_Comments();

    private Message_Song message_song=new Message_Song();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
        Bundle bundle=new Bundle();
        bundle=this.getIntent().getExtras();
        final String sId=bundle.getString("songId");
        back=(ImageView) findViewById(R.id.back);
        comment_num=(TextView) findViewById(R.id.comment_num);
        song_image=(ImageView) findViewById(R.id.song_image);
        song_name=(TextView) findViewById(R.id.song_name);
        singer=(TextView) findViewById(R.id.singer);
        recyclerView=(RecyclerView) findViewById(R.id.comment_recyclerview);
        edit_comment=(EditText) findViewById(R.id.edit_comment);
        add_comment=(ImageButton) findViewById(R.id.add_comment);



        String base_Uri=new String("http://192.168.199.234:8088/");
        retrofit= ServiceFactory.createRetrofit(base_Uri);
        interfaceService =retrofit.create(InterfaceService.class);
        comment_list=new ArrayList<Comment>();

        get_SongImg(MusicService.currentSong.songAvator);
        song_name.setText(MusicService.currentSong.songName);
        singer.setText(MusicService.currentSong.singer);

        final JSONObject requestData=new JSONObject();
        try {
            requestData.put("songId",sId);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requestData.toString());

        final Observable<Message_Comments> observable_comment=interfaceService.get_comments_bySongId("get_song_comments",requestBody);

        observable_comment.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_Comments>() {
                    @Override
                    public void onCompleted() {
                        Log.d("willingee","complete");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("willingee",e.getMessage());
                    }

                    @Override
                    public void onNext(Message_Comments mes) {
                        message_comments=mes;
                        Log.d("willingee",mes.state);
                        Log.d("willingee",String.valueOf(mes.data.comments.size()));
                        //comment_list=message_comments.data.comments;
                        for(int i=0;i<message_comments.data.comments.size();i++){
                            comment_list.add(message_comments.data.comments.get(i));
                        }
                        comment_num.setText("评论("+String.valueOf(message_comments.data.comments.size())+")");

                        // song_list_recyclerview.getLayoutParams().height=240*for_adapter_list.size();
                        commentCommonAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onStart(){

                    }
                });


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        commentCommonAdapter=new CommonAdapter<Comment>(CommentDetail.this,R.layout.comment_item,comment_list) {
            @Override
            public void convert(ViewHolder holder, Comment comment) {
                final ImageView user_image=holder.getView(R.id.user_image);
                final TextView user_name=holder.getView(R.id.user_name);
                TextView comment_date=holder.getView(R.id.comment_date);
                TextView comment_text=holder.getView(R.id.comment_text);

                comment_date.setText(comment.time);
                comment_text.setText(comment.content);
                Log.d("willingee","convert");

                final JSONObject requestData1=new JSONObject();
                try {
                    requestData1.put("account",comment.account);
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                RequestBody requestBody1=RequestBody.create(MediaType.parse("application/json"),requestData1.toString());
                final Observable<Message_User> message_userObservable=interfaceService.get_user_ByAccount("get_user_ByAccount",requestBody1);

                message_userObservable.subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Message_User>() {
                            @Override
                            public void onCompleted() {
                                Observable<ResponseBody> observable_userImg=interfaceService.get_file("uploads/UserAvator/"+message_user.data.avator);
                                Log.d("trings",message_user.data.avator);
                                Log.d("trings","wrong here");
                                observable_userImg.subscribeOn(Schedulers.newThread())
                                        .map(new Func1<ResponseBody, Bitmap>() {
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
                                                Toast.makeText(getApplicationContext(),"加载用户头像失败",Toast.LENGTH_SHORT).show();
                                                Log.e("willingww",e.getMessage());
                                            }
                                            @Override
                                            public void onNext(Bitmap tmp) {
                                                Log.d("willingww","have recieve");
                                                user_image.setImageBitmap(tmp);
                                            }
                                            @Override
                                            public void onStart(){
                                                Log.d("willingww","start ...");
                                            }
                                        });

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.d("trings","wrongss");

                            }

                            @Override
                            public void onNext(Message_User mse) {
                                message_user=mse;
                                user_name.setText(message_user.data.nickname);
                            }
                        });

//                total_height+=viewHolder.itemView.getHeight();
//                if(comment_list.indexOf(comment)==comment_list.size()-1){
//                    recyclerView.getLayoutParams().height=total_height;
//                    total_height=0;
//                    recyclerView.setNestedScrollingEnabled(false);
//                }

            }
        };

        recyclerView.setAdapter(commentCommonAdapter);






        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=edit_comment.getText().toString();
                if(text.equals("")){
                    Toast.makeText(getApplication(),"请先输入评论",Toast.LENGTH_SHORT).show();
                }
                else{
                    final JSONObject requestComment=new JSONObject();
                    try {
                        requestComment.put("account", com.example.wille.willing_audio.Adapter_And_Service.Message.message_user.data.account);
                        requestComment.put("content",text);
                        requestComment.put("songId",MusicService.currentSong.songId);
                    }
                    catch (JSONException e){
                        e.printStackTrace();
                    }
                    RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requestComment.toString());

                    Observable<Message> comment_observable=interfaceService.add_comment("add_comment",requestBody);

                    comment_observable.subscribeOn(Schedulers.newThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Subscriber<Message>() {
                                @Override
                                public void onCompleted() {
                                    Toast.makeText(getApplicationContext(),"评论成功",Toast.LENGTH_SHORT).show();

                                    Intent intent=new Intent(CommentDetail.this,CommentDetail.class);

                                    Bundle bundle=new Bundle();
                                    bundle.putString("songId",sId);
                                    intent.putExtras(bundle);
                                    finish();
                                    startActivity(intent);



                                }

                                @Override
                                public void onError(Throwable e) {
                                    Toast.makeText(getApplicationContext(),"评论失败",Toast.LENGTH_SHORT).show();

                                }

                                @Override
                                public void onNext(Message message) {

                                }
                            });



                }
            }
        });







    }



    void get_SongImg(String load_url){
        Observable<ResponseBody> observable_songImg=interfaceService.get_file("uploads/SongAvator/"+load_url);
        observable_songImg.subscribeOn(Schedulers.newThread())
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
                        song_image.setImageBitmap(tmp);
                    }
                    @Override
                    public void onStart(){
                        Log.d("willingww","start ...");
                    }
                });
    }
}
