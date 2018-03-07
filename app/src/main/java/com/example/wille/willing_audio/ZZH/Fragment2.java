package com.example.wille.willing_audio.ZZH;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wille.willing_audio.Adapter_And_Service.CommonAdapter_touch;
import com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server;
import com.example.wille.willing_audio.Adapter_And_Service.SpaceItemDecoration;
import com.example.wille.willing_audio.R;
import com.example.wille.willing_audio.SongListDetail;
import com.example.wille.willing_audio.ViewHolder;
import com.example.wille.willing_audio.ZLG.RecommendedSongList;

import java.util.ArrayList;
import java.util.List;

import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.GetRandomList;
import static com.example.wille.willing_audio.Adapter_And_Service.Communicate_with_Server.GetRandomSongs;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.RandomLists;
import static com.example.wille.willing_audio.Adapter_And_Service.Message.RandomSongs;


/**
 * Created by Administrator on 2017/12/27.
 */
public class Fragment2 extends Fragment {
    private boolean visited=false;
    private View view;
    List<String> mDatas1;
    List<RecommendedSongList> mDatas2;
    RecyclerView mRecyclerView1;
    RecyclerView mRecyclerView2;
    CommonAdapter_touch commonAdapterTouch1;
    CommonAdapter_touch commonAdapterTouch2;
    int direction=0;
    static int flag=0;
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.e("ZZH","here3");
        Log.e("ZZH",String.valueOf(visited));
        if(visited==true){

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.layout2, container, false);
        LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.Random_BROADCAST");
        BroadcastReceiver mItemViewListClickReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent){
                String msg = intent.getStringExtra("data");
                if("refresh_random".equals(msg)){
                    Log.e("ZLG","random_broadcast!!!!");
                    main();
                }
            }
        };
        broadcastManager.registerReceiver(mItemViewListClickReceiver, intentFilter);
        Log.e("ZZH","here2");
        visited=true;
        return view;
    }
    @Override
    public void onActivityCreated(Bundle saveInstanceState){
        super.onActivityCreated(saveInstanceState);
        GetRandomList(view.getContext());
        GetRandomSongs(view.getContext());
    }
    private void main(){



        mRecyclerView1=(RecyclerView)view.findViewById(R.id.mrecyclerview1);
        mRecyclerView2=(RecyclerView)view.findViewById(R.id.mrecyclerview2);

        mDatas1=new ArrayList<>();

        if(RandomSongs!=null){
            for(int i=0;i<RandomSongs.songs.size();i++){
                mDatas1.add(RandomSongs.songs.get(i).songAvator);
            }
        }

        mDatas2=new ArrayList<>();

        if(RandomLists!=null){
            for(int i=0;i<RandomLists.songLists.size();i++){
                RecommendedSongList recommendedSongList1=new RecommendedSongList(RandomLists.songLists.get(i).listAvator,RandomLists.songLists.get(i).listName);
                mDatas2.add(recommendedSongList1);
            }
        }






        GridLayoutManager layoutManage = new GridLayoutManager(getActivity(), 3);
        mRecyclerView2.setLayoutManager(layoutManage);
        commonAdapterTouch2 =new CommonAdapter_touch<RecommendedSongList>(getActivity(),R.layout.rcyview2,mDatas2){
            public void convert(ViewHolder holder, RecommendedSongList s){
                ImageView img=holder.getView(R.id.rcy2_img);
                TextView text1=holder.getView(R.id.rcy2_text);
                Communicate_with_Server.GetNetPic("ListAvator/"+s.img,img);
                String str1=s.listname;
                String str2="";
                if(str1.length()>16) str2=str1.substring(0,15)+"...";
                else str2=str1;
                text1.setText(str2);

            }
        };
        mRecyclerView2.addItemDecoration(new SpaceItemDecoration(5));
        mRecyclerView2.setAdapter(commonAdapterTouch2);








        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        linearLayoutManager.setSmoothScrollbarEnabled(false);
        mRecyclerView1.setLayoutManager(linearLayoutManager);
        commonAdapterTouch1 =new CommonAdapter_touch<String>(getActivity(), R.layout.rcyview1,mDatas1){
            public void convert(ViewHolder holder,String s){
                ImageView img=holder.getView(R.id.rcy1_img);
                Communicate_with_Server.GetNetPic("SongAvator/"+s,img);
            }
        };

        mRecyclerView1.setAdapter(commonAdapterTouch1);
        final Handler mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                if(flag==0)
                    switch(msg.what){
                        case 123:
                            try {
                                int firstVisibleItemPosition=linearLayoutManager.findFirstVisibleItemPosition();//可见范围内的第一项的位置
                                int lastVisibleItemPosition=linearLayoutManager.findLastVisibleItemPosition();//可见范围内的最后一项的位置
                                if(direction==0){
                                    if(lastVisibleItemPosition>=6) direction=1;
                                    if(lastVisibleItemPosition<7)  mRecyclerView1.smoothScrollToPosition(lastVisibleItemPosition+1);
                                }
                                else if(direction==1){
                                    if(firstVisibleItemPosition<=1) direction=0;
                                    if(lastVisibleItemPosition>0) mRecyclerView1.smoothScrollToPosition(firstVisibleItemPosition-1);
                                }


                            } catch (Exception a) {
                                a.printStackTrace();
                            }
                            ;
                            break;

                    }
            }
        };
        final Thread mThread=new Thread(){
            @Override
            public void run(){
                while(true){
                    try{
                        Thread.sleep(4000);
                    }
                    catch(InterruptedException e){
                        e.printStackTrace();
                    }
                    if(flag==0){
                        mHandler.obtainMessage(123).sendToTarget();
                    }


                }
            }
        };
        mThread.start();


        commonAdapterTouch1.setOnItemClickListener(new CommonAdapter_touch.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(view.getContext(), SongListDetail.class);
                intent.putExtra("listID",String.valueOf(RandomLists.songLists.get(position).listId));
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
            @Override
            public void onTouch(int position){
                flag=1;
            }
        });
        commonAdapterTouch2.setOnItemClickListener(new CommonAdapter_touch.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent=new Intent(view.getContext(), SongListDetail.class);
                intent.putExtra("listID",String.valueOf(RandomLists.songLists.get(position).listId));
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
            @Override
            public void onTouch(int position){
                flag=1;
            }
        });
        mRecyclerView1.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int count=0;
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                count+=dx;
            }
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch(newState)
                {
                    case 0:
                        int firstVisibleItemPosition=linearLayoutManager.findFirstVisibleItemPosition();//可见范围内的第一项的位置
                        int lastVisibleItemPosition=linearLayoutManager.findLastVisibleItemPosition();//可见范围内的最后一项的位置
                        if(count>0) direction=0;
                        else direction=1;
                        if(count>0&&lastVisibleItemPosition<mDatas1.size()&&lastVisibleItemPosition>=0) {

                            mRecyclerView1.smoothScrollToPosition(lastVisibleItemPosition);
                        }
                        else if(firstVisibleItemPosition<mDatas1.size()&&firstVisibleItemPosition>=0)mRecyclerView1.smoothScrollToPosition(firstVisibleItemPosition);
                        count=0;
//                        System.err.println(count)
                        flag=0;
                        System.out.println("recyclerview已经停止滚动");
                        break;
                    case 1:

                        System.out.println("recyclerview正在被拖拽");
                        break;
                    case 2:
                        System.out.println("recyclerview正在依靠惯性滚动");
                        break;
                }
            }
        });
    }




}
















