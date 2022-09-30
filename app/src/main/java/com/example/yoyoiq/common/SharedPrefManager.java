package com.example.yoyoiq.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.example.yoyoiq.Model.UserData;

public class SharedPrefManager {
    private static String SHARED_PREF_NAME = "YoyoIqUserDetails";
    private SharedPreferences sharedPreferences;
    Context context;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        this.context = context;
    }

    public void saveUser(UserData userData) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("userName", userData.getUserName());
        editor.putString("mobileNo", userData.getMobileNo());
        editor.putString("emailId", userData.getEmailId());
        editor.putString("user_id", userData.getUser_id());
        editor.putBoolean("logged", true);
        editor.apply();
    }

    public UserData getUserData() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UserData(sharedPreferences.getString("userName", ""), sharedPreferences.getString("mobileNo", ""),
                sharedPreferences.getString("emailId", ""), sharedPreferences.getString("user_id", ""));
    }

    public boolean isLoggedIn() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("logged", false);
    }

    public void logout() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
