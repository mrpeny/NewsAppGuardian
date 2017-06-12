package com.example.mrpeny.mrpenynewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

import static com.example.mrpeny.mrpenynewsapp.QueryUtils.parseNews;

/**
 * Created by MrPeny on 2017. 06. 12..
 */

public class NewsLoader extends AsyncTaskLoader<List<NewsData>> {
    private String query;

    public NewsLoader(Context context, String query) {
        super(context);
        this.query = query;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsData> loadInBackground() {
        String response = QueryUtils.fetchNewsData(query);
        List<NewsData> newsDataList = QueryUtils.parseNews(response);
        return newsDataList;
    }
}
