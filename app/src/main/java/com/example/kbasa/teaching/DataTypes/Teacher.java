package com.example.kbasa.teaching.DataTypes;

import android.util.Log;

import com.example.kbasa.teaching.DataTypes.PersonalDetails;
import com.example.kbasa.teaching.DataTypes.Schedule;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by kbasa on 2/26/2018.
 */

public class Teacher {
    PersonalDetails personalDetails;
    String aboutYou;
    List<Integer> courseOffered;
    List<Schedule> schedules;

    public Teacher() {
        courseOffered = new LinkedList<>();
        schedules = new LinkedList<>();
        Log.i("DataConstructors","In Teacher def");
    }

    public Teacher(PersonalDetails personalDetails,String aboutYou, List<Integer> courseOffered, List<Schedule> schedules) {
        Log.i("DataConstructors","In Teacher para");
        this.personalDetails = personalDetails;
        this.courseOffered = courseOffered;
        this.schedules = schedules;
        this.aboutYou = aboutYou;

    }

    public String getAboutYou() {
        return aboutYou;
    }

    public void setAboutYou(String aboutYou) {
        this.aboutYou = aboutYou;
    }

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public List<Integer> getCourseOffered() {
        return courseOffered;
    }

    public List<Schedule> getSchedules() {
        return schedules;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public void setCourseOffered(List<Integer> courseOffered) {
        this.courseOffered = courseOffered;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
