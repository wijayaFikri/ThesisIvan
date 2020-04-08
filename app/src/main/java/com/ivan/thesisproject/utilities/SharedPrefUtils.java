package com.ivan.thesisproject.utilities;

import android.content.SharedPreferences;

public class SharedPrefUtils {
    private SharedPreferences sharedPreferences;

    public SharedPrefUtils(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean save(String key, String value) {
        Boolean result = Boolean.FALSE;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        try {
            editor.putString(key, value);
            editor.apply();
            result = Boolean.TRUE;
        } catch (Exception e) {
            e.getStackTrace();
        }

        return result;
    }

    public String get(String key) {
        return sharedPreferences.getString(key, null);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.apply();
    }
}
