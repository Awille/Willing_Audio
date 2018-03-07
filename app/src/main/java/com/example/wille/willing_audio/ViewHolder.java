package com.example.wille.willing_audio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by wille on 2017/12/14.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    public SparseArray<View> mViews;//存储list_item的子view
    private View mConvertView; //存储list_item
    private Context mContext;
    public ViewHolder(Context context, View itemView, ViewGroup parent){
        super(itemView);
        mContext=context;
        mConvertView=itemView;
        mViews=new SparseArray<View>();
    }
    public static ViewHolder get(Context context, ViewGroup parent, int layoutId){
        View itemView = LayoutInflater.from(context).inflate(layoutId,parent,false);
        ViewHolder holder=new ViewHolder(context,itemView,parent);
        return holder;
    }


    public <T extends  View> T getView(int viewId){
        View view=mViews.get(viewId);
        if(view==null){
            //创建view
            view=mConvertView.findViewById(viewId);
            //将view存入mViews
            mViews.put(viewId,view);
        }
        return (T) view;
    }
    public ViewHolder setText(int viewId, String text){
        TextView tv=getView(viewId);
        tv.setText(text);
        return this;
    }
    public ViewHolder setImageResource(int viewID, int resId){
        ImageView view=getView(viewID);
        view.setImageResource(resId);
        return this;
    }
    public  ViewHolder setOnClickListener(int viewId,View.OnClickListener listener){
        View view=getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }
}
