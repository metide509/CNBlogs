package com.metide.cnblogs.Adapter;

import android.content.Context;
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
import com.metide.cnblogs.bean.News;
import com.metide.cnblogs.interfaces.OnToWebListener;

import java.util.List;

/**
 * Author   metide
 * Date     2017/4/15
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.BlogViewHolder> {

    private Context mContext;
    private List<News> mNewses;
    private OnToWebListener onToWebListener;

    public NewsAdapter(Context context, List<News> news, OnToWebListener onToWebListener){
        mContext = context;
        mNewses = news;
        this.onToWebListener = onToWebListener;
    }

    @Override
    public BlogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.recycler_item_news, parent,false);
        return new BlogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BlogViewHolder holder, final int position) {
        final News news = mNewses.get(position);


        Glide.with(mContext)
                .load(TextUtils.isEmpty(news.image) ? R.drawable.ic_blog_author : news.image)
                .centerCrop()
                .placeholder(R.drawable.ic_blog_author)
                .crossFade()
                .error(R.drawable.ic_blog_author)
                .into(holder.image);

        holder.title.setText(news.title);
        holder.time.setText(news.time);
        holder.summary.setText(news.summary);
        holder.source.setText(news.source);
        holder.comment.setText(news.comment + "");
        holder.view.setText(news.view + "");

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onToWebListener != null){
                    onToWebListener.toWeb(v,position, news.link);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNewses != null ? mNewses.size() : 0;
    }

    class BlogViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title;
        TextView time;
        TextView summary;
        TextView source;
        TextView comment;
        TextView view;
        LinearLayout linearLayout;

        public BlogViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.news_image);
            title = (TextView) itemView.findViewById(R.id.news_title);
            time = (TextView) itemView.findViewById(R.id.news_time);
            summary = (TextView) itemView.findViewById(R.id.news_summary);
            source = (TextView) itemView.findViewById(R.id.news_source);
            comment = (TextView) itemView.findViewById(R.id.news_comment);
            view = (TextView) itemView.findViewById(R.id.news_view);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.linear_layout);
        }
    }
}