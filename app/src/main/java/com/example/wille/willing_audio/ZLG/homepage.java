package com.example.wille.willing_audio.ZLG;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.wille.willing_audio.R;


public class homepage extends AppCompatActivity {
    Button load,register;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Log.e("ZLG","Homepage");
        load=(Button)findViewById(R.id.load);
        register=(Button)findViewById(R.id.register);

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(homepage.this,loginORregister.class);
                intent1.putExtra("flag","load");
                startActivityForResult(intent1,1);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(homepage.this,loginORregister.class);
                intent1.putExtra("flag","register");
                startActivityForResult(intent1,1);
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode==100){
            Toast.makeText(getApplicationContext(),"exit_homepage",Toast.LENGTH_SHORT).show();
            setResult(100,new Intent());
            finish();
        }
    }
}
