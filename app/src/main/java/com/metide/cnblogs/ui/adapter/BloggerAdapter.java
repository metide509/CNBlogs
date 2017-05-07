package com.metide.cnblogs.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.metide.cnblogs.R;
import com.metide.cnblogs.service.entity.BloggerResult;
import com.metide.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author   metide
 * Date     2017/4/12
 */

public class BloggerAdapter extends RecyclerView.Adapter<BloggerAdapter.BlogViewHolder>{
    private Context mContext;
    private List<BloggerResult.Blogger> mBloggers;
    private OnBlogItemClickListener mOnBlogItemClickListener;

    public BloggerAdapter(Context context, OnBlogItemClickListener onClickListener){
        mContext = context;
        mOnBlogItemClickListener = onClickListener;
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_blogger, parent,false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BlogViewHolder holder, final int position) {
        final BloggerResult.Blogger blogger = mBloggers.get(position);

        Glide.with(mContext)
                .load(blogger.avatar)
                .centerCrop()
                .placeholder(R.drawable.ic_blog_author)
                .crossFade()
                .into(holder.headImage);

        holder.name.setText(blogger.title);
        holder.lastTime.setText(blogger.updated);
        holder.postCount.setText(blogger.postCount + "");
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnBlogItemClickListener != null){
                    mOnBlogItemClickListener.onClick(v,position, blogger);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBloggers != null ? mBloggers.size() : 0;
    }

    class BlogViewHolder extends RecyclerView.ViewHolder {

        CircleImageView headImage;
        TextView name;
        TextView lastTime;
        TextView postCount;
        LinearLayout linearLayout;

        public BlogViewHolder(View itemView) {
            super(itemView);
            headImage = (CircleImageView) itemView.findViewById(R.id.blogger_head_image);
            name = (TextView) itemView.findViewById(R.id.blogger_name);
            lastTime = (TextView) itemView.findViewById(R.id.blogger_last_time);
            postCount = (TextView) itemView.findViewById(R.id.blogger_post_count);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }
    }

    public interface OnBlogItemClickListener{
        void onClick(View view, int position, BloggerResult.Blogger blogger);
    }

    public void addDatas(List<BloggerResult.Blogger> bloggers){
        if(bloggers != null && bloggers.size() > 0){
            if(mBloggers == null){
                mBloggers = new ArrayList<>();
            }
            mBloggers.addAll(bloggers);
            notifyDataSetChanged();
        }
    }
}
