package com.projs.udacity.lucas.booklistingapp;

public class Books {
    private String mTitle;
    private String[] mAuthors;
    private String mUrl;
    private String mDate;

    public Books(String title, String[] authors, String date, String url) {
        mTitle = title;
        mAuthors = authors;
        mDate = date;
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String[] getAuthors() {
        return mAuthors;
    }

    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }
}
