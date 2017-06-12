package com.example.mrpeny.mrpenynewsapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by MrPeny on 2017. 06. 12..
 */

public class NewsDataAdapter extends RecyclerView.Adapter<NewsDataAdapter.ViewHolder> {
    List<NewsData> newsDataList;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView sectionNameTextView;
        TextView webPublicationDateTextView;
        TextView webTitleTextView;
        TextView authorsTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.sectionNameTextView = (TextView) itemView.findViewById(R.id.section_name_text_view);
            this.webPublicationDateTextView = (TextView) itemView.findViewById(R.id.publication_date_text_view);
            this.webTitleTextView = (TextView) itemView.findViewById(R.id.title_text_view);
            this.authorsTextView = (TextView) itemView.findViewById(R.id.authors_text_view);
        }
    }

    public NewsDataAdapter(Context context, List<NewsData> newsDataList) {
        this.context = context;
        this.newsDataList = newsDataList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View newsListItemView =
                LayoutInflater.from(context).inflate(R.layout.news_list_item, parent, false);

        NewsDataAdapter.ViewHolder newsDateViewHolder =
                new NewsDataAdapter.ViewHolder(newsListItemView);

        return newsDateViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (newsDataList == null) {
            return;
        }
        NewsData newsData = newsDataList.get(position);

        viewHolder.sectionNameTextView.setText(newsData.getSectionName());
        viewHolder.webTitleTextView.setText(newsData.getWebTitle());
        viewHolder.webPublicationDateTextView.setText(newsData.getWebPublicationDate());
    }

    @Override
    public int getItemCount() {
        return newsDataList.size();
    }

    public void setNewsDataList(List<NewsData> newsDataList) {
        this.newsDataList = newsDataList;
    }
}
