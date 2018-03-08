package com.example.kbasa.teaching;

import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.database.Cursor;
import android.provider.MediaStore;
import android.view.View.OnClickListener;
import com.example.kbasa.teaching.DataTypes.Course;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
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
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class AddCourseActivity extends AppCompatActivity {


    String videoFilePath;
    String picFilePath;
    Button btnUpload;
    String aboutProfessor;
    String courseName;
    String courseDetails;
    boolean available;
    String courseUri;
    String tag;
    DatabaseReference teacherDB;
    FirebaseAuth user;
    Course course;
    String courseId;
    ProgressDialog progressBar;
    StorageReference storageRef;
    FirebaseStorage storage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);


        btnUpload = findViewById(R.id.uploadVideo);


        Button videoPikerButton = findViewById(R.id.videoPickerButton);
        videoPikerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                System.out.println();
            }
        });

        Button displayImagePikerButton = findViewById(R.id.displayImagePickerButton);
        displayImagePikerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select video"), 2);
                System.out.println();
            }
        });


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        progressBar = new ProgressDialog(AddCourseActivity.this);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                user = FirebaseAuth.getInstance();
                teacherDB = FirebaseDatabase.getInstance().getReference("Teacher").child(user.getUid()).child("courseOffered");
                course = new Course();
                courseId = teacherDB.push().getKey();


                InputStream videoStream = null;
                InputStream picStream = null;
                try {
                    videoStream = new FileInputStream(new File(videoFilePath));
                    picStream = new FileInputStream(new File(picFilePath));
                } catch (Exception e) {
                    Log.i("louda upload", "path : " + videoFilePath);
                }

                StorageReference mountainsRef = storageRef.child(courseId);


                UploadTask picUploadTask = mountainsRef.child("pic").putStream(picStream);
                picUploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri profileUri = taskSnapshot.getDownloadUrl();
                        course.setProfileUri(profileUri.toString());
                    }
                });

                UploadTask videoUploadTask = mountainsRef.child("video").putStream(videoStream);
                videoUploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        progressBar.dismiss();
                        Toast.makeText(AddCourseActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        progressBar.dismiss();
                        Toast.makeText(AddCourseActivity.this, "success", Toast.LENGTH_SHORT).show();
                        Uri videoUri = taskSnapshot.getDownloadUrl();
                        Log.i("louda upload", videoUri.toString());


                        teacherDB.child(courseId).setValue("courseID");

                        //Setting values to course from UI
                        aboutProfessor = ((EditText) findViewById(R.id.aboutProfessorTextView)).getText().toString();
                        courseName = ((EditText) findViewById(R.id.courseNameTextView)).getText().toString();
                        courseDetails = ((EditText) findViewById(R.id.detailsTextView)).getText().toString();
                        available = true;
                        courseUri = videoUri.toString();
                        tag = ((EditText) findViewById(R.id.tagTextView)).getText().toString();
                        List<String> tags = new LinkedList<>();
                        StringTokenizer tag1 = new StringTokenizer(tag, ",");
                        while (tag1.hasMoreElements()) {
                            tags.add(tag1.nextToken());
                        }

                        course.setAboutProfessor(aboutProfessor);
                        course.setCourseName(courseName);
                        course.setCourseDetails(courseDetails);
                        course.setAvailable(available);
                        course.setCourseUri(courseUri);
                        course.setTags(tags);


                        final DatabaseReference courseDB = FirebaseDatabase.getInstance().getReference("Course");

                        Log.i("louda child count", "start: " + course.getTaughtBy());
                        HashMap hm = new HashMap();
                        hm.put(courseId, course);
                        courseDB.updateChildren(hm);
                        Log.i("louda child count", "finished");

                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Teacher").child(user.getUid()).child("personalDetails");

                        db.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if (dataSnapshot1.getKey().equals("name")) {
                                        Log.i("louda child count", dataSnapshot1.getValue(String.class));
                                        final String s = dataSnapshot1.getValue(String.class);
                                        courseDB.child(courseId).updateChildren(new HashMap() {{
                                            put("name", s);
                                        }});
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        Intent intent = new Intent(AddCourseActivity.this, EditCourseActivity.class);
                        intent.putExtra("courseId", courseId);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {


        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                videoFilePath = getPath(getApplicationContext(), uri);

                Log.i("storage", "Uri: " + videoFilePath);
            }
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {

            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                picFilePath = getPath(getApplicationContext(), uri);

                Log.i("storage", "Uri: " + picFilePath);
            }
        }
    }

    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
