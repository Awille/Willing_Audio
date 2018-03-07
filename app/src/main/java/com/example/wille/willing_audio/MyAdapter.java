package com.example.wille.willing_audio;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wille.willing_audio.Model.SongList;

import java.util.List;
import java.util.Map;

import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.AddSongToList;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.Lists_mycollect;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.Lists_mycreate;
import static com.example.wille.willing_audio.MusicService.currentSong;
import static com.example.wille.willing_audio.Player.builder;
import static com.example.wille.willing_audio.SongListDetail.getSongList;


/**
 * Created by Administrator on 2017\12\31 0031.
 */

public class MyAdapter extends BaseAdapter {
    private List<Map<String, Object>> list;
    private int resource;
    private LayoutInflater inflater;
    private Context context;
    private ServiceConnection mConnection;
    private IBinder mBinder;
    private Parcel data=Parcel.obtain();
    private Parcel reply=Parcel.obtain();

    public MyAdapter(List<Map<String, Object>> list, Context context, int resource) {
        this.list = list;
        this.resource = resource;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mConnection = new ServiceConnection(){ // ServiceConnection实例化
            @Override
            public void onServiceConnected(ComponentName name, IBinder
                    service) {
                mBinder = service;
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mConnection = null;
            }
        };
        Intent intent = new Intent(context,MusicService.class);
// 绑定activity和服务
        context.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, null);
        }
        final Map<String, Object> map = list.get(position);
        ((TextView) convertView.findViewById(R.id.songName)).setText(map.get("songName").toString());
        ((TextView) convertView.findViewById(R.id.songName)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.setDataPosition(0);
                data.writeInt(position);
                data.writeInt(0);
                try {
                    mBinder.transact(109, data, reply, 0);
                } catch (Exception e) {e.printStackTrace();}
            }
        });
        ((Button) convertView.findViewById(R.id.delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSongList().delSong(position);
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        ((Button) convertView.findViewById(R.id.like)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("abccc", "onClick: ");
                String [] songList=new String[Lists_mycreate.songLists.size()];
                Log.d("abccc", "11223: ");
                for (int i=0;i<Lists_mycreate.songLists.size();++i) {
                    songList[i]=getSongLists(i).listName;
                }
                Log.d("abccc", "SADASD: ");


                Log.d("abccc", "SADAasdasdSD: ");
                builder.setTitle("选择收藏到哪一个歌单")
                        .setSingleChoiceItems(songList, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(context, "你了歌选择收藏到单:"+getSongLists(which).listName, Toast.LENGTH_SHORT).show();
                                //listId= getSongLists(which).listId;
                                AddSongToList(String.valueOf(currentSong.songId),String.valueOf(getSongLists(which).listId));
                                //herehrehrhehrehrehhrehrhehrehrherhehrhrhehrherherhehrherherhdkjsahdkahdherkjwherknmn
                                //sajkhdkasjhdkashd
                                //dsajkhdksahd




                                dialog.cancel();
                            }
                        })

                        .create().show();

            }
        });
        return convertView;
    }

    private SongList getSongLists(int i) {
        return Lists_mycreate.songLists.get(i);
    }

}