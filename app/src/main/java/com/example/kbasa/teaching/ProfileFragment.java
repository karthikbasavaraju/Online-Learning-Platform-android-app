package com.example.kbasa.teaching;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kbasa.teaching.teachers.T_AddCourseActivity;
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
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Pattern;


public class ProfileFragment extends Fragment {

    static String picFilePath=null;
    boolean flag1;
    boolean flag2;

    ImageView imageView;
    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        FirebaseUser auth = FirebaseAuth.getInstance().getCurrentUser();
        final TextView name = view.findViewById(R.id.tv_name);
        TextView email = view.findViewById(R.id.tv_email);
        imageView = view.findViewById(R.id.imageView);

        email.setText(auth.getEmail());
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialFilePicker().withActivity(getActivity())
                        .withFilter(Pattern.compile("[a-z]+.(jpg|png|gif|bmp)$"))
                        .withRequestCode(1)
                        .start();

            }
        });

        DatabaseReference courseDB = FirebaseDatabase.getInstance().getReference("Teacher").child(FirebaseAuth.getInstance().getUid());
        courseDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("personalDetails").child("fullName").getValue(String.class)!=null){
                    name.setText(dataSnapshot.child("personalDetails").child("fullName").getValue(String.class));
                    Picasso.with(getContext()).load(dataSnapshot.child("profileUri").getValue(String.class)).into(imageView);
                    flag1=true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference studentDB = FirebaseDatabase.getInstance().getReference("Student").child(FirebaseAuth.getInstance().getUid());
        studentDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("profilePic").exists() ) {
                    String profilePic = dataSnapshot.child("profilePic").getValue(String.class);
                    Picasso.with(getActivity().getApplicationContext()).load(profilePic).into(imageView);

                }
                if (dataSnapshot.child("personalDetails").child("fullName").getValue(String.class)!=null) {
                    name.setText(dataSnapshot.child("personalDetails").child("fullName").getValue(String.class));
                    Picasso.with(getContext()).load(dataSnapshot.child("profileUri").getValue(String.class)).into(imageView);
                    flag2=true;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button logout = view.findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        return view;
    }

}
