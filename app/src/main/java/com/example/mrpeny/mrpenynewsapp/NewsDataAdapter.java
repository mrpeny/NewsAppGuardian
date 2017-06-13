package com.example.mrpeny.mrpenynewsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        final NewsDataAdapter.ViewHolder newsDataViewHolder =
                new NewsDataAdapter.ViewHolder(newsListItemView);

        // setting onClickListener on the list itmes that open the corresponding article online
        newsListItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String webUrl = newsDataList.get(newsDataViewHolder.getAdapterPosition()).getWebUrl();
                Uri webUri = Uri.parse(webUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, webUri);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                }
            }
        });

        return newsDataViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (newsDataList == null) {
            return;
        }
        NewsData newsData = newsDataList.get(position);

        viewHolder.sectionNameTextView.setText(newsData.getSectionName());
        viewHolder.webTitleTextView.setText(newsData.getWebTitle());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        Date date = null;
        try {
            date = simpleDateFormat.parse(newsData.getWebPublicationDate());
        } catch (ParseException e) {
            Log.e("NewsDataAdapter", e.getMessage());
        }

        //SimpleDateFormat dateFormatterForPresentation = new SimpleDateFormat("yyyy.mm.dd. HH:mm");
        simpleDateFormat.applyLocalizedPattern("yyyy.MM.dd. HH:mm");
        viewHolder.webPublicationDateTextView.setText(simpleDateFormat.format(date));
    }

    @Override
    public int getItemCount() {
        return newsDataList.size();
    }

    public void setNewsDataList(List<NewsData> newsDataList) {
        this.newsDataList = newsDataList;
    }
}
