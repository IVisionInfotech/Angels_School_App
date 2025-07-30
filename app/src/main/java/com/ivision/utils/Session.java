package com.ivision.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Session {

    private SharedPreferences prefs;

    public Session(Context cntx) {
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void clearData() {
        prefs.edit().clear().apply();
    }

    public Boolean getLoginStatus() {
        return prefs.getBoolean("loginStatus", false);
    }

    public void setLoginStatus(Boolean status) {
        prefs.edit().putBoolean("loginStatus", status).apply();
    }

    public String getStudentId() {
        return prefs.getString("studentId", "");
    }

    public void setStudentId(String studentId) {
        prefs.edit().putString("studentId", studentId).apply();
    }

    public String getUsername() {
        return prefs.getString("username", "");
    }

    public void setUsername(String username) {
        prefs.edit().putString("username", username).apply();
    }

    public String getPassword() {
        return prefs.getString("password", "");
    }

    public void setPassword(String password) {
        prefs.edit().putString("password", password).apply();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        prefs.edit().putBoolean("isFirstTimeLaunch", isFirstTime).apply();
    }

    public boolean isFirstTimeLaunch() {
        return prefs.getBoolean("isFirstTimeLaunch", true);
    }
}