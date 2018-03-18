package com.example.kbasa.teaching.teachers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.kbasa.teaching.DataTypes.Course;
import com.example.kbasa.teaching.DataTypes.MyDate;
import com.example.kbasa.teaching.FieldsOk;
import com.example.kbasa.teaching.InputFilterMinMax;
import com.example.kbasa.teaching.LoginActivity;
import com.example.kbasa.teaching.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;



public class T_EditCourseActivity extends AppCompatActivity {

    private int mYear1, mMonth1, mDay1;

    private TextView mDateDisplay1, mDateDisplay2, mDateDisplay3;
    private Button mPickDate1, mPickDate2, mPickDate3;

    private int date_id;

    static final int DATE_DIALOG_ID = 0;
    private Button btn_remove;
    private Button btn_save;
    String courseId="";
    Course course = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_t__edit_course);

        btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_save = (Button) findViewById(R.id.btn_save);

        // check if the permission is granted


        enable_button();

        // select date
        mDateDisplay1 = (TextView) findViewById(R.id.showMyDate1);
        mDateDisplay2 = (TextView) findViewById(R.id.showMyDate2);
        mDateDisplay3 = (TextView) findViewById(R.id.showMyDate3);

        mPickDate1 = (Button) findViewById(R.id.myDatePickerButton1);
        mPickDate2 = (Button) findViewById(R.id.myDatePickerButton2);
        mPickDate3 = (Button) findViewById(R.id.myDatePickerButton3);

        mPickDate1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
                date_id = 1;
            }
        });
        mPickDate2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
                date_id = 2;
            }
        });
        mPickDate3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID);
                date_id = 3;
            }
        });

        // get the current date
        final Calendar c = Calendar.getInstance();
        mYear1 = c.get(Calendar.YEAR);
        mMonth1 = c.get(Calendar.MONTH);
        mDay1 = c.get(Calendar.DAY_OF_MONTH);

        // display the current date
        for(int i=1; i<=3; i++) {
            date_id = i;
            updateDisplay();
        }


        Bundle b = getIntent().getExtras();

        if(b!=null){
            courseId = b.getString("courseId");
        }
        Log.i("test courseId",courseId);

        DatabaseReference courseDB = FirebaseDatabase.getInstance().getReference("Course").child(courseId);
        courseDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("test - editcount : ",String.valueOf(dataSnapshot.getChildrenCount()));
                course = dataSnapshot.getValue(Course.class);

                EditText courseName = findViewById(R.id.courseNameTextView);
                courseName.setText(course.getCourseName());

                EditText description = findViewById(R.id.descriptionTextView);
                description.setText(course.getCourseDetails());
                String tags="";
                int i;
                for(i=0;i<course.getTags().size()-1;i++){
                    tags = tags + course.getTags().get(i) +", ";
                }
                tags = tags + course.getTags().get(i);

                mDateDisplay1.setText(course.getMyDate().get(0).toString().split(" ")[0]);
                ((TextView)findViewById(R.id.hour1)).setText((course.getMyDate().get(0).toString().split(" ")[1]).split(":")[0]);
                ((TextView)findViewById(R.id.min1)).setText((course.getMyDate().get(0).toString().split(" ")[1]).split(":")[1]);

                mDateDisplay2.setText(course.getMyDate().get(1).toString().split(" ")[0]);
                ((TextView)findViewById(R.id.hour2)).setText((course.getMyDate().get(1).toString().split(" ")[1]).split(":")[0]);
                ((TextView)findViewById(R.id.min2)).setText((course.getMyDate().get(1).toString().split(" ")[1]).split(":")[1]);

                mDateDisplay3.setText(course.getMyDate().get(2).toString().split(" ")[0]);
                ((TextView)findViewById(R.id.hour3)).setText((course.getMyDate().get(2).toString().split(" ")[1]).split(":")[0]);
                ((TextView)findViewById(R.id.min3)).setText((course.getMyDate().get(2).toString().split(" ")[1]).split(":")[1]);

                EditText tagsEditText = findViewById(R.id.tagTextView);
                tagsEditText.setText(tags);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button enrollButton = findViewById(R.id.btn_save);
        enrollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean fieldsOK = FieldsOk.validate(new EditText[] { findViewById(R.id.courseNameTextView),findViewById(R.id.descriptionTextView), findViewById(R.id.tagTextView) });
                if(fieldsOK) {

                    String courseName = ((EditText) findViewById(R.id.courseNameTextView)).getText().toString();
                    String courseDetails = ((EditText) findViewById(R.id.descriptionTextView)).getText().toString();
                    String tag = ((EditText) findViewById(R.id.tagTextView)).getText().toString();
                    List<String> tags = new LinkedList<>();
                    StringTokenizer tag1 = new StringTokenizer(tag, ",");
                    while (tag1.hasMoreElements()) {
                        tags.add(tag1.nextToken());
                    }

                    course.setCourseName(courseName);
                    course.setCourseDetails(courseDetails);
                    course.setTags(tags);

                    List<MyDate> myDates = new ArrayList<>();

                    TextView date1 = findViewById(R.id.showMyDate1);
                    EditText hour1 = findViewById(R.id.hour1);
                    hour1.setFilters(new InputFilter[]{new InputFilterMinMax("0", "23")});
                    EditText minute1 = findViewById(R.id.min1);
                    minute1.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});
                    myDates.add(new MyDate(date1.getText().toString(),
                            Integer.parseInt(hour1.getText().toString()),
                            Integer.parseInt(minute1.getText().toString())));

                    TextView date2 = findViewById(R.id.showMyDate2);
                    EditText hour2 = findViewById(R.id.hour2);
                    hour2.setFilters(new InputFilter[]{new InputFilterMinMax("0", "23")});
                    EditText minute2 = findViewById(R.id.min2);
                    minute2.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});
                    myDates.add(new MyDate(date2.getText().toString(),
                            Integer.parseInt(hour2.getText().toString()),
                            Integer.parseInt(minute2.getText().toString())));

                    TextView date3 = findViewById(R.id.showMyDate3);
                    EditText hour3 = findViewById(R.id.hour3);
                    hour3.setFilters(new InputFilter[]{new InputFilterMinMax("0", "24")});
                    EditText minute3 = findViewById(R.id.min3);
                    minute3.setFilters(new InputFilter[]{new InputFilterMinMax("0", "59")});
                    myDates.add(new MyDate(date3.getText().toString(),
                            Integer.parseInt(hour3.getText().toString()),
                            Integer.parseInt(minute3.getText().toString())));
                    course.setMyDate(myDates);


                    DatabaseReference db = FirebaseDatabase.getInstance().getReference("Course");
                    Toast.makeText(T_EditCourseActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();
                    db.updateChildren(new HashMap<String, Object>() {{
                        put(courseId, course);
                    }});

                }
                else{
                    Toast.makeText(T_EditCourseActivity.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void enable_button() {
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatabaseReference db = FirebaseDatabase.getInstance().getReference("Course");
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.i("kkkk",String.valueOf(dataSnapshot.child(courseId).child("schedules").getChildrenCount()));
                        int flag = 0;
                        for(DataSnapshot dataSnapshot1:dataSnapshot.child(courseId).child("schedules").getChildren()){
                            for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                                    MyDate myDate = dataSnapshot2.getValue(MyDate.class);
                                    int index = myDate.compare(0);
                                    if(index == 1){
                                        Toast.makeText(T_EditCourseActivity.this,"Students have enrolled. Cannot delete the course",Toast.LENGTH_SHORT).show();
                                        flag=1;
                                    }
                            }
                        }
                        if(flag==0){
                            Bundle bundle = new Bundle();
                            bundle.putString("courseId", courseId);
                            bundle.putString("professorId", course.getProfessorId());
                            DefaultFragment dialog = new DefaultFragment();
                            dialog.setArguments(bundle);
                            dialog.show(getSupportFragmentManager(),"my_dia");
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


    }


    private void updateDisplay() {
        switch (date_id) {
            case 1:
                this.mDateDisplay1.setText(
                        new StringBuilder()
                                // Month is 0 based so add 1
                                .append(mMonth1 + 1).append("-")
                                .append(mDay1).append("-")
                                .append(mYear1));
                break;
            case 2:
                this.mDateDisplay2.setText(
                        new StringBuilder()
                                // Month is 0 based so add 1
                                .append(mMonth1 + 1).append("-")
                                .append(mDay1).append("-")
                                .append(mYear1));
                break;
            case 3:
                this.mDateDisplay3.setText(
                        new StringBuilder()
                                // Month is 0 based so add 1
                                .append(mMonth1 + 1).append("-")
                                .append(mDay1).append("-")
                                .append(mYear1));
                break;
        }

    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    mYear1 = year;
                    mMonth1 = monthOfYear;
                    mDay1 = dayOfMonth;

                    updateDisplay();

                }
            };

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener,
                        mYear1, mMonth1, mDay1);
        }

        return null;
    }


}
