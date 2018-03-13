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
    CourseTaken courseTaken;

    public PersonalDetails getPersonalDetails() {
        return personalDetails;
    }

    public void setPersonalDetails(PersonalDetails personalDetails) {
        this.personalDetails = personalDetails;
    }

    public String getAboutYou() {
        return aboutYou;
    }

    public void setAboutYou(String aboutYou) {
        this.aboutYou = aboutYou;
    }

    public List<Integer> getCourseOffered() {
        return courseOffered;
    }

    public void setCourseOffered(List<Integer> courseOffered) {
        this.courseOffered = courseOffered;
    }

    public CourseTaken getCourseTaken() {
        return courseTaken;
    }

    public void setCourseTaken(CourseTaken courseTaken) {
        this.courseTaken = courseTaken;
    }

    public Teacher(PersonalDetails personalDetails, String aboutYou, List<Integer> courseOffered, CourseTaken courseTaken) {

        this.personalDetails = personalDetails;
        this.aboutYou = aboutYou;
        this.courseOffered = courseOffered;
        this.courseTaken = courseTaken;
    }
}
