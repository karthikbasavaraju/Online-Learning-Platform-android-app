package com.example.kbasa.teaching;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by kbasa on 3/6/2018.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    Vector<Map<String,String>> animals;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, Vector<Map<String,String>> animals) {
        this.context = applicationContext;
        this.animals = animals;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return animals.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_gridview, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.icon);
//        Uri profileUri = Uri.parse((animals.get(i)).get("profileUri"));

        Uri profileUri = Uri.parse((animals.get(i)).get("profileUri"));
        imageView.setTag(profileUri);

        new DownloadImageTask().execute(imageView);
        TextView courseName = view.findViewById(R.id.courseNameTextView);
        courseName.setText((animals.get(i)).get("courseName"));
        return view;
    }
}
