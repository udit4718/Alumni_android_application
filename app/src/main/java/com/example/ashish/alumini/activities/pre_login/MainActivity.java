package com.example.ashish.alumini.activities.pre_login;


import android.graphics.Color;
import android.os.Bundle;

import android.app.TabActivity;
import android.content.Intent;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.ashish.alumini.R;
import com.example.ashish.alumini.supporting_classes.GlobalBus;
import com.example.ashish.alumini.supporting_classes.ProgressBarVisibility;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.IndeterminateHorizontalProgressDrawable;

public class MainActivity extends TabActivity {
    /** Called when the activity is first created. */

    String TAG = getClass().getSimpleName();

    @Bind(R.id.material_progressBar_activity_main)
    ProgressBar mProgressBar;

    GlobalBus mGlobalBus = GlobalBus.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // butterknife bindings
        ButterKnife.bind(this);

        //event mGlobalBus registering
//        mGlobalBus.register(this);

        // setting progress bar
        mProgressBar.setIndeterminateDrawable(new IndeterminateHorizontalProgressDrawable(this));
        mProgressBar.setVisibility(View.INVISIBLE);

        TabHost tabHost = getTabHost();
        TabHost.TabSpec spec;

        Intent intent;

        intent = new Intent().setClass(this, Login.class);
        spec = tabHost.newTabSpec("Login").setIndicator("Login")
                .setContent(intent);
        tabHost.addTab(spec);


        intent = new Intent().setClass(this,SignUp.class);
        spec = tabHost.newTabSpec("Sign Up").setIndicator("Sign Up")
                .setContent(intent);
        tabHost.addTab(spec);


        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++)
        {
            TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title);
            tv.setTextColor(Color.parseColor("#ffffff"));
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        mGlobalBus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlobalBus.register(this);

    }
    @Subscribe
    public void hidePtogressBar(ProgressBarVisibility barVisibility){

        if (barVisibility.isVisibility()){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        Log.d(TAG,"Bus Working");

    }


}