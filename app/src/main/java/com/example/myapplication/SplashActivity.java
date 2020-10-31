package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class SplashActivity extends AppCompatActivity {

    Config config;
    Intent intent;
//    DatabaseHandler dataBaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        config = new Config(this);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!config.getStringValue(Config.USER_LOGIN).equals("")){
//                    Toast.makeText(SplashActivity.this, config.getStringValue(Config.USER_LOGIN)+" Already signed in", Toast.LENGTH_SHORT).show();
                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }else{
                    Toast.makeText(SplashActivity.this,"Not sign in", Toast.LENGTH_SHORT).show();

                    intent = new Intent(SplashActivity.this,SignInActivity.class);
                }
                startActivity(intent);
            }
        }, 5000);

    }
}