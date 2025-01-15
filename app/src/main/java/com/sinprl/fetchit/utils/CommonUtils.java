package com.sinprl.fetchit.utils;

import android.widget.EditText;


public class CommonUtils {

    public static boolean check_for_empty_text(EditText inputbox, String errorMessage){
        if (inputbox.getText().toString().length() != 10)
        {
            inputbox.setError(errorMessage);
            return false;
        }
        return true;
    }
}
