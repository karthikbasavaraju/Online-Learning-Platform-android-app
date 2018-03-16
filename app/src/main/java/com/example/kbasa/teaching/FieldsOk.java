package com.example.kbasa.teaching;

import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by kbasa on 3/15/2018.
 */

public class FieldsOk {

    public static boolean validate(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                return false;
            }
        }
        return true;
    }

    public static boolean vlidate(TextView[] fields){
        for(int i = 0; i < fields.length; i++){
            TextView currentField = fields[i];
            if(currentField.getText().toString().equals("No picture selected") || currentField.getText().toString().equals("No video selected")){
                return false;
            }
        }
        return true;
    }
}
