package com.projs.udacity.lucas.booklistingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Button searchGoogleBooksButton = (Button) findViewById(R.id.btn_search);
        searchGoogleBooksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etSearchInputField = (EditText) findViewById(R.id.search_inputField);
                String strSearchInput = etSearchInputField.getText().toString().trim();
                Intent intent = new Intent(SearchActivity.this, BooksActivity.class);
                intent.putExtra("usrQueryString", strSearchInput);
                startActivity(intent);
            }
        });
    }
}
