package com.example.wille.willing_audio.Interfaces2;




import com.example.wille.willing_audio.MessageModel.Message_SongLists;
import com.example.wille.willing_audio.MessageModel.Message_Songs;
import com.example.wille.willing_audio.MessageModel.Message_The_File;
import com.example.wille.willing_audio.MessageModel.Message_User;
import com.example.wille.willing_audio.MessageModel.Message_UserSongLists;
import com.example.wille.willing_audio.MessageModel.Message_Users;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by Administrator on 2017/12/31.
 */

public interface Interfaces2 {
    //获取歌单接口
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Interface.jsp")
    Observable<Message_UserSongLists> PostMessage(@Query("intent") String intent, @Body RequestBody requestBody);

    //获取随机歌单接口
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Interface.jsp")
    Observable<Message_SongLists> PostMessage_songlists(@Query("intent") String intent, @Body RequestBody requestBody);

    //获取随机歌曲接口
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Interface.jsp")
    Observable<Message_Songs> PostMessage_songs(@Query("intent") String intent, @Body RequestBody requestBody);


    //获取用户信息、登录、注册、新建歌单、删除歌单都是用的它
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Interface.jsp")
    Observable<Message_User> PostUser(@Query("intent") String intent, @Body RequestBody requestBody);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("Interface.jsp")
    Observable<Message_Users> PostUsers(@Query("intent") String intent, @Body RequestBody requestBody);


    //申请获取并显示图片
    @POST
    Observable<ResponseBody> downloadPicFromNet(@Url String fileUrl);

    //上传图片
    @Multipart
    @POST("UploadUserAvator.jsp")
    Observable<Message_The_File> upload_file(
            @Query("intent") String intent,
            @Query("pid") String pid,
            @Part MultipartBody.Part file);


}
