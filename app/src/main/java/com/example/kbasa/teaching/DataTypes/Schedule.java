package com.example.kbasa.teaching.DataTypes;

import android.util.Log;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kbasa on 2/26/2018.
 */

public class Schedule {

    String courseId;
    String professorId;
    MyDate myDate;

    public Schedule(String courseId, String professorId, MyDate myDate) {
        this.courseId = courseId;
        this.professorId = professorId;
        this.myDate = myDate;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public MyDate getMyDate() {
        return myDate;
    }

    public void setMyDate(MyDate myDate) {
        this.myDate = myDate;
    }
}
