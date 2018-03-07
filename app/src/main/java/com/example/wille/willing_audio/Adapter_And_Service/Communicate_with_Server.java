package com.example.wille.willing_audio.Adapter_And_Service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import com.example.wille.willing_audio.Factory2.ServiceFactory2;
import com.example.wille.willing_audio.Interfaces2.Interfaces2;
import com.example.wille.willing_audio.MessageModel.Message_SongLists;
import com.example.wille.willing_audio.MessageModel.Message_Songs;
import com.example.wille.willing_audio.MessageModel.Message_The_File;
import com.example.wille.willing_audio.MessageModel.Message_User;
import com.example.wille.willing_audio.MessageModel.Message_UserSongLists;
import com.example.wille.willing_audio.MessageModel.Message_Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.example.wille.willing_audio.Adapter_And_Service.Message.base_Uri;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.message_user;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.*;


/**
 * Created by Administrator on 2018/1/5.
 */

public class Communicate_with_Server {
    static public void AddSongList(String account,String listname,final Context context){
        ServiceFactory2 serviceFactory=new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces= serviceFactory.getRetrofit().create(Interfaces2.class);
        final JSONObject requetData=new JSONObject();
        try {
            requetData.put("account",account);
            requetData.put("listname",listname);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requetData.toString());
        interfaces.PostUser("create_new_songlist",requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_User>() {
                    @Override
                    public void onCompleted() {
                        GetLists(context);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context,"login用户不存在",Toast.LENGTH_SHORT).show();
                        Log.d("LOL",e.getMessage());
                    }
                    @Override
                    public void onNext(Message_User tem) {
                        Log.e("ADD",tem.state);
                        if(message_user!=null) Log.e("ADD","messnotnull2");
                        else Log.e("ADD","messnull2");
                    }
                });
    }
    static public void GetNetPic(String pic_path,final ImageView imgView)
    {
        Log.e("PIC","GETPIC");
        ServiceFactory2 serviceFactory2 =new ServiceFactory2("http://192.168.199.234:8088/uploads/");
        final Interfaces2 service= serviceFactory2.getRetrofit().create(Interfaces2.class);
        service.downloadPicFromNet(pic_path)
                .subscribeOn(Schedulers.newThread())//在新线程中实现该方法
                .map(new Func1<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap call(ResponseBody arg0) {
                        Bitmap bitmap = BitmapFactory.decodeStream(arg0.byteStream());
                        return bitmap;//返回一个bitmap对象
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//在Android主线程中展示
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Log.e("PIC","11");
                    }
                    @Override
                    public void onCompleted() {
                        Log.e("PIC","22");
                    }
                    @Override
                    public void onError(Throwable arg0) {
                        Log.e("PIC",arg0.getMessage());
                    }

                    @Override
                    public void onNext(Bitmap arg0) {
                        Log.e("ZLGG","hhhh");
                        imgView.setImageBitmap(arg0);
                    }
                });
    }
    static public void GetNetPic(String pic_path,final CircleImageView imgView)
    {
        Log.e("ZLGG","GETPIC");
        ServiceFactory2 serviceFactory2 =new ServiceFactory2("http://192.168.199.234:8088/uploads/");
        final Interfaces2 service= serviceFactory2.getRetrofit().create(Interfaces2.class);
        service.downloadPicFromNet(pic_path)
                .subscribeOn(Schedulers.newThread())//在新线程中实现该方法
                .map(new Func1<ResponseBody, Bitmap>() {
                    @Override
                    public Bitmap call(ResponseBody arg0) {
                        Bitmap bitmap = BitmapFactory.decodeStream(arg0.byteStream());
                        return bitmap;//返回一个bitmap对象
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//在Android主线程中展示
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        Log.e("PIC","11");
                    }
                    @Override
                    public void onCompleted() {
                        Log.e("PIC","22");
                    }
                    @Override
                    public void onError(Throwable arg0) {
                        Log.e("PIC",arg0.getMessage());
                    }

                    @Override
                    public void onNext(Bitmap arg0) {
                        Log.e("ZLGG","here");
                        imgView.setImageBitmap(arg0);
                    }
                });
    }


    static public void GetLists(final Context context){
        String user_account;
        if(message_user!=null&&message_user.data.account!=null) user_account= message_user.data.account;
        else user_account="无";

        ServiceFactory2 serviceFactory2 =new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces2 = serviceFactory2.getRetrofit().create(Interfaces2.class);
        final JSONObject requetData=new JSONObject();
        try {
            requetData.put("account",user_account);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requetData.toString());
        Log.e("ZLG","getlist");
        Log.e("ZLG","getlistaccount"+user_account);
        interfaces2.PostMessage("get_list_by_account",requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_UserSongLists>() {
                    @Override
                    public void onCompleted() {
                        //Log.e("LOL",String.valueOf(Lists_mycreate.songLists.size()));
                        Log.e("LOL","完成传输");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context,"用户不存在",Toast.LENGTH_SHORT).show();
                        Log.d("LOL",e.getMessage());
                    }
                    @Override
                    public void onNext(Message_UserSongLists tem) {
                        Log.e("ZLG","INNNNNN");
                        message_userSongLists=tem;
                        Lists_mycreate=message_userSongLists.data.mycreate;
                        Lists_mycollect=message_userSongLists.data.mycollect;
                        Log.e("ZLG",tem.data.mycreate.songLists.get(0).listName);

                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                        intent.putExtra("data","refresh");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        context.sendBroadcast(intent);
                    }
                });
    }
    static public void DeleteSongList(Long listID,final Context context){
        ServiceFactory2 serviceFactory2 =new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces2 = serviceFactory2.getRetrofit().create(Interfaces2.class);
        final JSONObject requetData=new JSONObject();
        try {
            requetData.put("listId",listID);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requetData.toString());
        interfaces2.PostUser("delete_songlist",requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_User>() {
                    @Override
                    public void onCompleted() {
                        GetLists(context);
                        Intent intent = new Intent("android.intent.action.CART_BROADCAST");
                        intent.putExtra("data","refresh");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        context.sendBroadcast(intent);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOL",e.getMessage());
                    }
                    @Override
                    public void onNext(Message_User tem) {
                        Log.e("DEL",tem.state);
                    }
                });

    }
    static public void UploadFile(String image_path,final Context context){
        File upload_file=new File(image_path);
        RequestBody requestfile=RequestBody.create(MediaType.parse("multipart/form-data"),upload_file);
        MultipartBody.Part body1=MultipartBody.Part.createFormData("file",upload_file.getName(),requestfile);

        ServiceFactory2 serviceFactory2 =new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces2 = serviceFactory2.getRetrofit().create(Interfaces2.class);
        Observable<Message_The_File> observable= interfaces2.upload_file("modify_user_avator",String.valueOf(message_user.data.account),body1);
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_The_File>() {
                    @Override
                    public void onCompleted() {
                        Log.d("willing","almost finish");
                        Toast.makeText(context,"传输完成",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context,"请确认用户存在",Toast.LENGTH_SHORT).show();
                        Log.e("willingee","what error");
                        Log.e("willingee",e.getMessage());
                    }
                    @Override
                    public void onNext(Message_The_File mes) {
                        Log.d("willingee","have recieve");
                        Log.d("willingee",mes.data.filename);
                        message_user.data.avator=mes.data.filename;
                    }
                    @Override
                    public void onStart(){
                        Log.d("willingee","start");
                    }
                });
    }

    static public void GetAllUsers(final Context context){
        ServiceFactory2 serviceFactory2 =new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces2 = serviceFactory2.getRetrofit().create(Interfaces2.class);
        final JSONObject requetData=new JSONObject();
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requetData.toString());
        interfaces2.PostUsers("get_all_users",requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_Users>() {
                    @Override
                    public void onCompleted() {
                        Log.e("LOL","获得用户信息");
                        Intent intent = new Intent("android.intent.action.GETALL_BROADCAST");
                        intent.putExtra("data","refresh_getall");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        context.sendBroadcast(intent);
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context,"login用户不存在",Toast.LENGTH_SHORT).show();
                        Log.d("LOL",e.getMessage());
                    }
                    @Override
                    public void onNext(Message_Users tem) {
                        Log.e("GETALL",tem.state);
                        Log.e("GETALL","size="+String.valueOf(tem.data.userList.size()));
                        for(int i=0;i<tem.data.userList.size();i++){
                            Log.e("GETALL",tem.data.userList.get(i).nickname);
                        }
                        Message.message_users=tem;
                    }
                });
    }
    static public void GetRandomList(final Context context){

        ServiceFactory2 serviceFactory2 =new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces2 = serviceFactory2.getRetrofit().create(Interfaces2.class);
        final JSONObject requetData=new JSONObject();
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requetData.toString());

        interfaces2.PostMessage_songlists("get_random_list",requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_SongLists>() {
                    @Override
                    public void onCompleted() {
                        Intent intent = new Intent("android.intent.action.Random_BROADCAST");
                        intent.putExtra("data","refresh_random");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        context.sendBroadcast(intent);

                        Log.e("LOL","完成传输");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context,"用户不存在",Toast.LENGTH_SHORT).show();
                        Log.d("LOL",e.getMessage());
                    }
                    @Override
                    public void onNext(Message_SongLists tem) {
                        Message.RandomLists=tem.data;
                        Log.e("Ran",tem.data.songLists.get(0).listName);
                    }
                });

    }
    static public void GetRandomSongs(final Context context){
        ServiceFactory2 serviceFactory2 =new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces2 = serviceFactory2.getRetrofit().create(Interfaces2.class);
        final JSONObject requetData=new JSONObject();
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requetData.toString());

        interfaces2.PostMessage_songs("get_random_song",requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_Songs>() {
                    @Override
                    public void onCompleted() {
                        Intent intent = new Intent("android.intent.action.Random_BROADCAST");
                        intent.putExtra("data","refresh_random");
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        context.sendBroadcast(intent);

                        Log.e("LOL","完成传输");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context,"用户不存在",Toast.LENGTH_SHORT).show();
                        Log.d("LOL",e.getMessage());
                    }
                    @Override
                    public void onNext(Message_Songs tem) {
                        Message.RandomSongs=tem.data;
                        Log.e("RanS",tem.data.songs.get(0).songName);
                    }
                });

    }

    static public void AddSongToList(String songId,String listId){
        ServiceFactory2 serviceFactory2 =new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces2 = serviceFactory2.getRetrofit().create(Interfaces2.class);
        final JSONObject requetData=new JSONObject();
        try {
            requetData.put("listId",listId);
            requetData.put("songId",songId);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requetData.toString());
        interfaces2.PostUsers("add_song_to_list",requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_Users>() {
                    @Override
                    public void onCompleted() {

                    }
                    @Override
                    public void onError(Throwable e) {
                        Log.d("LOL",e.getMessage());
                    }
                    @Override
                    public void onNext(Message_Users tem) {
                    }
                });
    }


}
