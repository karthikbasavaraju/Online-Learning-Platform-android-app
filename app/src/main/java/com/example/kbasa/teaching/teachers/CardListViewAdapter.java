package com.example.kbasa.teaching.teachers;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.kbasa.teaching.DownloadImageTask;
import com.example.kbasa.teaching.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

/**
 * Created by derek on 2018/3/5.
 */

public class CardListViewAdapter extends RecyclerView.Adapter<CardListViewAdapter.ViewHolder> {

    private ArrayList<String> dataset;
    private final View.OnClickListener mOnClickListener;
    Context context;
    Vector<Map<String,String>> course;
    LayoutInflater inflter;



    public CardListViewAdapter(ArrayList<String> dataset,Context applicationContext, Vector<Map<String,String>> course) {
        this.context = applicationContext;
        this.course = course;
        inflter = (LayoutInflater.from(applicationContext));
        this.dataset = dataset;

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), T_EditCourseActivity.class);
                v.getContext().startActivity(intent);
            }
        };
    }

    @Override
    public CardListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_course, parent, false);
        v.setOnClickListener(mOnClickListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
try{
        URL url = new URL((course.get(position)).get("profileUri"));
        ImageView imageView = holder.imageView;
       // imageView.setImageURI(url);

    imageView.setImageDrawable(Drawable.createFromStream((InputStream)new
            URL((course.get(position)).get("profileUri")).getContent(), "src"));

    } catch (Exception e)
    {


    }


        //        holder.imageView.setTag(profileUri);

//        holder.imageView.setImageResource(R.drawable.dialogs_regions);
        Log.i("Home-profileUri",(course.get(position)).get("profileUri"));
  //      holder.imageView.setImageURI(profileUri);
  //      new DownloadImageTask().execute(imageView);


     //   holder.mTitle.setText((course.get(position)).get("courseName"));
    }

    @Override
    public int getItemCount() {
        return course.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
     //       mTitle = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.icon);
        }
    }
}
