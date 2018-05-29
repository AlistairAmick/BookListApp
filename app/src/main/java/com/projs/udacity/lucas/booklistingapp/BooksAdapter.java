package com.projs.udacity.lucas.booklistingapp;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class BooksAdapter extends ArrayAdapter<Books> {
    public BooksAdapter(Context context, List<Books> books) {
        super(context, 0, books);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_books, parent, false);
        }
        Books currentBook = getItem(position);
        TextView titleTextView = (TextView) listItemView.findViewById(R.id.title);
        TextView authorsTextView = (TextView) listItemView.findViewById(R.id.authors);
        TextView dateTextView = (TextView) listItemView.findViewById(R.id.date);
        titleTextView.setText(currentBook.getTitle());
        authorsTextView.setText(formatAuthors(currentBook.getAuthors()));
        dateTextView.setText((currentBook.getDate()));
        return listItemView;
    }

    private String formatAuthors(String[] authors) {
        String authorsString = TextUtils.join(", ", authors);
        return authorsString;
    }
}
