package com.example.wille.willing_audio.ZLG;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;


import com.example.wille.willing_audio.Adapter_And_Service.CommonAdapter_touch;
import com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server;
import com.example.wille.willing_audio.Adapter_And_Service.NetWork;
import com.example.wille.willing_audio.Factory2.ServiceFactory2;
import com.example.wille.willing_audio.Interfaces2.Interfaces2;
import com.example.wille.willing_audio.MessageModel.Message_User;
import com.example.wille.willing_audio.R;
import com.example.wille.willing_audio.ViewHolder;
import com.example.wille.willing_audio.Adapter_And_Service.Message.*;
import com.example.wille.willing_audio.factory.ServiceFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.UploadFile;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.base_Uri;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.message_user;


public class alter extends AppCompatActivity {
    List<String> mDatas;
    CommonAdapter_touch<String> commonAdapterTouch;
    SimpleAdapter simpleAdapter;
    RecyclerView mRecyclerView;
    List<Map<String,Object>> data;
    ListView mlistview;
    CircleImageView img;
    ImageView return_;
    String First[]={"昵称","性别","喜欢的歌手","喜欢的乐曲","个性签名"};
    String Second[]={"邹狗蛋","女","薛之谦","丑八怪","邹海枫是个傻逼"};
    public static int change_image=0;
    private static int RESULT_LOAD_IMAGE = 1;
    public static String imgpath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter);

        Log.e("ZLG","gointoalter");
        data=new ArrayList<>();
        mDatas = new ArrayList<String>();
        if(change_image==0)
            mDatas.add("123");
        else mDatas.add(imgpath);

        mRecyclerView=(RecyclerView)findViewById(R.id.alterRecyclerView);
        mlistview=(ListView)findViewById(R.id.alterListview);
        return_=(ImageView)findViewById(R.id.return1);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        commonAdapterTouch =new CommonAdapter_touch<String>(this, R.layout.rcy1,mDatas){
            public void convert(ViewHolder holder, String s){
                img=holder.getView(R.id.personcharacter);
                if(NetWork.isNetworkAvailable(getApplicationContext())==true){
                    Communicate_with_Server.GetNetPic("UserAvator/"+message_user.data.avator,img);
                }
//                if(change_image==1){
//                    img.setImageURI(Uri.fromFile(new File(s)));
//                }

            }
        };
        mRecyclerView.setAdapter(commonAdapterTouch);
        commonAdapterTouch.setOnItemClickListener(new CommonAdapter_touch.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//打开本地相册
                startActivityForResult(i, RESULT_LOAD_IMAGE); //设定结果返回
            }

            @Override
            public void onLongClick(int position) {


            }
            @Override
            public void onTouch(int position) {

            }

        });
        Log.e("ZLG","alter1");

         if(message_user!=null&&message_user.data!=null){
            if(message_user.data.nickname!=null){
                Second[0]=message_user.data.nickname;
            }
            else Second[0]="无";
            if(message_user.data.sex!=null)
            Second[1]=message_user.data.sex;
            else Second[1]="无";
            if(message_user.data.favourite_singer!=null)
            Second[2]=message_user.data.favourite_singer;
            else Second[2]="无";
            if(message_user.data.favourite_song!=null)
            Second[3]=message_user.data.favourite_song;
            else Second[3]="无";
            if(message_user.data.signature!=null)
            Second[4]=message_user.data.signature;
            else Second[4]="无";
        }

        Log.e("ZLG","alter2");



        for(int i=0;i<First.length;i++){
            Map<String,Object> temp=new LinkedHashMap<>();
            temp.put("first",First[i]);
            temp.put("second",Second[i]);
            data.add(temp);
        }

        simpleAdapter=new SimpleAdapter(this,data,R.layout.list,
                new String[]{"first","second"},new int[]{R.id.first,R.id.second});
        mlistview.setAdapter(simpleAdapter);
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final EditText et = new EditText(alter.this);
                if(position==1)
                et.setFilters(new InputFilter[] {
                        new InputFilter() {
                            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                                if(source.length()>1) return "";
                                for (int i = start; i < end; i++) {
                                    if (source.charAt(i)!='男' && source.charAt(i)!='女' )
                                    {
                                        return "";
                                    }
                                }
                                return source;
                            }  }});
                new AlertDialog.Builder(alter.this).setTitle("请输入").setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str=et.getText().toString();
                                if(str.equals("")) str="无";
                                data.clear();
                                Second[position]=str;
                                message_user.data.nickname=Second[0];
                                message_user.data.sex=Second[1];
                                message_user.data.favourite_singer=Second[2];
                                message_user.data.favourite_song=Second[3];
                                message_user.data.signature=Second[4];
                                for(int i=0;i<First.length;i++){
                                    Map<String,Object> temp=new LinkedHashMap<>();
                                    temp.put("first",First[i]);
                                    temp.put("second",Second[i]);
                                    data.add(temp);
                                }
                                simpleAdapter.notifyDataSetChanged();
                                Updatemydata(Second[0],Second[1],Second[2],Second[3],Second[4]);
                            }
                        })
                        .setNegativeButton("取消", null).show();
            }
        });


        return_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(200, new Intent());
                finish();
            }
        });

    }

    public void Updatemydata(final String user_nickname,final String user_sex,
                             final String user_favoritesinger,final String user_favoritesong,final String user_signature){
        ServiceFactory2 serviceFactory=new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces= serviceFactory.getRetrofit().create(Interfaces2.class);
        final JSONObject requetData=new JSONObject();
        try {
            requetData.put("account",message_user.data.account);
            requetData.put("password",message_user.data.password);
            requetData.put("avator","avatorrr");
            requetData.put("nickname",user_nickname);
            requetData.put("sex",user_sex);
            requetData.put("favourite_singer",user_favoritesinger);
            requetData.put("favourite_song",user_favoritesong);
            requetData.put("signature",user_signature);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requetData.toString());
        interfaces.PostUser("modify_user",requestBody)
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
                     //   message_user=tem;
                        //String state=message_user.state;
//                       Log.e("ZLG","update"+state);
                    }
                });
    }

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
                    img.setImageBitmap(bitmap);
                    System.out.println("GOOD");
                    //第二种方式去读取路径
                    Cursor cursor =this.getContentResolver().query(uri, null, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    String image_path=path;
                    Log.e("ZZHUP",image_path);

                    UploadFile(image_path,getApplicationContext());

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    System.out.println("BAD");
                }
            }
        }

    }

}