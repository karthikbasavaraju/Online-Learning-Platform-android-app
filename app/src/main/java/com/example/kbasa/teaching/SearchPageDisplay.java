package com.example.kbasa.teaching;
import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class SearchPageDisplay extends ArrayAdapter<String> implements Filterable{

    private final Activity context;
    private List<String> web;
    private Vector<Map<String,String>> vector;
    public SearchPageDisplay(Activity context,
                      List<String> web1, Vector<Map<String,String>> vector1) {
        super(context, R.layout.activity_textview, web1);

            this.web = web1;
            this.vector = vector1;
            this.context = context;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.activity_textview, null, true);

        Map<String,String> courseDetails = vector.elementAt(position);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
        txtTitle.setText(courseDetails.get("courseName"));


        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);

        Uri profileUri = Uri.parse(courseDetails.get("profileUri"));
        imageView.setTag(profileUri);
        new DownloadImageTask().execute(imageView);

        return rowView;
    }
}