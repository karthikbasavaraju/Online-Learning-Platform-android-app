package com.example.kbasa.teaching;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.kbasa.teaching.DataTypes.Course;
import com.example.kbasa.teaching.DataTypes.PersonalDetails;
import com.example.kbasa.teaching.DataTypes.Teacher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AddCourseActivity extends AppCompatActivity {


    Button btnUpload; // to upload the video
    MultiAutoCompleteTextView aboutCourse;


    String path = System.getenv("EXTERNAL_STORAGE");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);


        btnUpload = findViewById(R.id.uploadVideo);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        final StorageReference mountainsRef = storageRef.child("video/mountains.mp4");
        final ProgressDialog progressBar = new ProgressDialog(AddCourseActivity.this);

        aboutCourse = findViewById(R.id.detailsMultiTextView);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputStream stream=null;
                try {
                    stream = new FileInputStream(new File(path + "/mountain.mp4"));
                }
                catch (Exception e){
                    Log.i("louda upload","path : "+path);
                }

                UploadTask uploadTask = mountainsRef.putStream(stream);
                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.dismiss();
                        Toast.makeText(AddCourseActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                        progressBar.dismiss();
                        Toast.makeText(AddCourseActivity.this,"success",Toast.LENGTH_SHORT).show();
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.i("louda upload",downloadUrl.toString());

                        final Course course = new Course();

                        FirebaseAuth user = FirebaseAuth.getInstance();





                        DatabaseReference teacherDB = FirebaseDatabase.getInstance().getReference("Teacher").child(user.getUid()).child("courseOffered");

                        final String courseId = teacherDB.push().getKey();
                        teacherDB.child(courseId).setValue("courseID");

                        String aboutProfessor="MAD and OS professor and entrepreneur";
                        String courseName="MAD";
                        String courseDetails="Its about android";
                        boolean available=true;
                        String courseUri= downloadUrl.toString();
                        String profileUri = downloadUrl.toString();
                        List<String> tags = new LinkedList<>();
                        tags.add("Mobile");
                        tags.add("JAVA");
                        tags.add("APP");

                        List<HashMap<Integer,String>> student;   //To store student id and their status(enrolled or complete)


                        course.setAboutProfessor(aboutProfessor);
                        course.setCourseName(courseName);
                        course.setCourseDetails(courseDetails);
                        course.setAvailable(available);
                        course.setCourseUri(courseUri);
                        course.setProfileUri(profileUri);
                        course.setTags(tags);


                        final DatabaseReference courseDB = FirebaseDatabase.getInstance().getReference("Course");


                        Log.i("louda child count" , "start: "+course.getTaughtBy());
                        HashMap hm = new HashMap();
                        hm.put(courseId,course);
                        courseDB.updateChildren(hm);
                        Log.i("louda child count" , "finished");

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Teacher").child(user.getUid()).child("personalDetails");

                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                    if(dataSnapshot1.getKey().equals("name")){
                                        Log.i("louda child count" , dataSnapshot1.getValue(String.class));
                                        final String s = dataSnapshot1.getValue(String.class);
                                        courseDB.child(courseId).updateChildren(new HashMap(){{put("name",s);}});
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Intent intent = new Intent(AddCourseActivity.this,EditCourseActivity.class);
                        intent.putExtra("courseId",courseId);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }
}
