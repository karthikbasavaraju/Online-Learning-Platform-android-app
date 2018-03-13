package com.example.kbasa.teaching;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class RegistrationActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repasswordEditText;
    private ToggleButton studentToggleButton;
    private ToggleButton teacherToggleButton;
    private EditText interestsEditText;

    private FirebaseAuth auth;
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
        if(passwordEditText.getText().toString().equals(repasswordEditText.getText().toString())) {
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


                            Student student = new Student(personalDetails,interest,null);
                            HashMap hm = new HashMap();
                            hm.put(auth.getUid(),student);
                            databaseReference.updateChildren(hm);
                            Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                            startActivity(intent);
                            Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            finish();

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
            Toast.makeText(RegistrationActivity.this, "Password doesnt match", Toast.LENGTH_SHORT).show();
        }
    }

    public void teacherRegister(){

            if(passwordEditText.getText().toString().equals(repasswordEditText.getText().toString())) {
                auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                //Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                databaseReference = FirebaseDatabase.getInstance().getReference();
                                databaseReference =databaseReference.child("Teacher");

                                PersonalDetails personalDetails = new PersonalDetails(firstNameEditText.getText().toString()+" "+lastNameEditText.getText().toString(),
                                        emailEditText.getText().toString(),null);

                                Teacher teacher = new Teacher(personalDetails,"I am professor",null,null);
                                HashMap hm = new HashMap();
                                hm.put(auth.getUid(),teacher);
                                databaseReference.updateChildren(hm);
                                Intent intent = new Intent(RegistrationActivity.this,LoginActivity.class);
                                startActivity(intent);
                                Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegistrationActivity.this, "Password doesnt match", Toast.LENGTH_SHORT).show();
            }
    }
}


