package com.metide.cnblogs.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.metide.cnblogs.R;
import com.metide.cnblogs.service.entity.BlogResult;
import com.metide.cnblogs.service.entity.BloggerResult;
import com.metide.cnblogs.ui.activity.BloggerActivity;
import com.metide.cnblogs.interfaces.OnToWebListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Author   metide
 * Date     2017/4/9
 */

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private Context mContext;
    private List<BlogResult.Blog> mBlogs = Collections.synchronizedList(new ArrayList<BlogResult.Blog>());
    private OnToWebListener mOnToWebListener;

    public BlogAdapter(Context context){
        mContext = context;
    }

    public void setOnToWebListener(OnToWebListener toWebListener){
        this.mOnToWebListener = toWebListener;
    }

    public void setDatas(List<BlogResult.Blog> blogs){
        this.mBlogs = blogs;
        notifyDataSetChanged();
    }

    public synchronized void addData(BlogResult.Blog blog){
        if(blog != null){
            mBlogs.add(blog);
            notifyItemInserted(mBlogs.size() - 1);
        }
    }


    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_item_blog, parent,false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BlogViewHolder holder, final int position) {
        final BlogResult.Blog blog = mBlogs.get(position);
        Glide.with(mContext)
                .load(TextUtils.isEmpty(blog.author.avatar) ? R.drawable.ic_load_error : blog.author.avatar)
                .centerCrop()
                .placeholder(R.drawable.ic_load_error)
                .crossFade()
                .error(R.drawable.ic_load_error)
                .into(holder.headImage);

        holder.title.setText(blog.title);
        holder.time.setText(blog.published);
        holder.summary.setText(blog.summary);
        holder.authorName.setText(blog.author.name);
        holder.comment.setText(blog.comments + "");
        holder.view.setText(blog.views + "");

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnToWebListener != null){
                    mOnToWebListener.toWeb(v,position, blog.link.href);
                }
            }
        });

        holder.authorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BloggerActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBlogs != null ? mBlogs.size() : 0;
    }

    public void addDatas(List<BlogResult.Blog> blogs) {
        if(blogs != null && blogs.size() > 0){
            if(mBlogs == null){
                mBlogs = new ArrayList<>();
            }
            mBlogs.addAll(blogs);
            notifyDataSetChanged();
        }
    }

    class BlogViewHolder extends RecyclerView.ViewHolder {

        ImageView headImage;
        TextView title;
        TextView time;
        TextView summary;
        TextView authorName;
        TextView comment;
        TextView view;
        LinearLayout linearLayout;

        public BlogViewHolder(View itemView) {
            super(itemView);
            headImage = (ImageView) itemView.findViewById(R.id.blog_author_image);
            title = (TextView) itemView.findViewById(R.id.blog_title);
            time = (TextView) itemView.findViewById(R.id.blog_time);
            summary = (TextView) itemView.findViewById(R.id.blog_summary);
            authorName = (TextView) itemView.findViewById(R.id.blog_author_name);
            comment = (TextView) itemView.findViewById(R.id.blog_comment);
            view = (TextView) itemView.findViewById(R.id.blog_view);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }
    }
}
