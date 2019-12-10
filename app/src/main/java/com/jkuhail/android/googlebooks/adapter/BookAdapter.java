package com.jkuhail.android.googlebooks.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.jkuhail.android.googlebooks.R;
import com.jkuhail.android.googlebooks.app.AppController;
import com.jkuhail.android.googlebooks.model.Book;


import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {
    private Activity activity;
    public static ArrayList<Book> data;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    public BookAdapter(Activity activity , ArrayList<Book> data) {
        this.activity = activity;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View root = LayoutInflater.from(activity).inflate(R.layout.single_book , null , false);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail =  root
                .findViewById(R.id.thumbnail);
        final TextView bookTitle= root.findViewById(R.id.bookTitle);
        final TextView authorName= root.findViewById(R.id.authorName);
        final TextView publishedDate= root.findViewById(R.id.publishedDate);
        final TextView categories= root.findViewById(R.id.categories);

        bookTitle.setText(data.get(i).getBookTitle());
        authorName.setText(data.get(i).getAuthorName());
        publishedDate.setText(data.get(i).getPublishedDate());
        categories.setText(data.get(i).getCategories());
        thumbNail.setImageUrl(data.get(i).getImage(), imageLoader);
        return root;
    }
}
