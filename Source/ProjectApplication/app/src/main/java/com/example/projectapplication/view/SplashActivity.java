package com.example.projectapplication.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.projectapplication.R;
import com.example.projectapplication.manager.Constants;
import com.example.projectapplication.manager.MyApplication;
import com.example.projectapplication.model.UserInforResponse;
import com.example.projectapplication.network.MyAPIClient;
import com.example.projectapplication.network.UserService;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import java.sql.Time;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    Timer timer;
    ProgressBar progressBar;
    UserService userService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        progressBar =(ProgressBar)findViewById(R.id.progressBar);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String accessToken = sharedPref.getString(getString(R.string.saved_access_token), null);
        long time = sharedPref.getLong(getString(R.string.saved_access_token_time), (long)0);
        // Get user info with access token
        long expire = (new Date()).getTime()/1000 - time;
        MyAPIClient.getInstance().setAccessToken(accessToken);
        timer = new Timer();
        Log.d("TOKEN", "onCreate: "+accessToken);


        if (TextUtils.isEmpty(accessToken) || expire > Constants.expire_token) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return;
                }
            },1000);
        }else{

            timer.schedule(new TimerTask() {
                @Override
                public void run() {

                    Intent intent = new Intent(SplashActivity.this, ListTours.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                    return;
                }
            },1000);
        }
    }



}
