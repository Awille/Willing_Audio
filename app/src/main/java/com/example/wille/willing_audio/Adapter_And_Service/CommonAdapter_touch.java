package com.example.wille.willing_audio.Adapter_And_Service;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.wille.willing_audio.ViewHolder;

import java.util.List;

/**
 * Created by lgla on 2017/12/21.
 */
//
//public abstract class CommonAdapter_touch<T> extends RecyclerView.Adapter<ViewHolder>
//{
//    protected Context mContext;
//    protected int mLayoutId;
//    protected List<T> mDatas;
//    protected LayoutInflater mInflater;
//    protected OnItemClickListener mOnItemClickListener;
//
//
//    public CommonAdapter_touch(Context context, int layoutId, List<T> datas)
//    {
//        mContext = context;
//        mInflater = LayoutInflater.from(context);
//        mLayoutId = layoutId;
//        mDatas = datas;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
//    {
//        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mLayoutId);
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position)
//    {
//        convert(holder, mDatas.get(position));
//        if(mOnItemClickListener!=null){
//            holder.itemView.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v){
//                    mOnItemClickListener.onClick(holder.getAdapterPosition());
//                }
//            });
//            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    mOnItemClickListener.onTouch(holder.getAdapterPosition());
//                    return true;
//                }
//            });
//            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
//                @Override
//                public boolean onLongClick(View v){
//                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
//                    return false;
//                }
//            });
//        }
//    }
//
//    public abstract void convert(ViewHolder holder, T t);
//
//    @Override
//    public int getItemCount()
//    {
//        return mDatas.size();
//    }
//    public interface  OnItemClickListener{//接口
//        void onClick(int position);
//        void onLongClick(int position);
//        void onTouch(int position);
//    }
//    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
//        this.mOnItemClickListener=onItemClickListener;
//    }
//}


/**
 * Created by Administrator on 2017/10/21.
 */

public abstract  class CommonAdapter_touch<T> extends RecyclerView.Adapter<ViewHolder>{
    protected Context mContext;
    protected int mLayoutId;
    public List<T> mDatas;
    protected LayoutInflater mInflater;
    private OnItemClickListener mOnItemClickListener;

    public CommonAdapter_touch(Context context, int layoutId, List<T> datas)
    {
        mContext=context;
        mInflater=LayoutInflater.from(context);
        mLayoutId=layoutId;
        mDatas=datas;
    }
    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent,int viewType)//创建item视图，并返回相应的viewholder
    {
        ViewHolder viewHolder=ViewHolder.get(mContext,parent,mLayoutId);
        return viewHolder;
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
        void onTouch(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickListener=onItemClickListener;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)//绑定数据到正确的item视图上
    {
        convert(holder,mDatas.get(position));
        if(mOnItemClickListener!=null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v)
                {
                    mOnItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    mOnItemClickListener.onTouch(holder.getAdapterPosition());
                    return false;
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    mOnItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public abstract void convert(ViewHolder holder, T t);

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }



}