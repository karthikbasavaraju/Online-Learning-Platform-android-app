package com.example.kbasa.teaching.DataTypes;

import android.util.Log;

import java.util.List;

/**
 * Created by kbasa on 2/26/2018.
 */

public class CourseTaken {
    List<Integer> past;
    List<Schedule> schedules;

    public CourseTaken() {
        Log.i("DataConstructors","In CourseTaken");
    }

    public CourseTaken(List<Integer> past, List<Schedule> schedules) {
        this.past = past;
        this.schedules = schedules;
    }

    public List<Integer> getPast() {
        return past;
    }

    public void setPast(List<Integer> past) {
        this.past = past;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
