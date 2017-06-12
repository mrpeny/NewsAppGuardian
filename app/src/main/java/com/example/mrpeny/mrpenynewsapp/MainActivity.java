package com.example.mrpeny.mrpenynewsapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>> {
    public static final String URL = "http://content.guardianapis.com/search?order-by=relevance&show-fields=trailText%2Cheadline&q=hungary&api-key=test";
    private static final int NEWSLOADER_ID = 0;

    private List<NewsData> newsDataList = new ArrayList<>();
    private RecyclerView newsRecyclerView;
    private NewsDataAdapter newsDataAdapter;
    private TextView emptyStateTextView;
    private View progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setting up RecyclerView and its LayoutManager and Adapter
        newsRecyclerView = (RecyclerView) findViewById(R.id.news_recycler_view);
        LinearLayoutManager newsLayoutManager = new LinearLayoutManager(this);
        newsRecyclerView.setLayoutManager(newsLayoutManager);
        newsDataAdapter = new NewsDataAdapter(this, newsDataList);
        newsRecyclerView.setAdapter(newsDataAdapter);
        newsRecyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        emptyStateTextView = (TextView) findViewById(R.id.empty_text_view);
        progressBar = findViewById(R.id.progress_bar);

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Checking Internet connection and handling cases
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            LoaderManager loaderManager = getLoaderManager();
            // Starting Loader thread for network and parsing
            loaderManager.initLoader(NEWSLOADER_ID, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.internet_connection_error);
        }

    }

    @Override
    public Loader<List<NewsData>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List<NewsData> updatedNewsList) {
        progressBar.setVisibility(View.GONE);
        newsDataAdapter.setNewsDataList(null);

        if (updatedNewsList != null && !updatedNewsList.isEmpty()) {
            // If there is new data then update the list
            newsDataAdapter.setNewsDataList(updatedNewsList);
            newsDataAdapter.notifyDataSetChanged();
        } else {
            emptyStateTextView.setText(getString(R.string.no_news_error));
        }

    }

    @Override
    public void onLoaderReset(Loader<List<NewsData>> loader) {
        newsDataAdapter.setNewsDataList(null);
    }
}
