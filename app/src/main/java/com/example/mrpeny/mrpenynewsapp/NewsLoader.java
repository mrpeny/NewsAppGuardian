package com.example.mrpeny.mrpenynewsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import static com.example.mrpeny.mrpenynewsapp.QueryUtils.fetchNewsData;

/**
 * Created by MrPeny on 2017. 06. 12..
 */

public class NewsLoader extends AsyncTaskLoader<String> {
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
    public String loadInBackground() {
        String response = QueryUtils.fetchNewsData(query);
        return response;
    }
}
