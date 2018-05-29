package com.projs.udacity.lucas.booklistingapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class BooksActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Books>> {
    private static final String GOOGLEBOOKS_VOLUMES_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?";
    private static final int BOOKS_LOADER_ID = 1;
    private String usrQueryString;
    private BooksAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);
        Intent intent = getIntent();
        usrQueryString = intent.getExtras().getString("usrQueryString");
        ListView booksListView = (ListView) findViewById(R.id.books_listview);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        booksListView.setEmptyView(mEmptyStateTextView);
        mAdapter = new BooksAdapter(this, new ArrayList<Books>());
        booksListView.setAdapter(mAdapter);
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Books currentBook = mAdapter.getItem(position);
                Uri booksUrl = Uri.parse(currentBook.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, booksUrl);
                startActivity(websiteIntent);
            }
        });
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(BOOKS_LOADER_ID, null, this);
        } else {
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(GONE);
            booksListView.setEmptyView(mEmptyStateTextView);
            mEmptyStateTextView.setText(getResources().getString(R.string.no_internet_connection));
        }
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int id, Bundle args) {
        String maxResults = "40";
        Uri baseUri = Uri.parse(GOOGLEBOOKS_VOLUMES_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("q", usrQueryString);
        uriBuilder.appendQueryParameter("maxResults", maxResults);
        return new BookLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> data) {
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setText(R.string.no_results_found);
        if (data != null && !data.isEmpty()) {
            mAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        mAdapter.clear();
    }
}
