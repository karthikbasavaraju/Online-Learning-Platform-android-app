package com.example.kbasa.teaching.DataTypes;

import android.util.Log;

import java.util.Date;
import java.util.TimeZone;

/**
 * Created by kbasa on 2/26/2018.
 */

public class Schedule {


    String otherPersonsId;
    MyDate myDate;

    public Schedule(String otherPersonsId, MyDate myDate) {
        this.otherPersonsId = otherPersonsId;
        this.myDate = myDate;
    }

    public Schedule(){}


    public String getOtherPersonsId() {

        return otherPersonsId;
    }

    public void setOtherPersonsId(String otherPersonsId) {
        this.otherPersonsId = otherPersonsId;
    }

    public MyDate getMyDate() {
        return myDate;
    }

    public void setMyDate(MyDate myDate) {
        this.myDate = myDate;
    }
}
