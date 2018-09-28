package com.example.kbasa.teaching.students;


import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.kbasa.teaching.DataTypes.Course;
import com.example.kbasa.teaching.DataTypes.MyDate;
import com.example.kbasa.teaching.DataTypes.Schedule;
import com.example.kbasa.teaching.DataTypes.ScheduleTracker;
import com.example.kbasa.teaching.R;
import com.example.kbasa.teaching.teachers.ViewCourseActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class EnrollActivity extends AppCompatActivity {

    String courseId = "";
    Course course = null;
    String schedule = null;
    MyDate myDate = null;
    String[] schedules;
    VideoView videoViewLandscape;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_enroll);

        final String sId = FirebaseAuth.getInstance().getUid();
        Bundle b = getIntent().getExtras();
        final Button enrollButton = findViewById(R.id.btn_enroll);


        if (b != null) {
            courseId = b.getString("courseId");
        }
        Log.i("test courseId", courseId);


        DatabaseReference courseDB = FirebaseDatabase.getInstance().getReference("Course").child(courseId);
        courseDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("test - editcount : ", String.valueOf(dataSnapshot.getChildrenCount()));
                course = dataSnapshot.getValue(Course.class);
                DatabaseReference profileDB = FirebaseDatabase.getInstance().getReference("Teacher").child(course.getProfessorId()).child("profileUri");
                profileDB.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Picasso.with(EnrollActivity.this).load(dataSnapshot.getValue(String.class)).into((ImageView) findViewById(R.id.img_teacher_profile));
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                uri = Uri.parse(course.getCourseUri());
                videoViewLandscape = findViewById(R.id.introVideoView);
                new StreamVideo1().execute();

                /*final ProgressDialog dialog = new ProgressDialog(EnrollActivity.this);
                dialog.setMessage("Loading");
                dialog.setIndeterminate(true);
                dialog.show();
                MediaController mediacontroller = new MediaController(
                        EnrollActivity.this);
                mediacontroller.setAnchorView(videoViewLandscape);
                videoViewLandscape.setVideoURI(uri);
                videoViewLandscape.requestFocus();

                videoViewLandscape.start();*/

                EditText courseName = findViewById(R.id.courseNameTextView);
                courseName.setText(course.getCourseName());

                EditText description = findViewById(R.id.descriptionTextView);
                description.setText(course.getCourseDetails());

                TextView aboutProfessor = findViewById(R.id.tv_about_teacher);
                aboutProfessor.setText(course.getAboutProfessor());

                String tags = "";
                int j = 0;
                for (; j < course.getTags().size() - 1; j++) {
                    tags += course.getTags().get(j);
                }
                tags += course.getTags().get(j);


                EditText tagsEditText = findViewById(R.id.tagEditView);
                tagsEditText.setText(tags);


                schedules = new String[(course.getMyDate()).size()];
                for (int i = 0; i < course.getMyDate().size(); i++) {
                    schedules[i] = course.getMyDate().get(i).toString();
                }


                for (int studentIndex = 0; studentIndex < course.getSchedules().size(); studentIndex++) {
                    if (((course.getSchedules().get(studentIndex))).containsKey(sId)) {
                        Log.i("EnrollActivity", "Already Enrolled");
                        enrollButton.setText("  Already Enrolled  ");
                        enrollButton.setEnabled(false);
                        break;
                    }
                }

                if (!course.isAvailable()) {
                    Log.i("EnrollActivity", "Course Deleted");
                    enrollButton.setText("  Course deleted  ");
                    enrollButton.setEnabled(false);

                }


                final Spinner scheduleSpinner = findViewById(R.id.scheduleSpinner);


                ArrayAdapter<String> adapter = new ArrayAdapter<String>(EnrollActivity.this,
                        android.R.layout.simple_spinner_item, schedules);

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

                DatabaseReference timings = FirebaseDatabase.getInstance().getReference("Student").child(FirebaseAuth.getInstance().getUid())
                        .child("courseTaken").child("schedules").child(courseId).child("myDate");
                timings.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            String s = "";
                            s += dataSnapshot.child("day").getValue(Long.class).toString();
                            s += "-";
                            s += dataSnapshot.child("month").getValue(Long.class).toString();
                            s += "-";
                            s += dataSnapshot.child("year").getValue(Long.class).toString();
                            s += " ";
                            s += dataSnapshot.child("hour").getValue(Long.class).toString();
                            s += ":";
                            s += dataSnapshot.child("minute").getValue(Long.class).toString();
                            Log.i("timings", s);
                            schedules = new String[1];
                            schedules[0] = s;
                            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(EnrollActivity.this,
                                    android.R.layout.simple_spinner_item, schedules);

                            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            scheduleSpinner.setAdapter(adapter1);


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


        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("Teacher").child(course.getProfessorId()).child("tokenId");

                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String tokenId = dataSnapshot.getValue(String.class);
                        if (schedule == null) {
                            schedule = course.getMyDate().get(0).toString();
                        }


                        DatabaseReference studentScheduleUpdate = FirebaseDatabase.getInstance().getReference("Student").child(FirebaseAuth.getInstance().getUid()).child("courseTaken").child("schedules");
                        final Schedule schedule1 = new Schedule(course.getProfessorId(), myDate);

                        studentScheduleUpdate.updateChildren(new HashMap<String, Object>() {{
                            put(courseId, schedule1);
                        }});

                        DatabaseReference teacherScheduleUpdate = FirebaseDatabase.getInstance().getReference("Teacher").child(course.getProfessorId()).child("courseTaken").child("schedules");
                        final Schedule schedule11 = new Schedule(sId, myDate);
                        teacherScheduleUpdate.updateChildren(new HashMap<String, Object>() {{
                            put(courseId, schedule11);
                        }});

                        DatabaseReference courseScheduleUpdate = FirebaseDatabase.getInstance().getReference("Course");

                        List temp = course.getSchedules();
                        temp.add(new HashMap() {{
                            put(sId, myDate);
                        }});
                        course.setSchedules(temp);
                        courseScheduleUpdate.updateChildren(new HashMap<String, Object>() {{
                            put(courseId, course);
                        }});

                        DatabaseReference courseSchedule = FirebaseDatabase.getInstance().getReference("Schedules");
                        final String key = courseSchedule.push().getKey();
                        final ScheduleTracker scheduleTracker = new ScheduleTracker();
                        scheduleTracker.setCourseId(courseId);
                        scheduleTracker.setStudentId(sId);
                        scheduleTracker.setProfessorId(course.getProfessorId());
                        scheduleTracker.setStudentTokenId(FirebaseInstanceId.getInstance().getToken());
                        scheduleTracker.setProfessorTokenId(course.getProfessorTokenId());
                        scheduleTracker.setCourseName(course.getCourseName());
                        scheduleTracker.setProfessorName(course.getName());
                        scheduleTracker.setStudentName(FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0]);
                        scheduleTracker.setDate(myDate.toString());
                        courseSchedule.updateChildren(new HashMap<String, Object>() {{
                            put(key, scheduleTracker);
                        }});


                        new RequestServerNotification(tokenId, FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0], course.getCourseName()).execute();
                        Intent intent = new Intent(EnrollActivity.this, EnrollActivity.class);
                        intent.putExtra("courseId", courseId);
                        Toast.makeText(EnrollActivity.this, "You have enrolled into " + course.getCourseName() + " course", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }

    private class StreamVideo1 extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(EnrollActivity.this);
            dialog.setMessage("Loading");
            dialog.setIndeterminate(true);
            dialog.show();
            MediaController mediacontroller = new MediaController(
                    EnrollActivity.this);
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
