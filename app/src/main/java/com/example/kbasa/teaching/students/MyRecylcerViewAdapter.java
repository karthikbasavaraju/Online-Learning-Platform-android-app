package com.example.kbasa.teaching.students;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.kbasa.teaching.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by derek on 2018/3/5.
 */

public class MyRecylcerViewAdapter extends RecyclerView.Adapter<MyRecylcerViewAdapter.ViewHolder> {

    Vector<HashMap<String,String>> dataset;
    private final View.OnClickListener mOnClickListener;
    Context context;
    int card;

    public MyRecylcerViewAdapter(Vector<HashMap<String,String>> dataset, Context context,int card) {
        this.dataset = dataset;
        this.context = context;
        this.card = card;

        mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), CourseIntroductionActivity.class);
                v.getContext().startActivity(intent);
            }
        };
    }

    @Override
    public MyRecylcerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(card, parent, false);
        v.setOnClickListener(mOnClickListener);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyRecylcerViewAdapter.ViewHolder holder, int position) {


        HashMap<String, String> temp = dataset.get(position);
        holder.mTitle.setText(temp.get("courseName"));
        if (card == R.layout.category) {
            holder.imageView.setImageResource(Integer.parseInt(temp.get("resourceId")));
        } else {
            Uri uri = Uri.parse(temp.get("profileUri"));
            Picasso.with(context).load(uri).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.title);
            imageView = itemView.findViewById(R.id.icon);
        }
    }
}
