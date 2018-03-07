package com.example.wille.willing_audio.InterfaceService;

import com.example.wille.willing_audio.Adapter_And_Service.Message;
import com.example.wille.willing_audio.MessageModel.Message_Comments;
import com.example.wille.willing_audio.MessageModel.Message_SongList;
import com.example.wille.willing_audio.MessageModel.Message_SongLists;
import com.example.wille.willing_audio.MessageModel.Message_Songs;
import com.example.wille.willing_audio.MessageModel.Message_The_File;
import com.example.wille.willing_audio.MessageModel.Message_User;
import com.example.wille.willing_audio.MessageModel.Message_UserSongLists;

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
 * Created by wille on 2018/1/1.
 */

public interface InterfaceService {
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("functions/Interface.jsp")
    Observable<Message_SongList> get_songlist_byId(@Query("intent") String intent,@Body RequestBody requestBody);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("functions/Interface.jsp")
    Observable<Message_User> get_user_ByAccount(@Query("intent") String intent, @Body RequestBody requestBody);


    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("functions/Interface.jsp")
    Observable<Message_Comments> get_comments_bySongId(@Query("intent") String intent, @Body RequestBody requestBody);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("functions/Interface.jsp")
    Observable<com.example.wille.willing_audio.MessageModel.Message> add_comment(@Query("intent") String intent, @Body RequestBody requestBody);


    @POST
    Observable<ResponseBody> get_file(@Url String fileUrl);

    @Multipart
    @POST("functions/UploadUserAvator.jsp")
    Observable<Message_The_File> upload_file(
            @Query("intent") String intent,
            @Query("pid")  String pid,
            @Part MultipartBody.Part file);

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("functions/Interface.jsp")
    Observable<Message_UserSongLists> PostMessage(@Query("intent") String intent, @Body RequestBody requestBody);

    //获取随机歌单接口
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("functions/Interface.jsp")
    Observable<Message_SongLists> PostMessage_songlists(@Query("intent") String intent, @Body RequestBody requestBody);

    //获取随机歌曲接口
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("functions/Interface.jsp")
    Observable<Message_Songs> PostMessage_songs(@Query("intent") String intent, @Body RequestBody requestBody);


    //获取用户信息、登录、注册、新建歌单、删除歌单都是用的它
    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @POST("functions/Interface.jsp")
    Observable<Message_User> PostUser(@Query("intent") String intent, @Body RequestBody requestBody);


}
