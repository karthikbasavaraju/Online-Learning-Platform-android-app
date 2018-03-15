package com.example.kbasa.teaching.DataTypes;

import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.LinkedList;
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
    String professorTokenId;
    List<String> tags;
    String professorId;
    List<HashMap<String,MyDate>> schedules;   //To store students id and their status(enrolled or complete)

    public Course(){
        schedules = new LinkedList<HashMap<String, MyDate>>();
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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getCourseUri() {
        return courseUri;
    }

    public void setCourseUri(String courseUri) {
        this.courseUri = courseUri;
    }

    public String getProfileUri() {
        return profileUri;
    }

    public void setProfileUri(String profileUri) {
        this.profileUri = profileUri;
    }

    public List<MyDate> getMyDate() {
        return myDate;
    }

    public void setMyDate(List<MyDate> myDate) {
        this.myDate = myDate;
    }

    public String getProfessorTokenId() {
        return professorTokenId;
    }

    public void setProfessorTokenId(String professorTokenId) {
        this.professorTokenId = professorTokenId;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public List<HashMap<String, MyDate>> getSchedules() {
        return schedules;
    }

    public void setSchedules(List<HashMap<String, MyDate>> schedules) {
        this.schedules = schedules;
    }

    public Course(String name, String aboutProfessor, String courseName, String courseDetails, boolean available, String courseUri, String profileUri, List<MyDate> myDate, String professorTokenId, List<String> tags, String professorId, List<HashMap<String, MyDate>> schedules) {

        this.name = name;
        this.aboutProfessor = aboutProfessor;
        this.courseName = courseName;
        this.courseDetails = courseDetails;
        this.available = available;
        this.courseUri = courseUri;
        this.profileUri = profileUri;
        this.myDate = myDate;
        this.professorTokenId = professorTokenId;
        this.tags = tags;
        this.professorId = professorId;
        this.schedules = schedules;
    }
}

