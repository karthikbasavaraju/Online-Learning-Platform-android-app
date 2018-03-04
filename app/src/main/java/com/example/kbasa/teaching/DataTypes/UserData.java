package com.example.kbasa.teaching.DataTypes;

/**
 * Created by kbasa on 2/25/2018.
 */

public class UserData {
    public String fullName;
    public String email;

    public UserData(){

    }

    public UserData(String fullName, String email){
        this.fullName = fullName;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

}
