package com.example.kbasa.teaching.DataTypes;

/**
 * Created by kbasa on 3/13/2018.
 */

public class ScheduleTracker {

    public String courseId;
    public String courseName;
    public String studentId;
    public String studentName;
    public String professorId;
    public String professorName;
    public String studentTokenId;
    public String professorTokenId;
    public String scheduled;
    public String date;

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }



    public ScheduleTracker(){}

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getProfessorId() {
        return professorId;
    }

    public void setProfessorId(String professorId) {
        this.professorId = professorId;
    }

    public String getStudentTokenId() {
        return studentTokenId;
    }

    public void setStudentTokenId(String studentTokenId) {
        this.studentTokenId = studentTokenId;
    }

    public String getProfessorTokenId() {
        return professorTokenId;
    }

    public void setProfessorTokenId(String professorTokenId) {
        this.professorTokenId = professorTokenId;
    }

    public String getScheduled() {
        return scheduled;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ScheduleTracker(String courseId, String studentId, String professorId, String studentTokenId, String professorTokenId, String scheduled, String date) {

        this.courseId = courseId;
        this.studentId = studentId;
        this.professorId = professorId;
        this.studentTokenId = studentTokenId;
        this.professorTokenId = professorTokenId;
        this.scheduled = scheduled;
        this.date = date;
    }
}
