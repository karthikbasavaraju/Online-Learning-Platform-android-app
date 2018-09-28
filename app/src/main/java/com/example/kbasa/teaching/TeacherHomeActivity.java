package com.example.kbasa.teaching;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.kbasa.teaching.students.HomeFragment;
import com.example.kbasa.teaching.students.ScheduleFragment;
import com.example.kbasa.teaching.teachers.T_AddCourseActivity;
import com.example.kbasa.teaching.teachers.T_HomeFragment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;

import static com.example.kbasa.teaching.ProfileFragment.picFilePath;

public class TeacherHomeActivity extends AppCompatActivity {

    private FragmentTransaction transaction;
    private MaterialSearchView materialSearchView;
    String role = "";
    Bundle savedInstanceStateT=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        final Bundle b = this.getIntent().getExtras();

        Log.i("please","pleasae");
        role = b.getString("user");

        // Search Bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Visdum");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
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


        new Thread(new Runnable() {
            @Override
            public void run() {



                // Bottom Nav-Bar
                BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
                if(role.equals("student")) {
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Student").child(FirebaseAuth.getInstance().getUid());
                    db.updateChildren(new HashMap<String, Object>(){{put("tokenId",FirebaseInstanceId.getInstance().getToken());}});




                    materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            Intent intent = new Intent(TeacherHomeActivity.this,StudentSearchActivity.class);
                            intent.putExtra("tag", query);
                            startActivity(intent);


                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            return false;
                        }
                    });


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
                                case R.id.action_schedule:
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
                                    break;
                            }
                            return true;
                        }
                    });
                }else if(role.equals("teacher")) {

                    new Notification().onTokenRefresh();



                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Teacher").child(FirebaseAuth.getInstance().getUid());
                    db.updateChildren(new HashMap<String, Object>(){{put("tokenId",FirebaseInstanceId.getInstance().getToken());}});



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
                                    transaction.replace(R.id.Fragment, new ScheduleFragment());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                    break;
                                case R.id.action_profile:
                                    transaction = getSupportFragmentManager().beginTransaction();
                                    transaction.replace(R.id.Fragment, new ProfileFragment());
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                    break;
                            }
                            return true;
                        }
                    });
                }

            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FragmentManager fm = this.getSupportFragmentManager();
        if(fm.getBackStackEntryCount()==1){
            fm.popBackStack();
        }
        else {
            for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                fm.popBackStack();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            }
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
