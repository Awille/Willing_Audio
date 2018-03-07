package com.example.wille.willing_audio.ZZH;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.example.wille.willing_audio.R;

import java.util.ArrayList;
import java.util.Map;

public class SearchSong extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_song);
        Bundle bundle = getIntent().getExtras();
        final ArrayList bundlelist = bundle.getParcelableArrayList("bundlelist");
        //从List中将参数转回 List<Map<String, Object>>
        final ArrayList<Map<String, Object>> list= (ArrayList<Map<String, Object>>)bundlelist.get(0);

        //String musicSource=list.get(index).get("uri").toString();
        final EditText editText=findViewById(R.id.edit_txt);
        final ArrayList<Map<String, Object>> search_list= new ArrayList<Map<String, Object>>();

        ListView listView=(ListView)findViewById(R.id.listView);
        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, search_list,
                R.layout.detail_item, new String[] {"order","title", "artist"},
                new int[] {R.id.order,R.id.song_title,R.id.song_artist});
        listView.setAdapter(simpleAdapter);
        ImageView imageView=findViewById(R.id.searchimg);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_list.clear();
                String search_str=editText.getText().toString();
                for(int i=0;i<list.size();i++){
                    String title=list.get(i).get("title").toString();
                    String artist=list.get(i).get("artist").toString();
                    if(title.contains(search_str)||artist.contains(search_str)){
                        search_list.add(list.get(i));
                        simpleAdapter.notifyDataSetChanged();
                    }
                }
            }
        });



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String address=LocalMusicList.get(position).get("uri").toString();
                Intent intent=new Intent(SearchSong.this,MusicPlaying_ZZH.class);
                intent.putExtra("position",position);
                //intent.putExtra("MusicSource",address);
                //intent.putStringArrayListExtra("MusicList",LocalMusicList);
                Bundle bundle = new Bundle();//须定义一个list用于在budnle中传递需要传递的ArrayList<Object>,这个是必须要的
                ArrayList bundlelist = new ArrayList();
                bundlelist.add(search_list);
                bundle.putParcelableArrayList("bundlelist",bundlelist);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
}
