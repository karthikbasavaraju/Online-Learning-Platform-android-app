package com.example.kbasa.teaching;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


import com.example.kbasa.teaching.students.EnrollActivity;
import com.example.kbasa.teaching.teachers.ViewCourseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class StudentSearchActivity extends AppCompatActivity {

    ListView list;
    Vector<Map<String, String>> vector;
    Vector<String> tagVector;
    List<String> web ;
    SearchView mSearchView;

    public StudentSearchActivity(){


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_student_search);


        Bundle b = this.getIntent().getExtras();
        final String tagQuery = b.getString("tag");

        DatabaseReference course = FirebaseDatabase.getInstance().getReference("Course");

        course.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                vector = new Vector<>();
                tagVector = new Vector<>();
                Log.i("please", String.valueOf(dataSnapshot.getChildrenCount()));
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String temp = dataSnapshot1.getKey();
                    Map<String, String> v = new HashMap<>();
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


                final Vector<String> tempTag = new Vector();
                final Vector<Map<String,String>> tempDetails = new Vector<>();
                for(int i = 0 ; i < web.size();i++){
                    if((web.get(i).toLowerCase()).contains(tagQuery.toLowerCase()))
                    {
                        tempTag.add(web.get(i));
                        tempDetails.add(vector.get(i));
                    }
                }


                final SearchPageDisplay adapter = new
                        SearchPageDisplay(StudentSearchActivity.this, tempTag, tempDetails);
                list=(ListView)findViewById(R.id.wikiLinks);
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        Intent intent = new Intent(StudentSearchActivity.this,EnrollActivity.class);
                        intent.putExtra("courseId", (tempDetails.get(position)).get("courseId"));
                        startActivity(intent);
                    }
                });

                SearchView sv = findViewById(R.id.searchButton);


                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        final Vector<String> tempTag = new Vector();
                        final Vector<Map<String,String>> tempDetails = new Vector<>();
                        for(int i = 0 ; i < web.size();i++){
                            if((web.get(i).toLowerCase()).contains(query.toLowerCase()))
                            {
                                tempTag.add(web.get(i));
                                tempDetails.add(vector.get(i));
                            }
                        }


                        SearchPageDisplay adapter1 = new
                                SearchPageDisplay(StudentSearchActivity.this, tempTag, tempDetails);
                        list=(ListView)findViewById(R.id.wikiLinks);
                        list.setAdapter(adapter1);
                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    int position, long id) {

                                Intent intent = new Intent(StudentSearchActivity.this,EnrollActivity.class);
                                intent.putExtra("courseId", (tempDetails.get(position)).get("courseId"));
                                startActivity(intent);

                            }
                        });

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
