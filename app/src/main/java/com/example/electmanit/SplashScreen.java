package com.example.electmanit;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.example.electmanit.activities.HomeActivity;
import com.example.electmanit.activities.LoginActivity;


public class SplashScreen extends AppCompatActivity {

    public static final String PREFERENCES = "prefKey";
    SharedPreferences sharedPreferences;
    public static final String isLogin = "islogin";


    @Override
    protected void onStart() {

        super.onStart();
        //to know whether user has logged in or not
        sharedPreferences = getApplicationContext().getSharedPreferences(PREFERENCES,MODE_PRIVATE);
        boolean bol = sharedPreferences.getBoolean(isLogin,false);
        setContentView(R.layout.activity_main);

        //How to make a splash screen
        new Handler().postDelayed(()->{
            if(bol) {
                startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                finish();
            }else{
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                finish();
            }
        },2500);
    }
}