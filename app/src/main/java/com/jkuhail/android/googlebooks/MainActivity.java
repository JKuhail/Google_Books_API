package com.jkuhail.android.googlebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jkuhail.android.googlebooks.adapter.BookAdapter;
import com.jkuhail.android.googlebooks.app.AppController;
import com.jkuhail.android.googlebooks.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String BASE_API_URL = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String LOG_TAG = "jehad";
    private ArrayList<Book> data = new ArrayList<>();
    private BookAdapter bookAdapter;
    private ListView booksListView;
    private TextView empty;
    private EditText search;
    private String author;
    private String category;
    private String publishedDate;
    private String description;
    private int pageCount;
    private long delay = 1000; // 1 seconds after user stops typing
    private long last_text_edit = 0;
    private Handler handler = new Handler();
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.search);
        empty = findViewById(R.id.empty);
        booksListView = findViewById(R.id.list);
        booksListView.setEmptyView(empty);
        bookAdapter = new BookAdapter(MainActivity.this, data);



        search.addTextChangedListener(new TextWatcher() {
                                          @Override
                                          public void beforeTextChanged (CharSequence s,int start, int count,
                                                                         int after){
                                          }
                                          @Override
                                          public void onTextChanged ( final CharSequence s, int start, int before,
                                                                      int count){
                                              //You need to remove this to run only once
                                              handler.removeCallbacks(input_finish_checker);
                                          }
                                          @Override
                                          public void afterTextChanged ( final Editable s){
                                              //avoid triggering event when text is empty
                                              if (s.length() > 0) {
                                                  last_text_edit = System.currentTimeMillis();
                                                  handler.postDelayed(input_finish_checker, delay);
                                                  booksListView.setAdapter(null);
                                                  empty.setAlpha(0);
                                              }
                                          }
                                      }

        );
    }


    private Runnable input_finish_checker = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (last_text_edit + delay - 500)) { //don't change this
                booksListView.setAdapter(null);
                data.clear();
                empty.setAlpha(1);
                empty.setText("Data is loading...");
                search(search.getText().toString());
            }
        }
    };

    private void search( String bookTitle){
        booksListView.setAdapter(bookAdapter);

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, BASE_API_URL + bookTitle + "&maxResults=40", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(LOG_TAG, response.toString());
                        try {
                            JSONArray booksArray = response.getJSONArray("items");

                            for (int i = 0; i < booksArray.length(); i++) {

                                JSONObject book_element = booksArray.getJSONObject(i);
                                JSONObject volumeInfo = book_element.getJSONObject("volumeInfo");
                                String title = volumeInfo.getString("title");

                                try{
                                    JSONArray authors = volumeInfo.getJSONArray("authors");
                                    author = authors.getString(0);
                                } catch (JSONException e){
                                    author = "No authors";
                                }
                                try{
                                    publishedDate = volumeInfo.getString("publishedDate");
                                } catch (JSONException e){
                                    publishedDate = "no published date";
                                }
                                try{
                                    description = volumeInfo.getString("description");
                                } catch (JSONException e){
                                    description = "no description";
                                }
                                try{
                                    pageCount = volumeInfo.getInt("pageCount");
                                } catch (JSONException e){
                                    pageCount = 0;
                                }



                                // I did this because some books don't have categories.
                                try{
                                    JSONArray categories = volumeInfo.getJSONArray("categories");
                                    category = categories.getString(0);
                                } catch (JSONException e){
                                    category = "No category";
                                }

                                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                                String imageLink = imageLinks.getString("thumbnail");
                                String url = volumeInfo.getString("canonicalVolumeLink");

                                book = new Book(title , author , publishedDate , description , pageCount , category , imageLink , url);
                                data.add(book);
                            }

                        } catch (JSONException e) {
                            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
                        }
                        bookAdapter.notifyDataSetChanged();
                        empty.setText("No results!");
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                booksListView.setAdapter(null);
                empty.setText("Nothing to show!");
                if(search.getText().toString().equals(" ") || search.getText().toString().equals("  "))
                    search.setError("This field is empty!");
            }
        });
        AppController.getInstance().addToRequestQueue(objectRequest);

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Uri bookUri = Uri.parse(BookAdapter.data.get(i).getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });
    }
}