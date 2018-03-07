package com.example.wille.willing_audio.ZLG;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wille.willing_audio.Factory2.ServiceFactory2;
import com.example.wille.willing_audio.Interfaces2.Interfaces2;
import com.example.wille.willing_audio.MessageModel.Message_User;
import com.example.wille.willing_audio.R;
import com.example.wille.willing_audio.ZZH.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.AddSongList;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.base_Uri;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.message_user;


public class loginORregister extends AppCompatActivity {
    LinearLayout line1;
    Button btn;
    ImageView image;
    TextView textView;
    EditText nickname;
    EditText login;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_orregister);
        Log.e("ZLG","LoginPage");


        line1=(LinearLayout)findViewById(R.id.linelayout1);
        btn=(Button)findViewById(R.id.login_register);
        image=(ImageView)findViewById(R.id.return2);
        textView=(TextView)findViewById(R.id.logintitle);
        nickname=(EditText)findViewById(R.id.nicaknameEdit);
        login=(EditText)findViewById(R.id.loginEdit);
        password=(EditText)findViewById(R.id.passwordEdit);

        Bundle bundle=getIntent().getExtras();
        final String str1=bundle.getString("flag");
        if(str1.equals("load")){
            line1.setVisibility(View.INVISIBLE);
            btn.setText("登录");
            textView.setText("登录");
        }
        else{
            btn.setText("注册");
            textView.setText("注册");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_account=login.getText().toString();
                String user_password=password.getText().toString();
                if(str1.equals("load")){
                    Log.e("ZLG","load");
                    GetUser(user_account,user_password);
                }
                else{
                    String user_nickname=nickname.getText().toString();
                    Register(user_nickname,user_account,user_password);
                }

            }
        });

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void GetUser(final String user_account,final String user_password){
        ServiceFactory2 serviceFactory=new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces= serviceFactory.getRetrofit().create(Interfaces2.class);
        final JSONObject requetData=new JSONObject();
        try {
            requetData.put("account",user_account);
            requetData.put("password",user_password);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requetData.toString());
        interfaces.PostUser("sign_in",requestBody)
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
                        Log.e("ZLG","loginnext");
                        Log.e("ZLG",tem.data.password);
                        message_user=tem;
                        String real_passwprd=message_user.data.password;
                        if(real_passwprd.equals(user_password)){
                            Log.e("ZLG","password_right");
                            final SharedPreferences setting = getSharedPreferences("share",0);
                            SharedPreferences.Editor editor=setting.edit();
                            editor.putString("user_account",user_account);
                            editor.putString("user_password",user_password);
                            editor.commit();
                            Log.e("ZLG","go_back_to_MainActivity");
                            Intent intent=new Intent(loginORregister.this,MainActivity.class);
                            startActivityForResult(intent,1);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"密码错误",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void Register(final String user_nickname,final String user_account,final String user_password){
        ServiceFactory2 serviceFactory=new ServiceFactory2(base_Uri);
        final Interfaces2 interfaces= serviceFactory.getRetrofit().create(Interfaces2.class);
        final JSONObject requetData=new JSONObject();
        try {
            requetData.put("nickname",user_nickname);
            requetData.put("account",user_account);
            requetData.put("password",user_password);
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("application/json"),requetData.toString());
        interfaces.PostUser("register",requestBody)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message_User>() {
                    @Override
                    public void onCompleted() {
                        AddSongList(user_account,"我喜欢的音乐",getApplicationContext());
                        Log.e("LOL","获得用户信息");
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(),"login用户不存在",Toast.LENGTH_SHORT).show();
                        Log.d("LOL",e.getMessage());
                    }
                    @Override
                    public void onNext(Message_User tem) {
                        Log.e("ZLG",tem.state);
                        //Message_User message_user=tem;
                        if(tem.state.equals("success")){
                            Log.e("ZLG","register_success");
                            Intent intent=new Intent(loginORregister.this,homepage.class);
                            startActivityForResult(intent,1);
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"账号已存在，请重新输入",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==100){
            Toast.makeText(getApplicationContext(),"exit",Toast.LENGTH_SHORT).show();
            setResult(100,new Intent());
            finish();
        }
    }
}