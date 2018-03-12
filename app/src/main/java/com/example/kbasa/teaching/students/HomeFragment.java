package com.example.kbasa.teaching.students;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.kbasa.teaching.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<String> dataset;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dataset = new ArrayList<>();
        for(int i=0; i<30; i++) {
            dataset.add("New Title # " + i);
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_recommended);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecylcerViewAdapter(dataset);
        recyclerView.setAdapter(adapter);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_ongoing);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecylcerViewAdapter(dataset);
        recyclerView.setAdapter(adapter);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_category);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecylcerViewAdapter(dataset);
        recyclerView.setAdapter(adapter);


        return view;
    }

}
