package com.example.kbasa.teaching.DataTypes;

import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

/**
 * Created by kbasa on 2/26/2018.
 */

public class Course {

    String name;
    String aboutProfessor;
    String courseName;
    String courseDetails;
    boolean available;
    String courseUri;
    String profileUri;
    List<MyDate> myDate;
    List<String> tags;
    String professorId;
    List<HashMap<Integer,String>> student;   //To store student id and their status(enrolled or complete)

    public Course(){
        available = true;
        Log.i("DataConstructors","In Course");
    }

    public Course(String name, String aboutProfessor, String courseName, String courseDetails, boolean available,
                  String courseUri, String profileUri, List<MyDate> myDate, List<String> tags, String professorId,
                  List<HashMap<Integer, String>> student) {
        this.name = name;
        this.aboutProfessor = aboutProfessor;
        this.courseName = courseName;
        this.courseDetails = courseDetails;
        this.available = available;
        this.courseUri = courseUri;
        this.profileUri = profileUri;
        this.myDate = myDate;
        this.tags = tags;
        this.professorId = professorId;
        this.student = student;
    }

    public List<MyDate> getMyDate() {
        return myDate;
    }

    public void setMyDate(List<MyDate> myDate) {
        this.myDate = myDate;
    }

    public String getCourseUri() {
        return courseUri;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
