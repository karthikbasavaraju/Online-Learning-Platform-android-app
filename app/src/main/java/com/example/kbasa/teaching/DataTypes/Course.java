package com.example.kbasa.teaching.DataTypes;

import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kbasa on 2/26/2018.
 */

public class Course {

    String taughtBy;
    String aboutProfessor;
    String courseName;
    String courseDetails;
    boolean available;
    String courseUri;
    String profileUri;
    List<String> tags;
    List<HashMap<Integer,String>> student;   //To store student id and their status(enrolled or complete)

    public Course(){
        available = true;
        Log.i("DataConstructors","In Course");
    }

    public Course(String taughtBy, String aboutProfessor, String courseName, String courseDetails, List<String> tags,
                  List<HashMap<Integer, String>> student, String courseUri, String profileUri) {
        this.taughtBy = taughtBy;
        this.aboutProfessor = aboutProfessor;
        this.courseName = courseName;
        this.courseDetails = courseDetails;
        this.tags = tags;
        this.student = student;
        this.courseUri = courseUri;
        this.profileUri = profileUri;
    }

    public String getCourseUri() {
        return courseUri;
    }

    public void setCourseUri(String uri) {
        this.courseUri = uri;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getTaughtBy() {
        return taughtBy;
    }

    public void setTaughtBy(String taughtBy) {
        this.taughtBy = taughtBy;
    }

    public String getAboutProfessor() {
        return aboutProfessor;
    }

    public void setAboutProfessor(String aboutProfessor) {
        this.aboutProfessor = aboutProfessor;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseDetails() {
        return courseDetails;
    }

    public void setCourseDetails(String courseDetails) {
        this.courseDetails = courseDetails;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<HashMap<Integer, String>> getStudent() {
        return student;
    }

    public void setStudent(List<HashMap<Integer, String>> student) {
        this.student = student;
    }
}
