package com.example.yoyoiq.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.example.yoyoiq.LoginPojo.userLoginData;
import com.example.yoyoiq.Model.UserData;

import java.util.ArrayList;

public class SessionManager {
    private static String SHARED_PREF_NAME1 = "YoyoIq_UserDetails_from_Server";
    private static String SHARED_PREF_NAME2 = "Image";
    private SharedPreferences sharedPreferences;
    Context context;
    private SharedPreferences.Editor editor;

    public SessionManager(Context context) {
        this.context = context;
    }

    public void saveUser(UserData userData) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME1, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("userId", userData.getUser_id());
        editor.putString("mobileNoServer", userData.getMobileNo());
        editor.putString("emailIdServer", userData.getEmailId());
        editor.putString("userNameServer", userData.getUserName());
        editor.putBoolean("logged", true);
        editor.apply();
    }

    public UserData getUserData() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME1, Context.MODE_PRIVATE);
        return new UserData(sharedPreferences.getString("userNameServer", ""), sharedPreferences.getString("mobileNoServer", ""),
                sharedPreferences.getString("emailIdServer", ""), sharedPreferences.getString("userId", ""));
    }

    public boolean isLoggedIn() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME1, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("logged", false);
    }

    public void logout() {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME1, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void saveProfileImage(Uri Image){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString("UserprofileImage", String.valueOf(Image));
        editor.apply();
    }

    public Uri getUserProfileImage(){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME2, Context.MODE_PRIVATE);
        return Uri.parse(sharedPreferences.getString("UserprofileImage", ""));
    }
}