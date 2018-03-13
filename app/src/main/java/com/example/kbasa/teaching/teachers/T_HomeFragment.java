package com.example.kbasa.teaching.teachers;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.example.kbasa.teaching.CustomAdapter;
import com.example.kbasa.teaching.Notification;
import com.example.kbasa.teaching.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * A simple {@link Fragment} subclass.
 */
public class T_HomeFragment extends Fragment {

    private ArrayList<String> res_list;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;
    private CardListViewAdapter adapter;
    FirebaseAuth user;
    DatabaseReference db;



    public T_HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_t__home, container, false);

        user = FirebaseAuth.getInstance();
        Log.i("test",user.getUid());

        String f = FirebaseInstanceId.getInstance().getToken();
        Log.i("Token",f);
        new Notification().onTokenRefresh();



        db = FirebaseDatabase.getInstance().getReference("Teacher").child(user.getUid());
        db.updateChildren(new HashMap<String, Object>(){{put("tokenId",FirebaseInstanceId.getInstance().getToken());}});

        db = FirebaseDatabase.getInstance().getReference("Teacher").child(user.getUid()).child("courseOffered");
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final ArrayList<String> list = new ArrayList<>();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    list.add(dataSnapshot1.getKey());
                    Log.i("child coubt", dataSnapshot1.getKey());
                }

                DatabaseReference course = FirebaseDatabase.getInstance().getReference("Course");

                course.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Vector<Map<String,String>> vector = new Vector<>();
                        Log.i("please",String.valueOf(dataSnapshot.getChildrenCount()));
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                            String temp = dataSnapshot1.getKey();
                            if(list.contains(temp)){
                                Map<String,String> v = new HashMap<>();
                                v.put("courseId",temp);
                                for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                                    if(dataSnapshot2.getKey().equals("courseName")){
                                        v.put(dataSnapshot2.getKey(),dataSnapshot2.getValue(String.class));
                                    }
                                    else if(dataSnapshot2.getKey().equals("profileUri")){
                                        v.put(dataSnapshot2.getKey(),dataSnapshot2.getValue(String.class));
                                    }
                                }
                                vector.add(v);
                            }
                        }

                        ListView simpleGrid= view.findViewById(R.id.list_t_courses);
                        if(getActivity().getApplicationContext()!=null) {
                            CustomAdapter customAdapter = new CustomAdapter(getActivity().getApplicationContext(), vector);
                            simpleGrid.setAdapter(customAdapter);

                            simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    // set an Intent to Another Activity
                                    Intent intent = new Intent(getContext(), ViewCourseActivity.class);
                                    Map<String, String> temp = vector.get(position);
                                    intent.putExtra("courseId", temp.get("courseId"));
                                    startActivity(intent); // start Intent
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
