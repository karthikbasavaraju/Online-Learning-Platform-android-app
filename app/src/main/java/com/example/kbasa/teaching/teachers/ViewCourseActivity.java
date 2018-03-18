package com.example.kbasa.teaching.teachers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.example.kbasa.teaching.DataTypes.Course;
import com.example.kbasa.teaching.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ViewCourseActivity extends AppCompatActivity {

    String courseId="";
    Course course = null;
    Button enrollButton;
    VideoView videoViewLandscape;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_course);


        Bundle b = getIntent().getExtras();
        enrollButton = findViewById(R.id.btn_edit);
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

                uri = Uri.parse(course.getCourseUri());
                videoViewLandscape = findViewById(R.id.introVideoView);
                new StreamVideo().execute();

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

                EditText tagsEditText = findViewById(R.id.tagEditView);
                tagsEditText.setText(tags);
                if(!course.isAvailable()){
                    Log.i("EnrollActivity","Course Deleted");
                    enrollButton.setText("  Course deleted  ");
                    enrollButton.setEnabled(false);

                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewCourseActivity.this, T_EditCourseActivity.class);
                intent.putExtra("courseId", courseId);
                startActivity(intent);
                finish();
            }
        });


    }

    private class StreamVideo extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(ViewCourseActivity.this);
            dialog.setMessage("Loading");
            dialog.setIndeterminate(true);
            dialog.show();
            MediaController mediacontroller = new MediaController(
                    ViewCourseActivity.this);
            mediacontroller.setAnchorView(findViewById(R.id.introVideoView));

            // Get the URL from String VideoURL
            videoViewLandscape.setMediaController(mediacontroller);
            videoViewLandscape.setVideoURI(uri);

            videoViewLandscape.requestFocus();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Start the MediaController


                videoViewLandscape.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    // Close the progress bar and play the video
                    public void onPrepared(MediaPlayer mp) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                      //          dialog.dismiss();
                                videoViewLandscape.start();

                            }
                        });
                    }
                });
            } catch (Exception e) {
             //   dialog.dismiss();
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            dialog.dismiss();


        }

    }

}
