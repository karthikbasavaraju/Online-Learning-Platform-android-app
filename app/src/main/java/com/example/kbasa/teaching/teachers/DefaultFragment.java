package com.example.kbasa.teaching.teachers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.example.kbasa.teaching.TeacherHomeActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class DefaultFragment extends DialogFragment {

    /*
    Overriding onCreateDialog to supply the content of dialog
    */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //allowing you to display standard alerts that is managed by a fragment
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //Setting content of dialog
        builder.setMessage("Do you want to delete this course permanently");


        //setting positive button and listener to it
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final String courseId = getArguments().getString("courseId");
                final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Course").child(courseId);
                db.updateChildren(new HashMap<String, Object>(){{put("available",false);}});

                Toast.makeText(getActivity(),"Course deleted",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TeacherHomeActivity.class);
                intent.putExtra("user","teacher");
                startActivity(intent);
                getActivity().finish();


            }
        });

        //setting negative button and listener to it
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"you clicked on No",Toast.LENGTH_SHORT).show();
            }
        });

        //setting neutral button and listener to it
        builder.setNeutralButton("close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(),"you clicked on close",Toast.LENGTH_SHORT).show();
            }
        });

        //setting title
        builder.setTitle("Alert Dialog");

        //returns dialog object
        return builder.create();
    }
}
