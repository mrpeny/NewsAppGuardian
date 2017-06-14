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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsData>>, SwipeRefreshLayout.OnRefreshListener {
    // base search URL of Guardian API
    public static final String URL = "http://content.guardianapis.com/search";
    private static final int NEWS_LOADER_ID = 0;
    // my chosen topic stated in the project rubric
    private static String myTopic = "Hungary";
    private List<NewsData> newsDataList = new ArrayList<>();
    private RecyclerView newsRecyclerView;
    private NewsDataAdapter newsDataAdapter;
    private TextView emptyStateTextView;
    private View progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Checking the presence of the Internet connection and handling cases
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            LoaderManager loaderManager = getLoaderManager();
            // Starting Loader thread for network and parsing
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
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

        // if Settings action was clicked then start SettingsActivity
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

        // extracting user preferences and store them in local variables
        String orderByValue = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        String pageSize = sharedPreferences.getString(
                getString(R.string.settings_page_size_key),
                getString(R.string.settings_page_size_default));

        // Build the base Guardian search API Uri
        Uri baseUri = Uri.parse(URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // appending order-by preference to the API query
        uriBuilder.appendQueryParameter(getString(R.string.settings_order_by_key), orderByValue);

        // appending the page-size preference to the API query
        uriBuilder.appendQueryParameter(getString(R.string.settings_page_size_key), pageSize);

        // encode the search topic to ensure spaces and special chares don't cause problems
        // in the query
        myTopic = Uri.encode(myTopic);
        uriBuilder.appendQueryParameter("q", myTopic);

        uriBuilder.appendQueryParameter("api-key", "a6ba290e-d70e-44da-93bb-3a04f7dcb7e9");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsData>> loader, List<NewsData> updatedNewsList) {
        progressBar.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        // releasing references to old data list
        newsDataAdapter.setNewsDataList(null);

        // If there is new data then update the list and notify adaptar about data change
        if (updatedNewsList != null && !updatedNewsList.isEmpty()) {
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

    // This method is called back when user wants to refresh list via swiping the RecyclerView down
    // when reached the top most list item
    @Override
    public void onRefresh() {
        restartLoader();
    }

    // logic for restarting the loader
    private void restartLoader() {
        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
    }
}
