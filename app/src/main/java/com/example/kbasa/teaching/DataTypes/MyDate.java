package com.example.kbasa.teaching.DataTypes;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by kbasa on 3/11/2018.
 */

public class MyDate {

    int year;
    int month;
    int day;
    int hour;
    int minute;

    public MyDate(String s, int hour, int minute) {
        StringTokenizer stringTokenizer = new StringTokenizer(s,"-");
        ArrayList<Integer> arrayList = new ArrayList();
        while(stringTokenizer.hasMoreElements())
            arrayList.add(Integer.parseInt(stringTokenizer.nextElement().toString()));
        this.month = arrayList.get(0);
        this.day = arrayList.get(1);
        this.year = arrayList.get(2);
        this.hour = hour;
        this.minute = minute;
    }

    public String toString(){
        return month+":"+day+":"+year+" "+hour+":"+minute;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }


}
