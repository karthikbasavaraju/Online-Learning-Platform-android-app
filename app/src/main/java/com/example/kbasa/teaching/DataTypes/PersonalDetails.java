package com.example.kbasa.teaching.DataTypes;

import android.util.Log;

import java.util.List;

/**
 * Created by kbasa on 2/26/2018.
 */

public class PersonalDetails {

    String fullName;
    String email;
    List<String> topics;


    public PersonalDetails() {
        Log.i("DataConstructors","In PersonalDetails");
    }

    public PersonalDetails(String fullName, String email, List<String> topics) {
        this.fullName = fullName;
        this.email = email;
        this.topics = topics;
    }

    public String getfullName() {
        return fullName;
    }

    public void setfullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getInterest() {
        return topics;
    }

    public void setInterest(List<String> topics) {
        this.topics = topics;
    }
}
