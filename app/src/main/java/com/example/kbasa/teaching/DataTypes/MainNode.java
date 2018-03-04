package com.example.kbasa.teaching.DataTypes;

import android.util.Log;

/**
 * Created by kbasa on 2/26/2018.
 */

public class MainNode {
    Student student;
    Teacher teacher;
    Course course;

    public MainNode(){
        Log.i("DataConstructors","In MainNode");
    }

    public MainNode(Student student, Teacher teacher, Course courses){
        this.student = student;
        this.teacher = teacher;
        this.course = courses;
    }


    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
