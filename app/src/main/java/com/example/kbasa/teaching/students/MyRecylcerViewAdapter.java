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
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by derek on 2018/3/5.
 */

public class MyRecylcerViewAdapter extends RecyclerView.Adapter<MyRecylcerViewAdapter.ViewHolder> {

    Vector<HashMap<String,String>> dataset;
    Context context;
    int card;

    public MyRecylcerViewAdapter(final Vector<HashMap<String,String>> dataset, Context context, int card) {
        this.dataset = dataset;
        this.context = context;
        this.card = card;


    }

    @Override
    public MyRecylcerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(card, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyRecylcerViewAdapter.ViewHolder holder, int position) {




        final HashMap<String, String> temp = dataset.get(position);
        holder.mTitle.setText(temp.get("courseName"));
        if (card == R.layout.category) {
            holder.imageView.setImageResource(Integer.parseInt(temp.get("resourceId")));
        } else {
            Uri uri = Uri.parse(temp.get("profileUri"));
            Picasso.with(context).load(uri).into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (card == R.layout.category) {

                } else {
                    Intent intent = new Intent(context,EnrollActivity.class);
                    intent.putExtra("courseId",temp.get("courseId"));
                    context.startActivity(intent);

                }
            }
        });
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
