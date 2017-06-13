package com.example.mrpeny.mrpenynewsapp;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>> {
    public static final String URL = "http://content.guardianapis.com/search"; //?order-by=relevance&show-fields=trailText%2Cheadline&q=hungary&api-key=test
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<NewsData>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderByValue = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        String pageSize = sharedPreferences.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));

        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter(getString(R.string.settings_order_by_key), orderByValue);
        uriBuilder.appendQueryParameter("q", "hungary");
        uriBuilder.appendQueryParameter(getString(R.string.settings_page_size_key), pageSize);
        uriBuilder.appendQueryParameter("api-key", "a6ba290e-d70e-44da-93bb-3a04f7dcb7e9");
        //Log.d("MainActivity", uriBuilder.toString());
        return new NewsLoader(this, uriBuilder.toString());
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
