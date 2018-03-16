package com.example.kbasa.teaching;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kbasa.teaching.DataTypes.Student;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.TimeZone;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private boolean studentFlag = false;
    private boolean teacherFlag = false;
    private boolean studentFlag1 = false;
    private boolean teacherFlag1 = false;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        auth = FirebaseAuth.getInstance();

        //To check if user has logged in
        /*if(auth.getCurrentUser()!=null){
            SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
            boolean userType = sharedPref.getBoolean("IsTeacher",false);
            //Which type of user
            //Teacher
            if(userType==true){
                Intent intent = new Intent(this,TeacherHomeActivity.class);
                startActivity(intent);
                finish();
            }
            //Student
            else{
                Intent intent = new Intent(this, StudentHomeActivity.class);
                startActivity(intent);
                finish();
            }
        }*/



    }


    /*
    *   New Registration
    */


    public void toRegister(View v){
        Intent intent = new Intent(this,RegistrationActivity.class);
        startActivity(intent);
    }

    public void toStudentHome(View v){
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        boolean fieldsOK = FieldsOk.validate(new EditText[] { emailEditText,passwordEditText });
        if(fieldsOK) {
            studentFlag1 = true;
            auth.signInWithEmailAndPassword(/*emailEditText.getText().toString()*/"karthik@mail.com", "k26616495"/*passwordEditText.getText().toString()*/)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            final FirebaseAuth user = FirebaseAuth.getInstance();
                            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Student");
                            db.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                        if (dataSnapshot1.getKey().equals(user.getUid())) {
                                            studentFlag = true;
                                        }
                                    }

                                    if (studentFlag == true && studentFlag1 == true) {
                                        Intent intent = new Intent(LoginActivity.this, TeacherHomeActivity.class);
                                        intent.putExtra("user", "student");
                                        startActivity(intent);
                                        studentFlag1 = false;
                                        studentFlag = false;
                                        SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPref.edit();
                                        editor.putBoolean("IsStudent", true);
                                        editor.putBoolean("IsTeacher", false);
                                        editor.commit();
                                        finish();
                                    } else {
                                        studentFlag = false;
                                        studentFlag1 = false;
                                        Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else{
            if(!fieldsOK)
                Toast.makeText(LoginActivity.this, "Fields cant be empty", Toast.LENGTH_SHORT).show();
        }

    }

    public void toTeacherHome(View v){

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        boolean fieldsOK = FieldsOk.validate(new EditText[] { emailEditText,passwordEditText });
        if(fieldsOK) {

            teacherFlag1 = true;
        auth.signInWithEmailAndPassword("eskafif@scu.edu"/*emailEditText.getText().toString()*/,/*passwordEditText.getText().toString()*/"mobileapp")
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {

                        final FirebaseAuth user = FirebaseAuth.getInstance();
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Teacher");
                        db.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    if(dataSnapshot1.getKey().equals(user.getUid())){
                                        teacherFlag=true;
                                    }
                                }
                                if(teacherFlag==true && teacherFlag1==true){
                                    Intent intent = new Intent(LoginActivity.this,TeacherHomeActivity.class);
                                    intent.putExtra("user","teacher");
                                    startActivity(intent);
                                    teacherFlag1 = false;
                                    teacherFlag=false;
                                    SharedPreferences sharedPref = LoginActivity.this.getPreferences(Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPref.edit();
                                    editor.putBoolean("IsTeacher", false);
                                    editor.putBoolean("IsStudent", true);
                                    editor.commit();
                                    finish();
                                }
                                else {
                                    teacherFlag1 = false;
                                    teacherFlag=false;
                                    Toast.makeText(LoginActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this,"Login failed",Toast.LENGTH_SHORT).show();
                    }
                });
        }
        else
            Toast.makeText(LoginActivity.this, "Fields cant be empty", Toast.LENGTH_SHORT).show();
    }

}

