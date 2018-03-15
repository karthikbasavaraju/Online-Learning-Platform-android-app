package com.example.kbasa.teaching;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

/**
 * Created by derek on 2018/3/6.
 */

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private Vector<Map<String,String>> list;

    public ListViewAdapter(Context context, Vector<Map<String,String>> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }



    public void setList(Vector<Map<String,String>> list) {
        this.list = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = View.inflate(context, R.layout.item, null);
        Map<String,String> details = list.get(position);

        TextView tvName = (TextView) v.findViewById(R.id.title);

        tvName.setText(details.get("courseName"));

        return v;
    }

    public void clearList() {
        list = new Vector<>();
    }
}
