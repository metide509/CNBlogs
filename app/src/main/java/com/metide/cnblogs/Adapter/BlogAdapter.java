package com.metide.cnblogs.Adapter;

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
import com.metide.cnblogs.activity.BloggerActivity;
import com.metide.cnblogs.bean.Blog;
import com.metide.cnblogs.bean.Blogger;
import com.metide.cnblogs.interfaces.OnToWebListener;
import com.metide.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Author   metide
 * Date     2017/4/9
 */

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.BlogViewHolder> {

    private Context mContext;
    private List<Blog> mBlogs;
    private OnToWebListener mOnToWebListener;

    public BlogAdapter(Context context, List<Blog> blogs, OnToWebListener onToWebListener){
        mContext = context;
        mBlogs = blogs;
        mOnToWebListener = onToWebListener;
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_item_blog, parent,false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BlogViewHolder holder, final int position) {
        final Blog blog = mBlogs.get(position);


        Glide.with(mContext)
                .load(TextUtils.isEmpty(blog.authorImage) ? R.drawable.ic_load_error : blog.authorImage)
                .centerCrop()
                .placeholder(R.drawable.ic_load_error)
                .crossFade()
                .error(R.drawable.ic_load_error)
                .into(holder.headImage);

        holder.title.setText(blog.title);
        holder.time.setText(blog.time);
        holder.summary.setText(blog.summary);
        holder.authorName.setText(blog.authorName);
        holder.comment.setText(blog.comment + "");
        holder.view.setText(blog.view + "");

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnToWebListener != null){
                    mOnToWebListener.toWeb(v,position, blog.link);
                }
            }
        });

        holder.authorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, BloggerActivity.class);
                Blogger blogger = new Blogger();
                blogger.headImage = blog.authorImage;
                blogger.name = blog.authorName;
                blogger.link = blog.authorLink;
                intent.putExtra("blogger", blogger);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBlogs != null ? mBlogs.size() : 0;
    }

    public void notifyData(List<Blog> blogs) {
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
