package com.example.kbasa.teaching.students;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.kbasa.teaching.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<String> dataset;

    ListView list;
    Vector<HashMap<String, String>> vector;
    Vector<String> tagVector;
    List<String> web ;
    Vector<HashMap<String, String>> courseCategories;
    SearchView mSearchView;


    public HomeFragment() {
        // Required empty public constructor
        courseCategories = new Vector();
        courseCategories.add(new HashMap<String,String>(){{put("courseName","Academics");put("resourceId",String.valueOf(R.drawable.academics));}});
        courseCategories.add(new HashMap<String,String>(){{put("courseName","Music");put("resourceId",String.valueOf(R.drawable.music));}});
        courseCategories.add(new HashMap<String,String>(){{put("courseName","Marketing");put("resourceId",String.valueOf(R.drawable.marketing));}});
        courseCategories.add(new HashMap<String,String>(){{put("courseName","IT & Software");put("resourceId",String.valueOf(R.drawable.it));}});
        courseCategories.add(new HashMap<String,String>(){{put("courseName","Health and Fitness");put("resourceId",String.valueOf(R.drawable.health));}});
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        final String sId = FirebaseAuth.getInstance().getUid();

        DatabaseReference course = FirebaseDatabase.getInstance().getReference("Course");

        course.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vector = new Vector();
                tagVector = new Vector<>();
                Log.i("please", String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String temp = dataSnapshot1.getKey();
                    HashMap<String, String> v = new HashMap<>();
                    v.put("courseId", temp);
                    String professorName="";
                    String courseName = "";
                    for (DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()) {
                        if (dataSnapshot2.getKey().equals("courseName")) {
                            v.put(dataSnapshot2.getKey(), dataSnapshot2.getValue(String.class));
                            courseName = dataSnapshot2.getValue(String.class);
                        }
                        else if (dataSnapshot2.getKey().equals("profileUri")) {
                            v.put(dataSnapshot2.getKey(), dataSnapshot2.getValue(String.class));
                        }
                        else if (dataSnapshot2.getKey().equals("name")) {
                            v.put(dataSnapshot2.getKey(), dataSnapshot2.getValue(String.class));
                            professorName =dataSnapshot2.getValue(String.class);
                        }
                        else if (dataSnapshot2.getKey().equals("tags")) {
                            ArrayList tempTag = (ArrayList)dataSnapshot2.getValue();
                            String tempString="";
                            for(Object tags : tempTag){
                                tempString += tags.toString() + " ";
                            }
                            tempString = courseName+" "+professorName +" "+ tempString;
                            tagVector.add(tempString);
                        }
                    }
                    vector.add(v);
                }
                web = new LinkedList();
                for(int i=0;i<tagVector.size();i++){
                    web.add(tagVector.elementAt(i));
                }

                recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_recommended);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new MyRecylcerViewAdapter(vector,getContext(),R.layout.row);
                recyclerView.setAdapter(adapter);



                DatabaseReference student = FirebaseDatabase.getInstance().getReference("Student");

                student.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot courses = dataSnapshot.child(sId).child("courseTaken").child("schedules");
                        Vector<HashMap<String,String>> myCourseVector = new Vector<>();
                        for(DataSnapshot dataSnapshot1 : courses.getChildren()){
                            String key = dataSnapshot1.getKey();
                            for(HashMap<String ,String> temp : vector){
                                if(temp.get("courseId").equals(key)){
                                    myCourseVector.add(temp);
                                }
                            }
                        }

                        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_ongoing);
                        recyclerView.setHasFixedSize(true);
                        layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
                        recyclerView.setLayoutManager(layoutManager);
                        adapter = new MyRecylcerViewAdapter(myCourseVector,getContext(),R.layout.row);
                        recyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



                recyclerView = view.findViewById(R.id.recycler_view_category);
                recyclerView.setHasFixedSize(true);
                layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false);
                recyclerView.setLayoutManager(layoutManager);
                adapter = new MyRecylcerViewAdapter(courseCategories,getContext(),R.layout.category);
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}
