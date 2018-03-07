package com.example.wille.willing_audio.ZZH;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.wille.willing_audio.Adapter_And_Service.CommonAdapter_touch;
import com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server;
import com.example.wille.willing_audio.Adapter_And_Service.Message;
import com.example.wille.willing_audio.Adapter_And_Service.NetWork;
import com.example.wille.willing_audio.Factory2.ServiceFactory2;
import com.example.wille.willing_audio.Interfaces2.Interfaces2;
import com.example.wille.willing_audio.MessageModel.Message_User;
import com.example.wille.willing_audio.Model.SongLists;
import com.example.wille.willing_audio.R;
import com.example.wille.willing_audio.SongListDetail;
import com.example.wille.willing_audio.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.AddSongList;
import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.DeleteSongList;
import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.GetLists;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.base_Uri;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.message_user;


/**
 * Created by Administrator on 2017/12/27.
 */

public class Fragment1 extends Fragment {
    private boolean visited=false;
    private View view;
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e("ZZH","here3");
        Log.e("ZZH",String.valueOf(visited));
//        if(visited==true){
//            setmRecycleView();
//            setmRecyclerView1();
//            setmRecyclerView2();
//        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.layout1, container, false);
        visited=true;
        return view;
    }
    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        setmRecycleView();
        setmRecyclerView1();
        setmRecyclerView2();
        addSongList();

        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.CART_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh".equals(msg)){
                    Log.e("ZLG","broadcast!!!!");
                    setmRecycleView();
                    setmRecyclerView1();
                    setmRecyclerView2();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
    }
    private void addSongList(){
        Button add_btn=(Button)view.findViewById(R.id.add_img);
        LayoutInflater layoutInflater=LayoutInflater.from(getActivity());
        final View dialoglayout=layoutInflater.inflate(R.layout.dialoglayout,null);
        final EditText dialog_edit_newlistname=(EditText)dialoglayout.findViewById(R.id.edit_newlistname);
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(dialoglayout)
                .setPositiveButton("创建", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newlistname=dialog_edit_newlistname.getText().toString();
                        if(message_user!=null&&message_user.data!=null) AddSongList(message_user.data.account,newlistname,view.getContext());
                        else Toast.makeText(view.getContext(),"登录后才能创建歌单",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("取消",null);
        final AlertDialog dialog = builder.create();//不可以放下面重复创建

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("ADD","intohere");
                dialog.show();
            }
        });
    }

    private void setmRecycleView(){
        final List<Map<String,Object>> List1=new ArrayList<>();
        Map<String,Object> m1=new HashMap<>();
        m1.put("name1","本地音乐");
        m1.put("img1",R.mipmap.music1_1);
        List1.add(m1);
        Map<String,Object> m2=new HashMap<>();
        m2.put("name1","最近播放");
        m2.put("img1",R.mipmap.history1_2);
        List1.add(m2);

        RecyclerView mRecyclerView=(RecyclerView)view.findViewById(R.id.mRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));  //设置布局管理器
        final CommonAdapter_touch<Map<String,Object>> commonAdapterTouch =new CommonAdapter_touch<Map<String,Object>>(getActivity(),R.layout.layout1_item1,List1){
            @Override
            public void convert(ViewHolder viewHolder, Map<String,Object> s){
                TextView text_name1= viewHolder.getView(R.id.name1);
                ImageView img1=viewHolder.getView(R.id.img1);
                Log.e("ZZH","before");
                img1.setImageResource(Integer.parseInt(String.valueOf(s.get("img1"))));
                text_name1.setText(s.get("name1").toString());
                Log.e("ZZH","after"+s.get("name1").toString());
            }
        };
        mRecyclerView.setAdapter(commonAdapterTouch);
        commonAdapterTouch.setOnItemClickListener(new CommonAdapter_touch.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(),String.valueOf(position),Toast.LENGTH_SHORT).show();
                if(position==0){
                    Toast.makeText(getActivity(),"scan local music",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getActivity(),LocalMusicList.class);
                    intent.putExtra("ListName","本地音乐");
                    startActivity(intent);
                }
                else if(position==1){
                    Intent intent=new Intent(getActivity(),LocalMusicList.class);
                    intent.putExtra("ListName","最近播放");
                    startActivity(intent);
                }
            }
            @Override
            public void onTouch(int position){
            }
            @Override
            public void onLongClick(int position) {

            }
        });


    }
    public void setmRecyclerView1(){Log.e("ZLG","HAHAHAHA");
        final List<Map<String,Object>> List2=new ArrayList<>();
        if(Message.Lists_mycreate!=null){
            List2.clear();
            Log.e("LOL","mycreate!=null");
            final SongLists mycreate_list=Message.Lists_mycreate;
            Log.e("F1",String.valueOf(mycreate_list.songLists.size()));
            for(int i=0;i<mycreate_list.songLists.size();i++){
                Map<String,Object> m1=new HashMap<>();
                Long listID=mycreate_list.songLists.get(i).listId;
                String name=mycreate_list.songLists.get(i).listName;
                String imagename=mycreate_list.songLists.get(i).listAvator;
                Log.e("PIC",imagename);
                String songnum=String.valueOf(mycreate_list.songLists.get(i).songs.songs.size());
                if(name.length()>=15) name=name.substring(0,14)+"...";
                m1.put("listID",listID);
                m1.put("name2",name);
                m1.put("img2",imagename);
                m1.put("songnum",songnum+"首");
                List2.add(m1);
                Log.e("F1",mycreate_list.songLists.get(i).listName);
            }
        }

        Log.e("LOL","setRV1pppppppppp");
        RecyclerView mRecyclerView1=(RecyclerView)view.findViewById(R.id.mRecyclerView1);
        mRecyclerView1.setNestedScrollingEnabled(false);
        mRecyclerView1.getLayoutParams().height = 240*List2.size();//一个item  240px
        mRecyclerView1.setLayoutManager(new LinearLayoutManager(view.getContext()));  //设置布局管理器
        final CommonAdapter_touch<Map<String,Object>> commonAdapterTouch =new CommonAdapter_touch<Map<String,Object>>(getActivity(),R.layout.layout1_item2,List2){
            @Override
            public void convert(ViewHolder viewHolder, Map<String,Object> s){
                TextView text_name2= viewHolder.getView(R.id.name2);
                ImageView img2=viewHolder.getView(R.id.img2);
                TextView text_songnum=viewHolder.getView(R.id.songnum);
                Log.e("ZZH","before");

                if(NetWork.isNetworkAvailable(view.getContext())==true){
                    Communicate_with_Server.GetNetPic("ListAvator/"+s.get("img2").toString(),img2);
                }
                text_name2.setText(s.get("name2").toString());
                text_songnum.setText(s.get("songnum").toString());
                Log.e("ZZH","after"+s.get("name2").toString());
                final String list_name=s.get("name2").toString();
                final Long listID= Long.valueOf(s.get("listID").toString());
                viewHolder.getView(R.id.point).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowBottomDialog(list_name,listID);
                    }
                });
            }
        };
        mRecyclerView1.setAdapter(commonAdapterTouch);
        commonAdapterTouch.setOnItemClickListener(new CommonAdapter_touch.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(),"Jump to SongList",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), SongListDetail.class);
                intent.putExtra("listID",List2.get(position).get("listID").toString());
                startActivity(intent);
            }
            @Override
            public void onTouch(int position){
            }
            @Override
            public void onLongClick(int position) {
                Toast.makeText(getActivity(),"you want to delete this list",Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("LOL","setRV1wwwwww");
    }





    private void setmRecyclerView2(){
        final List<Map<String,Object>> List3=new ArrayList<>();
        if(Message.Lists_mycollect!=null){
            SongLists mycollect_list=Message.Lists_mycollect;
            Log.e("F1",String.valueOf(mycollect_list.songLists.size()));
            for(int i=0;i<mycollect_list.songLists.size();i++){
                Map<String,Object> m1=new HashMap<>();
                Long listID=mycollect_list.songLists.get(i).listId;
                String name=mycollect_list.songLists.get(i).listName;
                String imagename=mycollect_list.songLists.get(i).listAvator;
                String songnum=String.valueOf(mycollect_list.songLists.get(i).songs.songs.size());
                if(name.length()>=10) name=name.substring(0,10)+"...";
                m1.put("listID",listID);
                m1.put("name2",name);
                m1.put("img2",imagename);
                m1.put("songnum",songnum+"首");
                List3.add(m1);
                Log.e("F1",mycollect_list.songLists.get(i).listName);
            }
        }


        RecyclerView mRecyclerView2=(RecyclerView)view.findViewById(R.id.mRecyclerView2);
        mRecyclerView2.setNestedScrollingEnabled(false);
        mRecyclerView2.getLayoutParams().height = 240*List3.size();//一个item  240px
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(view.getContext()));  //设置布局管理器
        final CommonAdapter_touch<Map<String,Object>> commonAdapterTouch =new CommonAdapter_touch<Map<String,Object>>(getActivity(),R.layout.layout1_item2,List3){
            @Override
            public void convert(ViewHolder viewHolder, Map<String,Object> s){
                TextView text_name2= viewHolder.getView(R.id.name2);
                ImageView img2=viewHolder.getView(R.id.img2);
                TextView text_songnum=viewHolder.getView(R.id.songnum);
                Log.e("ZZH","before");
                if(NetWork.isNetworkAvailable(view.getContext())==true){
                    Communicate_with_Server.GetNetPic("ListAvator/"+s.get("img2").toString(),img2);
                }
                text_name2.setText(s.get("name2").toString());
                text_songnum.setText(s.get("songnum").toString());
                Log.e("ZZH","after"+s.get("name2").toString());
                final String list_name=s.get("name2").toString();
                final Long listID= Long.valueOf(s.get("listID").toString());
                viewHolder.getView(R.id.point).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ShowBottomDialog(list_name,listID);
                    }
                });
            }
        };
        mRecyclerView2.setAdapter(commonAdapterTouch);
        commonAdapterTouch.setOnItemClickListener(new CommonAdapter_touch.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Toast.makeText(getActivity(),"Jump to SongList",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getActivity(), SongListDetail.class);
                intent.putExtra("listID",List3.get(position).get("listID").toString());
                startActivity(intent);
            }
            @Override
            public void onTouch(int position){
            }
            @Override
            public void onLongClick(int position) {
                Toast.makeText(getActivity(),"you want to delete this list",Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void ShowBottomDialog(final String list_name,final Long listID) {
        Log.e("DIA","showdialog"+list_name);
        final Dialog bottomDialog = new Dialog(getActivity(), R.style.BottomDialog);
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_content_normal, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        bottomDialog.setCanceledOnTouchOutside(true);
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();

        contentView.findViewById(R.id.delete_txt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("DIA","delete"+list_name);
                Log.e("DEL","DELETE"+listID.toString());
                DeleteSongList(listID,view.getContext());
                bottomDialog.cancel();
            }
        });
    }


}