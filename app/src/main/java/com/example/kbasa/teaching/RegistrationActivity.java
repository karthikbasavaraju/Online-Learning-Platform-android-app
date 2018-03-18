package com.example.kbasa.teaching;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.kbasa.teaching.DataTypes.Student;
import com.example.kbasa.teaching.DataTypes.TeacherId;
import com.example.kbasa.teaching.DataTypes.PersonalDetails;
import com.example.kbasa.teaching.DataTypes.Teacher;
import com.example.kbasa.teaching.DataTypes.UserData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import static com.example.kbasa.teaching.ProfileFragment.picFilePath;

public class RegistrationActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repasswordEditText;
    private ToggleButton studentToggleButton;
    private ToggleButton teacherToggleButton;
    private EditText interestsEditText;
    private String profile;
    private FirebaseAuth auth;
    private String profileuri;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

 

    }


    public void register(View v){

        auth = FirebaseAuth.getInstance();

        firstNameEditText = findViewById(R.id.input_first_name);
        lastNameEditText = findViewById(R.id.input_last_name);
        emailEditText = findViewById(R.id.input_email);
        passwordEditText = findViewById(R.id.input_password);
        repasswordEditText = findViewById(R.id.input_password);
        studentToggleButton = findViewById(R.id.btn_students);
        teacherToggleButton = findViewById(R.id.btn_teachers);
        interestsEditText = findViewById(R.id.input_interests);




        if(studentToggleButton.isChecked())
            studentRegister();
        else if(teacherToggleButton.isChecked())
            teacherRegister();
        else
            Toast.makeText(RegistrationActivity.this,"select which type of user",Toast.LENGTH_SHORT).show();

    }

    public void checkStudent(View v) {

        studentToggleButton = findViewById(R.id.btn_students);
        teacherToggleButton = findViewById(R.id.btn_teachers);

        if(studentToggleButton.isChecked()) {
            teacherToggleButton.setChecked(false);
        }
    }


    public void checkTeacher(View v) {

        studentToggleButton = findViewById(R.id.btn_students);
        teacherToggleButton = findViewById(R.id.btn_teachers);

        if(teacherToggleButton.isChecked()) {
            studentToggleButton.setChecked(false);
        }
    }

    public void studentRegister(){

        boolean fieldsOK = FieldsOk.validate(new EditText[] { findViewById(R.id.input_first_name),findViewById(R.id.input_last_name),findViewById(R.id.input_password),findViewById(R.id.input_interests),findViewById(R.id.input_email) });
        if(fieldsOK && profile!=null) {
            auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference =databaseReference.child("Student");

                            PersonalDetails personalDetails = new PersonalDetails(firstNameEditText.getText().toString()+" "+lastNameEditText.getText().toString(),
                                    emailEditText.getText().toString(),null);

                            StringTokenizer stringTokenizer =  new StringTokenizer(interestsEditText.getText().toString(),",");
                            ArrayList<String> interest = new ArrayList<>();
                            while(stringTokenizer.hasMoreElements()){
                                interest.add(stringTokenizer.nextElement().toString());
                            }


                            final Student student = new Student(personalDetails,interest,null);



                            if (profile != null) {
                                InputStream picStream = null;
                                try {
                                    picStream = new FileInputStream(new File(profile));
                                } catch (Exception e) {
                                    Log.i("louda upload", "path : " + picStream);
                                }

                                StorageReference mountainsRef = FirebaseStorage.getInstance().getReference();

                                UploadTask picUploadTask = mountainsRef.child(FirebaseAuth.getInstance().getUid()).putStream(picStream);
                                picUploadTask.addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        final Uri profileUri = taskSnapshot.getDownloadUrl();
                                        student.setProfileUri(profileUri.toString());


                                        HashMap hm = new HashMap();
                                        hm.put(auth.getUid(),student);
                                        databaseReference.updateChildren(hm);
                                        Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        finish();

                                    }
                                });
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            Toast.makeText(RegistrationActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        }
    }

    public void teacherRegister(){

        boolean fieldsOK = FieldsOk.validate(new EditText[] { findViewById(R.id.input_first_name),findViewById(R.id.input_last_name),findViewById(R.id.input_password),findViewById(R.id.input_interests),findViewById(R.id.input_email) });
        if(fieldsOK && profile!=null) {
            auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference =databaseReference.child("Teacher");

                                PersonalDetails personalDetails = new PersonalDetails(firstNameEditText.getText().toString()+" "+lastNameEditText.getText().toString(),
                                        emailEditText.getText().toString(),null);

                                final Teacher teacher = new Teacher(personalDetails,"I am professor",null,null);





                                if (profile != null) {
                                    InputStream picStream = null;
                                    try {
                                        picStream = new FileInputStream(new File(profile));
                                    } catch (Exception e) {
                                        Log.i("louda upload", "path : " + picStream);
                                    }

                                    StorageReference mountainsRef = FirebaseStorage.getInstance().getReference();

                                    UploadTask picUploadTask = mountainsRef.child(FirebaseAuth.getInstance().getUid()).putStream(picStream);
                                    picUploadTask.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            final Uri profileUri = taskSnapshot.getDownloadUrl();
                                            teacher.setProfileUri(profileUri.toString());


                                            HashMap hm = new HashMap();
                                            hm.put(auth.getUid(), teacher);
                                            databaseReference.updateChildren(hm);
                                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistrationActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            else{
                Toast.makeText(RegistrationActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            }
    }

    public void profilePic(View v){
        Button imageView = findViewById(R.id.btn_profilePic);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker().withActivity(RegistrationActivity.this)
                        .withFilter(Pattern.compile("[a-z]+.(jpg|png|gif|bmp)$"))
                        .withRequestCode(1)
                        .start();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //  super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == -1) {
            File f = new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
            profile = f.getAbsolutePath();

        }
    }

}


