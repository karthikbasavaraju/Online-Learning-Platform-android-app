package com.example.kbasa.teaching.DataTypes;

import android.content.Intent;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    public MyDate(){}

    public  MyDate(String mydate){
        String stringSplit[] = mydate.split(" ");
        String dateSplit[] = stringSplit[0].split(":");
        String timeSplit[] = stringSplit[1].split(":");
        this.month = Integer.parseInt(dateSplit[0]);
        this.day = Integer.parseInt(dateSplit[1]);
        this.year = Integer.parseInt(dateSplit[2]);

        this.hour = Integer.parseInt(timeSplit[0]);
        this.minute= Integer.parseInt(timeSplit[0]);

    }

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


    public int compare(int duration){

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("MM:dd:yyyy HH:mm");
        MyDate now = new MyDate(dateFormat.format(date));

        Calendar currentTime = Calendar.getInstance();
        currentTime.set(now.getYear(), now.getMonth()-1, now.getDay(),now.getHour(),now.getMinute());

        Calendar courseTime = Calendar.getInstance();
        courseTime.set(this.getYear(), this.getMonth()-1, this.getDay(),this.getHour(),this.getMinute());

        long currentTimeTimeInMillis = currentTime.getTimeInMillis();
        long courseTimeTimeInMillis = courseTime.getTimeInMillis();
        long interval = currentTimeTimeInMillis-courseTimeTimeInMillis;
        long intervalInMinutes = interval/(60*1000);

        if(intervalInMinutes<duration && intervalInMinutes>=0)
            return 0;
        else if(intervalInMinutes<0)
            return 1;
        else
            return -1;
    }

}
