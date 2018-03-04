package com.example.kbasa.teaching.DataTypes;

import android.util.Log;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kbasa on 2/26/2018.
 */

public class Schedule {

    String courseId;
    String OtherPersonid;
    TimeZone time;
    Date date;

    public Schedule(){
        Log.i("DataConstructors","In Schedules");
    }

    public Schedule(String courseId, String otherPersonid, TimeZone time, Date date) {
        this.courseId = courseId;
        this.OtherPersonid = otherPersonid;
        this.time = time;
        this.date = date;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getOtherPersonid() {
        return OtherPersonid;
    }

    public void setOtherPersonid(String otherPersonid) {
        OtherPersonid = otherPersonid;
    }

    public TimeZone getTime() {
        return time;
    }

    public void setTime(TimeZone time) {
        this.time = time;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
