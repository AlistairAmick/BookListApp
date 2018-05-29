package com.projs.udacity.lucas.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Books>> {
    private String mUrl;

    public BookLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Books> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        List<Books> books = QueryUtils.fetchBookData(mUrl);
        return books;
    }
}
