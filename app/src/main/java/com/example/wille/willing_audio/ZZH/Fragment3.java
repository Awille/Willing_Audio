package com.example.wille.willing_audio.ZZH;

/**
 * Created by Administrator on 2017/12/27.
 */


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.wille.willing_audio.Adapter_And_Service.CommonAdapter_touch;
import com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server;
import com.example.wille.willing_audio.Model.User;
import com.example.wille.willing_audio.R;
import com.example.wille.willing_audio.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.GetAllUsers;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.message_users;


public class Fragment3 extends Fragment {
    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.layout3, container, false);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.GETALL_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh_getall".equals(msg)){
                    Log.e("ZLG","layout3_broadcast!!!!");
                    setRecyclerView_layout3();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);


         return view;

    }
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        GetAllUsers(view.getContext());
        //setRecyclerView_layout3();
    }
    private void setRecyclerView_layout3(){
        ArrayList<Map<String,Object>> AllUserList=new ArrayList<Map<String,Object>>();
        if(message_users!=null&&message_users.data!=null){
            AllUserList.clear();
            ArrayList<User> userArrayList=message_users.data.userList;
            for(int i=0;i<userArrayList.size();i++){
                Map<String,Object> m1=new HashMap<>();
                String nickname=userArrayList.get(i).nickname;
                String avator=userArrayList.get(i).avator;
                m1.put("nickname",nickname);
                m1.put("avator",avator);
                AllUserList.add(m1);
                Log.e("ALL",nickname);
            }
        }
        RecyclerView RecyclerView_layout3=(RecyclerView)view.findViewById(R.id.RecyclerView_layout3);
        RecyclerView_layout3.setLayoutManager(new LinearLayoutManager(view.getContext()));  //设置布局管理器
        final CommonAdapter_touch<Map<String,Object>> commonAdapterTouch =new CommonAdapter_touch<Map<String,Object>>(getActivity(),R.layout.layout1_item2,AllUserList){
            @Override
            public void convert(ViewHolder viewHolder, Map<String,Object> s){
                TextView text_name1= viewHolder.getView(R.id.name2);
                ImageView img2=viewHolder.getView(R.id.img2);
                Communicate_with_Server.GetNetPic("UserAvator/"+s.get("avator").toString(),img2);
                text_name1.setText(s.get("nickname").toString());
            }
        };
        RecyclerView_layout3.setAdapter(commonAdapterTouch);

    }



}