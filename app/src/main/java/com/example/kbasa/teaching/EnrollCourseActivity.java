package com.example.kbasa.teaching;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.kbasa.teaching.DataTypes.Course;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EnrollCourseActivity extends AppCompatActivity {
    String courseId="";
    Course course = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /*    setContentView(R.layout.activity_edit_course);

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
                String details="";
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Log.i("test - "+dataSnapshot1.getKey(),dataSnapshot1.getValue(Object.class).toString());
                    details = details + dataSnapshot1.getKey()+" : "+dataSnapshot1.getValue(Object.class).toString()+"\n";
                }
                course = dataSnapshot.getValue(Course.class);
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

        Button enrollButton = findViewById(R.id.enrollButton);
        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Teacher").child(course.getProfessorId()).child("tokenId");

                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String tokenId = dataSnapshot.getValue(String.class);
                        new RequestServerNotification(tokenId).execute();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




            }
        });

*/
    }

}
