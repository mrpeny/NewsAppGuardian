package com.example.mrpeny.mrpenynewsapp;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    public static final String URL = "http://content.guardianapis.com/search?order-by=relevance&show-fields=trailText%2Cheadline&q=hungary&api-key=test";

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.text_view);

        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        // Checking Internet connection and handling cases
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            LoaderManager loaderManager = getLoaderManager();
            // Starting Loader thread for network and parsing
            loaderManager.initLoader(0, null, this);
        } else {
            Toast.makeText(this, R.string.internet_connection_error, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, URL);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        textView.setText(data);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
