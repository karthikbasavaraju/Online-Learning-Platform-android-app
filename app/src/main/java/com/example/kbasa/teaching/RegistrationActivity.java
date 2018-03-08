package com.example.kbasa.teaching;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity {

    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText repasswordEditText;
    private Button studentRegisterButton;
    private Button teacherRegisterButton;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        auth = FirebaseAuth.getInstance();

        fullNameEditText = findViewById(R.id.fullNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        repasswordEditText = findViewById(R.id.repasswordEditText);
        studentRegisterButton = findViewById(R.id.studentRegisterButton);
        teacherRegisterButton = findViewById(R.id.teacherRegisterButton);

        studentRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(passwordEditText.getText().toString().equals(repasswordEditText.getText().toString())) {
                    auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    databaseReference = FirebaseDatabase.getInstance().getReference();
                                    databaseReference =databaseReference.child("Student");

                                    PersonalDetails personalDetails = new PersonalDetails(fullNameEditText.getText().toString(),
                                            emailEditText.getText().toString(),null);

                                    Teacher teacher = new Teacher(personalDetails,"nothing",null,null);
                                    HashMap hm = new HashMap();
                                    hm.put(auth.getUid(),teacher);
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
        });

        teacherRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(passwordEditText.getText().toString().equals(repasswordEditText.getText().toString())) {
                    auth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    //Toast.makeText(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    databaseReference = FirebaseDatabase.getInstance().getReference();
                                    databaseReference =databaseReference.child("Teacher");

                                    PersonalDetails personalDetails = new PersonalDetails(fullNameEditText.getText().toString(),
                                                                                          emailEditText.getText().toString(),null);

                                    Teacher teacher = new Teacher(personalDetails,"",null,null);
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
        });
    }

}
