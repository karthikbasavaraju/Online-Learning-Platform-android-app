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
                new MaterialFilePicker()
                        .withActivity(getActivity())
                        .withFilter(Pattern.compile("[a-z]*.(jpg|png|gif|bmp)$"))
                        .withRequestCode(1)
                        .start();
            }
        });

        DatabaseReference courseDB = FirebaseDatabase.getInstance().getReference("Teacher").child(auth.getUid());
        courseDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("profilePic").exists()){
                    String profilePic = dataSnapshot.child("profilePic").getValue(String.class);
                    Picasso.with(getActivity().getApplicationContext()).load(profilePic).into(imageView);

                }
                name.setText(dataSnapshot.child("personalDetails").child("fullName").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        Log.i("profilePic",picFilePath);
        if(picFilePath!=null){
            final DatabaseReference courseDB = FirebaseDatabase.getInstance().getReference("Teacher").child(FirebaseAuth.getInstance().getUid());
            InputStream picStream = null;
            try {
                picStream = new FileInputStream(new File(picFilePath));
            } catch (Exception e) {
                Log.i("louda upload", "path : " + picStream);
            }

            StorageReference mountainsRef = FirebaseStorage.getInstance().getReference();


            UploadTask picUploadTask = mountainsRef.child("pic").putStream(picStream);
            picUploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri profileUri = taskSnapshot.getDownloadUrl();
                    courseDB.updateChildren(new HashMap<String, Object>(){{put("profilePic",profileUri);}});

                }
            });

        }

    }


}
