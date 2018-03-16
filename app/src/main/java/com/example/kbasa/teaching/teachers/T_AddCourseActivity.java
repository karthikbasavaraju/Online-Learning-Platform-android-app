package com.example.kbasa.teaching.teachers;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kbasa.teaching.DataTypes.Course;
import com.example.kbasa.teaching.DataTypes.MyDate;
import com.example.kbasa.teaching.FieldsOk;
import com.example.kbasa.teaching.InputFilterMinMax;
import com.example.kbasa.teaching.LoginActivity;
import com.example.kbasa.teaching.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

public class T_AddCourseActivity extends AppCompatActivity {


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
    boolean flag=false;

    private Button btn_upload_v;
    private Button btn_upload_pic;

    private int mYear1, mMonth1, mDay1;

    private TextView mDateDisplay1, mDateDisplay2, mDateDisplay3;
    private Button mPickDate1, mPickDate2, mPickDate3;

    private int date_id;

    static final int DATE_DIALOG_ID = 0;
    TextView videoText;
    TextView picText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t__add_course);

        videoText = findViewById(R.id.tv_intro_video_name);
        picText = findViewById(R.id.tv_intro_picture_name);
        btn_upload_v = (Button) findViewById(R.id.btn_upload_video);
        btn_upload_pic = (Button) findViewById(R.id.btn_upload_picture);

        // check if the permission is granted
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                return;
            }
        }


        btn_upload_v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(T_AddCourseActivity.this)
                        .withFilter(Pattern.compile("[a-z]*.(mp4|MP4|Mp4|mP4|avi|flv|wmv)$"))
                        .withRequestCode(1)
                        .start();
            }
        });
        btn_upload_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker()
                        .withActivity(T_AddCourseActivity.this)
                        .withFilter(Pattern.compile("[a-z]*.(jpg|png|gif|bmp)$"))
                        .withRequestCode(2)
                        .start();
            }
        });

        // select date
        mDateDisplay1 = (TextView) findViewById(R.id.showMyDate1);
        mDateDisplay2 = (TextView) findViewById(R.id.showMyDate2);
        mDateDisplay3 = (TextView) findViewById(R.id.showMyDate3);

        mPickDate1 = (Button) findViewById(R.id.myDatePickerButton1);
        mPickDate2 = (Button) findViewById(R.id.myDatePickerButton2);
        mPickDate3 = (Button) findViewById(R.id.myDatePickerButton3);

        mPickDate1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
                date_id = 1;
            }
        });
        mPickDate2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
                date_id = 2;
            }
        });
        mPickDate3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
                date_id = 3;
            }
        });

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear1 = c.get(Calendar.YEAR);
        mMonth1 = c.get(Calendar.MONTH);
        mDay1 = c.get(Calendar.DAY_OF_MONTH);

        // display the current date
        for(int i=1; i<=3; i++) {
            date_id = i;
            updateDisplay();
        }



        btnUpload = findViewById(R.id.upload);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        progressBar = new ProgressDialog(T_AddCourseActivity.this);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean fieldsOK = FieldsOk.validate(new EditText[] { findViewById(R.id.courseNameTextView),findViewById(R.id.descriptionTextView), findViewById(R.id.tagTextView) });
                boolean filesOk =  FieldsOk.vlidate(new TextView[] { findViewById(R.id.tv_intro_video_name),findViewById(R.id.tv_intro_picture_name) });
                if(fieldsOK && filesOk) {

                    final ProgressDialog dialog = new ProgressDialog(T_AddCourseActivity.this);
                    dialog.setMessage("uploading your course");
                    dialog.setIndeterminate(true);
                    dialog.show();


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
                            dialog.dismiss();
                            Toast.makeText(T_AddCourseActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            dialog.dismiss();
                            Toast.makeText(T_AddCourseActivity.this, "success", Toast.LENGTH_SHORT).show();
                            Uri videoUri = taskSnapshot.getDownloadUrl();
                            Log.i("louda upload", videoUri.toString());


                            teacherDB.child(courseId).setValue("courseID");

                            //Setting values to course from UI
//                        aboutProfessor = ((EditText) findViewById(R.id.aboutProfessorTextView)).getText().toString();
                            courseName = ((EditText) findViewById(R.id.courseNameTextView)).getText().toString();
                            courseDetails = ((EditText) findViewById(R.id.descriptionTextView)).getText().toString();
                            available = true;
                            courseUri = videoUri.toString();
                            tag = ((EditText) findViewById(R.id.tagTextView)).getText().toString();
                            List<String> tags = new LinkedList<>();
                            StringTokenizer tag1 = new StringTokenizer(tag, ",");
                            while (tag1.hasMoreElements()) {
                                tags.add(tag1.nextToken());
                            }

                            course.setAboutProfessor("I am professor");
                            course.setCourseName(courseName);
                            course.setCourseDetails(courseDetails);
                            course.setAvailable(available);
                            course.setCourseUri(courseUri);
                            course.setTags(tags);
                            course.setProfessorId(user.getUid());
                            course.setName(user.getCurrentUser().getEmail().split("@")[0]);
                            course.setProfessorTokenId(FirebaseInstanceId.getInstance().getToken());

                            List<MyDate> myDates = new ArrayList<>();

                            TextView date1 = findViewById(R.id.showMyDate1);
                            EditText hour1 = findViewById(R.id.hour1);
                            hour1.setFilters(new InputFilter[]{new InputFilterMinMax("0", "23")});
                            EditText minute1 = findViewById(R.id.min1);
                            minute1.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});
                            myDates.add(new MyDate(date1.getText().toString(),
                                    Integer.parseInt(hour1.getText().toString()),
                                    Integer.parseInt(minute1.getText().toString())));

                            TextView date2 = findViewById(R.id.showMyDate2);
                            EditText hour2 = findViewById(R.id.hour2);
                            hour2.setFilters(new InputFilter[]{new InputFilterMinMax("0", "23")});
                            EditText minute2 = findViewById(R.id.min2);
                            minute2.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});
                            myDates.add(new MyDate(date2.getText().toString(),
                                    Integer.parseInt(hour2.getText().toString()),
                                    Integer.parseInt(minute2.getText().toString())));

                            TextView date3 = findViewById(R.id.showMyDate3);
                            EditText hour3 = findViewById(R.id.hour3);
                            hour3.setFilters(new InputFilter[]{new InputFilterMinMax("0", "24")});
                            EditText minute3 = findViewById(R.id.min3);
                            minute3.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});
                            myDates.add(new MyDate(date3.getText().toString(),
                                    Integer.parseInt(hour3.getText().toString()),
                                    Integer.parseInt(minute3.getText().toString())));
                            course.setMyDate(myDates);


                            final DatabaseReference courseDB = FirebaseDatabase.getInstance().getReference("Course");

                            Log.i("louda child count", "start: " + course.getName());
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
                                    flag = true;

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            Intent intent = new Intent(T_AddCourseActivity.this, ViewCourseActivity.class);
                            intent.putExtra("courseId", courseId);
                            startActivity(intent);
                            finish();

                        }
                    });
                }
                else
                    Toast.makeText(T_AddCourseActivity.this, "Fields cant be empty", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateDisplay() {
        switch (date_id) {
            case 1:
                this.mDateDisplay1.setText(
                        new StringBuilder()
                                // Month is 0 based so add 1
                                .append(mMonth1 + 1).append("-")
                                .append(mDay1).append("-")
                                .append(mYear1));
                break;
            case 2:
                this.mDateDisplay2.setText(
                        new StringBuilder()
                                // Month is 0 based so add 1
                                .append(mMonth1 + 1).append("-")
                                .append(mDay1).append("-")
                                .append(mYear1));
                break;
            case 3:
                this.mDateDisplay3.setText(
                        new StringBuilder()
                                // Month is 0 based so add 1
                                .append(mMonth1 + 1).append("-")
                                .append(mDay1).append("-")
                                .append(mYear1));
                break;
        }

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear1 = year;
                    mMonth1 = monthOfYear;
                    mDay1 = dayOfMonth;

                    updateDisplay();

                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear1, mMonth1, mDay1);
        }

        return null;
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {

            if (data != null) {
                File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                videoFilePath = f.getAbsolutePath();
                int lastIndex = videoFilePath.lastIndexOf('/');
                videoText.setText(videoFilePath.substring(lastIndex + 1));
                Log.i("AddCourse - Video path", videoFilePath);
            }
        }
        else {
                if (data != null) {
                    File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
                    picFilePath = f.getAbsolutePath();

                    int lastIndex = picFilePath.lastIndexOf('/');
                    picText.setText(picFilePath.substring(lastIndex + 1));
                    Log.i("AddCourse - Video path", picFilePath);
                }
            }

        }

    private String getMimeType(String path) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(path);

        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
    }
}
