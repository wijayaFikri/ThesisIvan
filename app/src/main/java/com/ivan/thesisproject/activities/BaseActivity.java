package com.ivan.thesisproject.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ivan.thesisproject.models.User;
import com.ivan.thesisproject.utilities.SharedPrefKey;
import com.ivan.thesisproject.utilities.SharedPrefUtils;

public abstract class BaseActivity extends AppCompatActivity {

    public void checkUser(){
        SharedPreferences sharedPreferences = getSharedPreferences(
                SharedPrefKey.PREFERENCE_KEY,MODE_PRIVATE);
        SharedPrefUtils sharedPrefUtils = new SharedPrefUtils(sharedPreferences);
        String userString = sharedPrefUtils.get(SharedPrefKey.USER_KEY);
        if (userString == null) {
            Intent intent = new Intent(this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        checkUser();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkUser();
    }
}
