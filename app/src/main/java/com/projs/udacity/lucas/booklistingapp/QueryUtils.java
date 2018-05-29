package com.projs.udacity.lucas.booklistingapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class QueryUtils {
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<Books> fetchBookData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making HTTP request ", e);
        }
        List<Books> books = extractFeaturesFromJSON(jsonResponse);
        return books;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving Google Books JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<Books> extractFeaturesFromJSON(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<Books> booksArrayList = new ArrayList<>();
        try {
            JSONObject baseJSONResponse = new JSONObject(jsonResponse);
            JSONArray itemArray = baseJSONResponse.getJSONArray("items");
            for (int i = 0; i < itemArray.length(); i++) {
                String title;
                String date;
                String url;
                JSONObject currentBookJsonObj = itemArray.getJSONObject(i);
                JSONObject volumeInfoJsonObj = currentBookJsonObj.getJSONObject("volumeInfo");
                title = volumeInfoJsonObj.getString("title");
                date = volumeInfoJsonObj.getString("publishedDate");
                url = volumeInfoJsonObj.getString("infoLink");
                JSONArray authorsArray = volumeInfoJsonObj.getJSONArray("authors");
                String[] authors = new String[authorsArray.length()];
                for (int i1 = 0; i1 < authorsArray.length(); i1++) {
                    authors[i1] = authorsArray.getString(i1);
                }
                Books bookObj = new Books(title, authors, date, url);
                booksArrayList.add(bookObj);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing book JSON results ", e);
        }
        return booksArrayList;
    }

    private static URL createUrl(String urlString) {
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem with building the URL ", e);
        }
        return url;
    }
}
