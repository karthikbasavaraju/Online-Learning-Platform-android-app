package com.example.kbasa.teaching;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.kbasa.teaching.DataTypes.PersonalDetails;
import com.example.kbasa.teaching.DataTypes.Teacher;
import com.example.kbasa.teaching.DataTypes.UserData;
import com.example.kbasa.teaching.students.HomeFragment;
import com.example.kbasa.teaching.teachers.T_AddCourseActivity;
import com.example.kbasa.teaching.teachers.T_HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class TeacherHomeActivity extends AppCompatActivity {

    private FragmentTransaction transaction;
    private MaterialSearchView materialSearchView;

    String role = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.i("please","pleasae");
        Bundle b = this.getIntent().getExtras();
        role = b.getString("user");

        // Search Bar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
  //      setSupportActionBar(toolbar);
    //    getSupportActionBar().setTitle("Search");
      //  toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        materialSearchView = (MaterialSearchView)findViewById(R.id.search_view);

        // Set the default Fragment to HomeFragment
        if(savedInstanceState == null) {
            transaction = getSupportFragmentManager().beginTransaction();
            if(role.equals("student")) {
                transaction.add(R.id.Fragment,  new HomeFragment());
            }else if(role.equals("teacher")) {
                transaction.add(R.id.Fragment,  new T_HomeFragment());
            }
            transaction.commit();
        }

        // Bottom Nav-Bar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        if(role.equals("student")) {
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // Change the Fragment based on which button is clicked.
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.Fragment, new HomeFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                            break;
                        /*case R.id.action_schedule:
                            transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.Fragment, new ScheduleFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                            break;
                        case R.id.action_profile:
                            transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.Fragment, new ProfileFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                            break;*/
                    }
                    return true;
                }
            });
        }else if(role.equals("teacher")) {
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // Change the Fragment based on which button is clicked.
                    switch (item.getItemId()) {
                        case R.id.action_home:
                            transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.Fragment, new T_HomeFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                            break;
                        case R.id.action_schedule:
                            transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.Fragment, new T_HomeFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                            break;
                        case R.id.action_profile:
                            transaction = getSupportFragmentManager().beginTransaction();
                            transaction.replace(R.id.Fragment, new T_HomeFragment());
                            transaction.addToBackStack(null);
                            transaction.commit();
                            break;
                    }
                    return true;
                }
            });
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(item);
        return true;
    }

    public void addCourse(View v){
        Intent intent = new Intent(TeacherHomeActivity.this, T_AddCourseActivity.class);
        startActivity(intent);
    }
}
