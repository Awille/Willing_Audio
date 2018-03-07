package com.example.wille.willing_audio.ZZH;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.wille.willing_audio.R;


/**
 * Created by Administrator on 2017/12/29.
 */

public class RecentlyPlayed extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_list);
        main();
    }
    private void main(){
        Intent receive_intent=getIntent();
        String ListName=receive_intent.getStringExtra("ListName");
        final TextView SongListName=(TextView)findViewById(R.id.SongListName);
        SongListName.setText(ListName);
    }
}
