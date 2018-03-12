package com.example.kbasa.teaching.students;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.VideoView;

import com.example.kbasa.teaching.DataTypes.Course;
import com.example.kbasa.teaching.DataTypes.MyDate;
import com.example.kbasa.teaching.DataTypes.Schedule;
import com.example.kbasa.teaching.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EnrollActivity extends AppCompatActivity {

    String courseId="";
    Course course = null;
    String schedule=null;
    MyDate myDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);


        Bundle b = getIntent().getExtras();

        if(b!=null){
            courseId = b.getString("courseId");
        }
        Log.i("test courseId",courseId);

        DatabaseReference courseDB = FirebaseDatabase.getInstance().getReference("Course").child(courseId);
        courseDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("test - editcount : ",String.valueOf(dataSnapshot.getChildrenCount()));
                course = dataSnapshot.getValue(Course.class);


                Uri uri = Uri.parse(course.getCourseUri());
                VideoView videoViewLandscape = findViewById(R.id.introVideoView);
                videoViewLandscape.setVideoURI(uri);
                videoViewLandscape.requestFocus();
                videoViewLandscape.start();

                EditText courseName = findViewById(R.id.courseNameTextView);
                courseName.setText(course.getCourseName());

                EditText description = findViewById(R.id.descriptionTextView);
                description.setText(course.getCourseDetails());

                final String[] schedules = new String[course.getMyDate().size()];
                for(int i=0;i<course.getMyDate().size();i++){
                    schedules[i] = course.getMyDate().get(i).toString();

                }




                Spinner scheduleSpinner = findViewById(R.id.scheduleSpinner);



                ArrayAdapter<String>adapter = new ArrayAdapter<String>(EnrollActivity.this,
                        android.R.layout.simple_spinner_item,schedules);

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                scheduleSpinner.setAdapter(adapter);
                scheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        schedule = schedules[i];
                        myDate = course.getMyDate().get(i);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button enrollButton = findViewById(R.id.btn_enroll);
        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Teacher").child(course.getProfessorId()).child("tokenId");

                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String tokenId = dataSnapshot.getValue(String.class);
                        if(schedule==null){
                            schedule = course.getMyDate().get(0).toString();
                        }
                        DatabaseReference db1 = FirebaseDatabase.getInstance().getReference("Student").child("courseTaken").child("schedules");
                        final String key = db1.push().getKey();
                        final Schedule schedule1 = new Schedule(courseId,course.getProfessorId(),myDate);
                        db1.updateChildren(new HashMap<String, Object>(){{put(key,schedule1);}});
                        new RequestServerNotification(tokenId).execute();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//
//                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Teacher").child(courseId);
//
//                db.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Intent intent = new Intent(ViewCourseActivity.this, T_EditCourseActivity.class);
//                        intent.putExtra("courseId", courseId);
//                        startActivity(intent);
//                        finish();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//



            }
        });


    }
}
