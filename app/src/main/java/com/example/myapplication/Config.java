package com.example.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

// SharedPreferences
public class Config {

    public static final String USER_LOGIN="user_login";
    public static final int ROLE_ADMIN = 1;
    public static final int ROLE_MANAGER = 2;
    public static final int ROLE_USER = 3;
    SharedPreferences sp;
    public Config(Context context) {

        sp = PreferenceManager.getDefaultSharedPreferences(context);
    }
    public void setStringValue(String key, String value){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,value).commit();

    }
    public String getStringValue(String key)
    {
        return sp.getString(key,"");

    }
}
