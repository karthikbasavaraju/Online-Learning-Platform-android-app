package com.example.kbasa.teaching;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.kbasa.teaching.DataTypes.PersonalDetails;
import com.example.kbasa.teaching.DataTypes.Teacher;
import com.example.kbasa.teaching.DataTypes.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TeacherHomeActivity extends AppCompatActivity {
    GridView simpleGrid;
    FirebaseAuth user;
    DatabaseReference db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
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

                        //Setting up custom Grid View with Course details
                        simpleGrid = (GridView) findViewById(R.id.gridview);
                        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), vector);
                        simpleGrid.setAdapter(customAdapter);

                        simpleGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // set an Intent to Another Activity
                                Intent intent = new Intent(TeacherHomeActivity.this, EditCourseActivity.class);
                                Map<String,String> temp = vector.get(position);
                                intent.putExtra("courseId",temp.get("courseId") );
                                startActivity(intent); // start Intent
                            }
                        });

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


        Button addCourseButton = findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherHomeActivity.this, AddCourseActivity.class);
                startActivity(intent);
            }
        });
    }
}
