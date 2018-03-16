package com.example.kbasa.teaching.teachers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.kbasa.teaching.DataTypes.Course;
import com.example.kbasa.teaching.DataTypes.MyDate;
import com.example.kbasa.teaching.FieldsOk;
import com.example.kbasa.teaching.LoginActivity;
import com.example.kbasa.teaching.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class T_EditCourseActivity extends AppCompatActivity {

    private Button btn_remove;
    private Button btn_save;
    String courseId="";
    Course course = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t__edit_course);

        btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_save = (Button) findViewById(R.id.btn_save);

        // check if the permission is granted


        enable_button();


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

                EditText courseName = findViewById(R.id.courseNameTextView);
                courseName.setText(course.getCourseName());

                EditText description = findViewById(R.id.descriptionTextView);
                description.setText(course.getCourseDetails());
                String tags="";
                int i;
                for(i=0;i<course.getTags().size()-1;i++){
                    tags = tags + course.getTags().get(i) +", ";
                }
                tags = tags + course.getTags().get(i);

                EditText tagsEditText = findViewById(R.id.tagTextView);
                tagsEditText.setText(tags);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button enrollButton = findViewById(R.id.btn_save);
        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean fieldsOK = FieldsOk.validate(new EditText[] { findViewById(R.id.courseNameTextView),findViewById(R.id.descriptionTextView), findViewById(R.id.tagTextView) });
                if(fieldsOK) {

                    String courseName = ((EditText) findViewById(R.id.courseNameTextView)).getText().toString();
                    String courseDetails = ((EditText) findViewById(R.id.descriptionTextView)).getText().toString();
                    String tag = ((EditText) findViewById(R.id.tagTextView)).getText().toString();
                    List<String> tags = new LinkedList<>();
                    StringTokenizer tag1 = new StringTokenizer(tag, ",");
                    while (tag1.hasMoreElements()) {
                        tags.add(tag1.nextToken());
                    }

                    course.setCourseName(courseName);
                    course.setCourseDetails(courseDetails);
                    course.setTags(tags);

                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Course");
                    Toast.makeText(T_EditCourseActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();
                    db.updateChildren(new HashMap<String, Object>() {{
                        put(courseId, course);
                    }});

                }
                else{
                    Toast.makeText(T_EditCourseActivity.this, "Fields cant be empty", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void enable_button() {
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Course");
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("kkkk",String.valueOf(dataSnapshot.child(courseId).child("schedules").getChildrenCount()));
                        int flag = 0;
                        for(DataSnapshot dataSnapshot1:dataSnapshot.child(courseId).child("schedules").getChildren()){
                            for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                                    MyDate myDate = dataSnapshot2.getValue(MyDate.class);
                                    int index = myDate.compare(0);
                                    if(index == 1){
                                        Toast.makeText(T_EditCourseActivity.this,"Students have enrolled. Cannot delete the course",Toast.LENGTH_SHORT).show();
                                        flag=1;
                                    }
                            }
                        }
                        if(flag==0){
                            Bundle bundle = new Bundle();
                            bundle.putString("courseId", courseId);
                            bundle.putString("professorId", course.getProfessorId());
                            DefaultFragment dialog = new DefaultFragment();
                            dialog.setArguments(bundle);
                            dialog.show(getSupportFragmentManager(),"my_dia");
                        }






                      /*  if(dataSnapshot.child(courseId).child("student").getChildrenCount()==0 && dataSnapshot.child(courseId).exists()){

                            Bundle bundle = new Bundle();
                            bundle.putString("courseId", courseId);
                            bundle.putString("professorId", course.getProfessorId());
                            DefaultFragment dialog = new DefaultFragment();
                            dialog.setArguments(bundle);
                            dialog.show(getSupportFragmentManager(),"my_dia");
                        }
                        else if(dataSnapshot.child(courseId).child("student").getChildrenCount()!=0 && dataSnapshot.child(courseId).exists()){

                        }*/

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }

}
