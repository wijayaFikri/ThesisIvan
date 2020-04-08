package com.ivan.thesisproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.ivan.thesisproject.R;
import com.ivan.thesisproject.models.UserAuth;
import com.ivan.thesisproject.retrofit.CallApi;
import com.ivan.thesisproject.retrofit.MyRetrofit;
import com.ivan.thesisproject.utilities.SharedPrefKey;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void goLogin(View view) {
        final Context context = this;
        EditText usernameEt = findViewById(R.id.login_username_editText);
        EditText passwordEt = findViewById(R.id.login_password_editText);
        String username = usernameEt.getText().toString();
        String password = usernameEt.getText().toString();
        if (username.equals("")) {
            usernameEt.setError("Username can't be empty");
            return;
        } else if (password.equals("")) {
            passwordEt.setError("Password can't be empty");
            return;
        }

        final ProgressDialog dialog = ProgressDialog.show(this, "",
                "Logging in , please wait ...", true);

        UserAuth userAuth = new UserAuth(username, password);
        MyRetrofit myRetrofit = new MyRetrofit(getSharedPreferences(SharedPrefKey.PREFERENCE_KEY, MODE_PRIVATE));
        myRetrofit.userLogin(userAuth, new CallApi<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                dialog.dismiss();
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(String message) {
                dialog.dismiss();
                Toast.makeText(context, message,
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}
