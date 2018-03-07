package com.example.wille.willing_audio.ZZH;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


import com.example.wille.willing_audio.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LocalMusicList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_list);
        main();
    }
    private void main(){
        Log.e("ZZH","afterjump");
        Intent receive_intent=getIntent();
        String ListName=receive_intent.getStringExtra("ListName");
        final TextView SongListName=(TextView)findViewById(R.id.SongListName);
        SongListName.setText(ListName);

        final ArrayList<Map<String,Object>> LocalMusicList=new ArrayList<>();
        Cursor cursor = this.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        int order=0;
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            //歌曲信息
            order++;
            String title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
            String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
            String uriData = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            long size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
            Map<String,Object> m=new HashMap<>();
            m.put("order",String.valueOf(order));
            m.put("title",title);
            m.put("artist",artist);
            m.put("uri",uriData);
            LocalMusicList.add(m);
        }
        ListView listView=(ListView)findViewById(R.id.listView);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, LocalMusicList,
            R.layout.detail_item, new String[] {"order","title", "artist"},
            new int[] {R.id.order,R.id.song_title,R.id.song_artist});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String address=LocalMusicList.get(position).get("uri").toString();
                Intent intent=new Intent(LocalMusicList.this,MusicPlaying_ZZH.class);
                intent.putExtra("position",position);
                //intent.putExtra("MusicSource",address);
                //intent.putStringArrayListExtra("MusicList",LocalMusicList);
                Bundle bundle = new Bundle();//须定义一个list用于在budnle中传递需要传递的ArrayList<Object>,这个是必须要的
                ArrayList bundlelist = new ArrayList();
                bundlelist.add(LocalMusicList);
                bundle.putParcelableArrayList("bundlelist",bundlelist);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        ImageView back_img=(ImageView) findViewById(R.id.imageback);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageView search_img=(ImageView)findViewById(R.id.search_img);
        search_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LocalMusicList.this, SearchSong.class);
                Bundle bundle = new Bundle();//须定义一个list用于在budnle中传递需要传递的ArrayList<Object>,这个是必须要的
                ArrayList bundlelist = new ArrayList();
                bundlelist.add(LocalMusicList);
                bundle.putParcelableArrayList("bundlelist",bundlelist);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

}
