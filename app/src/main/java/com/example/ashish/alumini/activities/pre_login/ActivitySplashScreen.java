package com.example.ashish.alumini.activities.pre_login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ashish.alumini.R;
import com.example.ashish.alumini.activities.post_login.MainScreenActivity;
import com.example.ashish.alumini.application.MyApplication;
import com.example.ashish.alumini.supporting_classes.GlobalPrefs;

public class ActivitySplashScreen extends AppCompatActivity {


    // to start either login or main screen
    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //hide the actionBar
        getSupportActionBar().hide();


        boolean spLogin = new GlobalPrefs(getApplicationContext())
                .getBoolean(getString(R.string.is_logged_in));


        if (spLogin){
            // skip the login/sign up tabbed activities
            MyApplication mApplication = (MyApplication) getApplication();
            mApplication.createListCLass();
            mIntent = new Intent(this, MainScreenActivity.class);
        }
        else{
            // SWITCH TO login/sign up screens
            mIntent = new Intent(this,MainActivity.class);
        }


        //creating thread to hold screen for splash
        Thread splashTimer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    startActivity(mIntent);
                }
            }
        };
        splashTimer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
         finish();
    }


}
