package com.example.kbasa.teaching;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.kbasa.teaching.DataTypes.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class EditCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_course);

        Bundle b = getIntent().getExtras();
        String courseId="";
        if(b!=null){
            courseId = b.getString("courseId");
        }
        Log.i("louda courseId",courseId);

        DatabaseReference courseDB = FirebaseDatabase.getInstance().getReference("Course").child(courseId);
        courseDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("louda - editcount : ",String.valueOf(dataSnapshot.getChildrenCount()));
                String details="";
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.i("louda - "+dataSnapshot1.getKey(),dataSnapshot1.getValue(Object.class).toString());
                    details = details + dataSnapshot1.getKey()+" : "+dataSnapshot1.getValue(Object.class).toString()+"\n";
                }
                Course course = dataSnapshot.getValue(Course.class);
                Uri uri = Uri.parse(course.getCourseUri());
                VideoView videoViewLandscape = findViewById(R.id.introVideoView);
                videoViewLandscape.setVideoURI(uri);
                videoViewLandscape.requestFocus();
                videoViewLandscape.start();
                TextView detailsMultiView = findViewById(R.id.detailsMultiTextView);
                detailsMultiView.setText(details);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
