package com.example.kbasa.teaching;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.kbasa.teaching.DataTypes.PersonalDetails;
import com.example.kbasa.teaching.DataTypes.Teacher;
import com.example.kbasa.teaching.DataTypes.UserData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class TeacherHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_home);
        final FirebaseAuth user = FirebaseAuth.getInstance();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Teacher").child(user.getUid());
        Log.i("louda",user.getUid());


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.i("louda child coubt" , String.valueOf(dataSnapshot.getChildrenCount()));



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(TeacherHomeActivity.this,"louda",Toast.LENGTH_SHORT).show();
            }
        });

        Button addCourseButton = findViewById(R.id.addCourseButton);
        addCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TeacherHomeActivity.this, AddCourseActivity.class);
                startActivity(intent);
            }
        });
    }
}
