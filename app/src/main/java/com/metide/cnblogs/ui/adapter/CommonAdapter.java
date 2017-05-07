package com.metide.cnblogs.ui.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Author   metide
 * Date     2017/4/23
 */

public abstract class CommonAdapter<T>
        extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {

    private Context mContext;
    private List<T> mDatas;

    private View mItemView;


    protected Context getContext(){
        return mContext;
    }

    private OnItemClickListener<T> onItemClickListener;

    public CommonAdapter(Context context){
        mContext = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public void setDatas(List<T> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(getRecyclerLayout(), parent, false);

        return new ViewHolder(view);
    }

    public void onBindViewHolder(ViewHolder holder, int position) {
        final T t = mDatas.get(position);
        final int tempPos = position;
        if(onItemClickListener != null){
            mItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(mItemView, tempPos, t);
                }
            });
        }

        onBindViewHolder(holder, tempPos, t);
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
            mItemView = itemView;
        }
    }


    public interface OnItemClickListener<T>{
        void onClick(View view, int position, T t);
    }

    public void addDatas(List<T> datas){

        if(datas == null || datas.size() == 0) return;

        if(mDatas == null){
            mDatas = new ArrayList<>();
        }

        mDatas.addAll(datas);
    }

    public abstract @LayoutRes int getRecyclerLayout();
    public abstract void initView(View itemView);
    public abstract void onBindViewHolder(ViewHolder viewHolder, int position, T t);
}
