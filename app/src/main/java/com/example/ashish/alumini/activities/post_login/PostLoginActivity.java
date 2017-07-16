package com.example.ashish.alumini.activities.post_login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.example.ashish.alumini.R;
import com.example.ashish.alumini.fragments.FragmentMembers;
import com.example.ashish.alumini.fragments.filter_fragments.FragmentFilter;
import com.example.ashish.alumini.fragments.settings.FragmentSettings;
import com.example.ashish.alumini.fragments.filter_fragments.FragmentFilterResult;
import com.example.ashish.alumini.supporting_classes.CommonData;
import com.example.ashish.alumini.supporting_classes.MenuVisibility;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.IndeterminateHorizontalProgressDrawable;

public class PostLoginActivity extends AppCompatActivity {

    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    // creating instance so that one instance can be used over the whole app
    public FragmentMembers mFragmentMembers =new FragmentMembers();

    String TAG = getClass().getSimpleName();

    @Bind(R.id.material_progressBar)
    ProgressBar mProgressBar;



    Bus mBus = new Bus();

    /*
    * To manage the back press button
    * */
    Boolean mBackToMainScreen = true;
    Boolean mBackToSettings = true;
    Boolean mBackToJobList = true;
    Boolean mBackToFilter = true;

    ActionBar mActionBar;

    Fragment mFragmentMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_post_login);

        //butterknife injection
        ButterKnife.bind(this);
        //initializing the Action Bar
        mActionBar = getSupportActionBar();



        //setting up the action bar
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setElevation(0);



        mFragmentManager = getSupportFragmentManager();

        mFragmentMenu = mFragmentManager.findFragmentById(R.id.fragmentMenuContainer);

        //setting first fragement
        mFragmentTransaction = mFragmentManager.beginTransaction();

        if (savedInstanceState!=null && CommonData.mCurrentFragmentPostLogin!=null){
            // case when the activity is restarted and we have to  put the same fargment again
            changeFragment(CommonData.mCurrentFragmentPostLogin);
        }else {
//            mFragmentTransaction.add(R.id.fragment_container, mFragmentMembers);
//            mFragmentTransaction.commit();
            changeFragment(mFragmentMembers);
        }

        // making the progress bar indetermined
        mProgressBar.setIndeterminateDrawable(new IndeterminateHorizontalProgressDrawable(this));
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }


    public void changeFragment(android.support.v4.app.Fragment fragment) {
        if (findViewById(R.id.fragment_container) != null) {
            mFragmentTransaction = mFragmentManager.beginTransaction();
            mFragmentTransaction.replace(R.id.fragment_container, fragment);
            mFragmentTransaction.commit();
            CommonData.mCurrentFragmentPostLogin = fragment;
        }
    }

    // posted from menu fragment initial 4 cases
    @Subscribe
    public void changingFragment(Integer id) {
        switch (id) {
            case R.id.linearLayout_home:
                mBackToMainScreen = true;
                break;

            case R.id.linearLayout_filter:
                mBackToMainScreen = true;
                break;

            case R.id.linearLayout_jobs:
                mBackToMainScreen = true;
                break;

            case R.id.linearLayout_settings:
                mBackToMainScreen = true;
                break;

            // when returning to settings
            case 9999:
                mBackToSettings = true;
                mBackToJobList = false;
                mBackToMainScreen = false;
                mBackToFilter =false;
                break;

            // when returning to job
            case 8888:
                mBackToJobList = true;
                mBackToSettings = false;
                mBackToMainScreen = false;
                mBackToFilter =false;
                break;

            // when returning to filter
            case 7777:
                mBackToFilter = true;
                mBackToSettings = false;
                mBackToJobList = false;
                mBackToMainScreen = false;
                break;

            // user clicks on some members from view pager
            case R.id.recycler_view:
                mBackToJobList = null;
                mBackToSettings = false;
                mBackToMainScreen = false;
                break;

            default :
                Log.d(TAG, " default case in switch encountered " + id + " recieved");
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //event bus registering
        mBus.register(this);


    }

    @Override
    protected void onPause() {
        super.onPause();

        //event bus unregistering
        mBus.unregister(this);

    }

    @Override
    public void onBackPressed() {
        if (!mBackToMainScreen) {
            mBackToMainScreen = true;
            if (mBackToSettings) {
                changeFragment(new FragmentSettings());
            } else if (mBackToJobList == null) {
                changeFragment(mFragmentMembers);
            } else if (mBackToJobList) {
                changeFragment(CommonData.fragmentJobs);
            } else if (CommonData.mCurrentFragmentPostLogin instanceof FragmentFilterResult){
                changeFragment(new FragmentFilter());
            }else if (mBackToFilter){
                changeFragment(new FragmentFilterResult());
            }

        } else
            this.finish();
    }

    @Subscribe
    public void setmProgressBar(Boolean isVisible) {
        if (isVisible){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else {
            mProgressBar.setVisibility(View.GONE);
            Log.d(TAG,"ProgressBar Hidden");
        }

    }


    @Subscribe
    public void changeVisibilityOfMenu(MenuVisibility visibility){

        Log.d(TAG, "Menu Visibility" + visibility.getState());


        if (!visibility.getState() && mFragmentMenu!=null){

            // HIDE menu
            mFragmentManager.beginTransaction()
                    .hide(mFragmentMenu)
                    .commit();

        } else if (visibility.getState() && mFragmentMenu!=null){

            // SHOW menu
            mFragmentManager.beginTransaction()
                    .show(mFragmentMenu)
                    .commit();

        }

    }
}
