package com.example.wille.willing_audio;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wille on 2017/12/14.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected ViewHolder viewHolder;
    private OnItemClickListener mOnItemClickLstener;

    //private AdapterView.OnItemLongClickListener onItemLongClickListener;

    public CommonAdapter(Context context, int layoutId, List<T> datas){
        mContext=context;
        mLayoutId=layoutId;
        mInflater=LayoutInflater.from(context);
        mDatas=datas;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        ViewHolder viewHolder_tmp=ViewHolder.get(mContext,parent,mLayoutId);
        viewHolder=viewHolder_tmp;
        return viewHolder_tmp;
    }

    public View getViewByPos(int i) {
        return viewHolder.getView(i);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){
        //holder.updatePosition(position);
        convert(holder,mDatas.get(position));
        if(mOnItemClickLstener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    mOnItemClickLstener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    mOnItemClickLstener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public abstract void convert(ViewHolder holder,T t);

    @Override
    public int getItemCount(){
        return mDatas.size();
    }

    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.mOnItemClickLstener=onItemClickListener;
    }
}
