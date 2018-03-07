package com.example.wille.willing_audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.sang.lrcview.LrcView;

import static com.example.wille.willing_audio.MusicService.mp;


/**
 * Created by Administrator on 2017\12\29 0029.
 */

public class LyricDisplay extends Fragment {

    private View v;
    private LinearLayout linearLayout;
    private BroadcastReceiver br;
    private LrcView lrcView;
    public static LyricDisplay newInstance() {
        LyricDisplay array= new LyricDisplay();
        return array;
    }
    private String [] path = {"", "123.lrc","thesoundofsilence.mp3", "k.mp3", "stressedOut.mp3"};
    String s= Environment.getExternalStorageDirectory()+"/data/";

    class albumStateChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("pre")||intent.getAction().equals("next")) {
                try {
                    lrcView.readLrcFile(SongListDetail.getSongList().get().lrc);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        br= new LyricDisplay.albumStateChangeReceiver();
        IntentFilter filter = new IntentFilter();

        filter.addAction("next");
        filter.addAction("pre");
        getContext().registerReceiver(br, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //在这里加载每个 fragment的显示的 View
        v = inflater.inflate(R.layout.lyricdisplay, container, false);
        linearLayout = (LinearLayout) v.findViewById(R.id.view);
        lrcView=new LrcView(linearLayout.getContext());
        try {
            lrcView.readLrcFile(SongListDetail.getSongList().get().lrc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        lrcView.setPlayer(mp);
        lrcView.init();
        linearLayout.addView(lrcView);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

}
