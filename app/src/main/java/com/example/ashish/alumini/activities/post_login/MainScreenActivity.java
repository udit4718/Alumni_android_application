package com.example.ashish.alumini.activities.post_login;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.ashish.alumini.R;
import com.example.ashish.alumini.fragments.main_screen_fragments.FragmentEvents;
import com.example.ashish.alumini.fragments.main_screen_fragments.FragmentMainScreen;
import com.example.ashish.alumini.fragments.FragmentGetProfileData;
import com.example.ashish.alumini.fragments.FragmentWebView;
import com.example.ashish.alumini.supporting_classes.CommonData;
import com.example.ashish.alumini.supporting_classes.GlobalPrefs;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.IndeterminateHorizontalProgressDrawable;


public class MainScreenActivity extends AppCompatActivity
{


    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    // for backpressed actions
    Fragment mCurrentFragment;
    Fragment mPreviousFragment;

    // event bus
    Bus mBus = new Bus();

    int mBackCounter;

    ActionBar mActionBar;

    @Bind(R.id.material_progressBar)
    ProgressBar mProgressBar;

    String TAG = getClass().getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);

        /*
        * check if user came after logout then finish it
        * on back press in login / signuop activity
        * */
        boolean b = new GlobalPrefs(this).getBoolean(getString(R.string.is_logged_in));
        if (!b && savedInstanceState!=null && !savedInstanceState.getBoolean("SIGNUP") ){

            this.finish();
        }

        setContentView(R.layout.activity_main_screen);


        //butterknife injection
        ButterKnife.bind(this);

        // getting action bar
        mActionBar = getSupportActionBar();

        // event bus
        mBus.register(this);

        // getting fragment manager
        mFragmentManager = getSupportFragmentManager();

        // checking if the activity is started from signup activity or splash activity
        Boolean isSignup = false;

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            // this means activity is started from signup activity
            isSignup = (Boolean) bundle.get("SIGNUP");
        }



        mFragmentTransaction = mFragmentManager.beginTransaction();

        if (savedInstanceState==null){
            if (isSignup){

                // show the getData fragment
                FragmentGetProfileData fragmentGetProfileData = new FragmentGetProfileData();

                mFragmentTransaction.add(R.id.container_main_screen, fragmentGetProfileData );

                // saving it to common data
                CommonData.mCurrentFragmentMainScreen = fragmentGetProfileData;

            }else {
                // condition to check if the user has come from logout button or not
                // if shared prefs will show college_logo logged out then finish the activity
                if (!new GlobalPrefs(this).getBoolean(getResources().getString(R.string.is_logged_in))){
                    this.finish();
                }
                //else show the min screen fragment
                FragmentMainScreen fragmentMainScreen = new FragmentMainScreen();

                mFragmentTransaction.add(R.id.container_main_screen,fragmentMainScreen);

                // saving it to common data
                CommonData.mCurrentFragmentMainScreen = fragmentMainScreen;
            }
            mFragmentTransaction.commit();

        }
        if (savedInstanceState!=null && CommonData.mCurrentFragmentMainScreen!= null){
            // case when activity is restarted
            changeFragment(CommonData.mCurrentFragmentMainScreen);
        }

        mCurrentFragment = new FragmentMainScreen();

        mProgressBar.setIndeterminateDrawable(new IndeterminateHorizontalProgressDrawable(this));
        mProgressBar.setVisibility(View.GONE);

        mActionBar.hide();
    }

    public void changeFragment(Fragment fragment){
        //updating the current fragment
        mPreviousFragment = mCurrentFragment;
        mCurrentFragment = fragment;

        // mainly for used while orientation
        CommonData.mCurrentFragmentMainScreen = fragment;

        // to change the titile in case of onCLick the event listView and web view was opened
        if (mPreviousFragment instanceof FragmentEvents && mCurrentFragment instanceof FragmentWebView){
            mActionBar.setTitle("About Event");
        }

        //changing the fragment
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.container_main_screen, fragment);
        mFragmentTransaction.commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home)
            onBackPressed();
        return true;
    }


    @Override
    public void onBackPressed() {
        if (mCurrentFragment instanceof FragmentMainScreen){
            TastyToast.makeText(getApplicationContext(),"Press back again to exit",Toast.LENGTH_SHORT,TastyToast.INFO);
            mBackCounter++;
            if (mBackCounter==2){
                // exit the application
                this.finish();
                System.exit(2);
            }
        }

        // case of list-->list clicked(i.e. web view)
        if (mPreviousFragment instanceof FragmentEvents && mCurrentFragment instanceof FragmentWebView){
            changeFragment(new FragmentEvents());
        }
        // if any fragment other than main screen
//        then return to the mainscreen fragment
        else if (!(mCurrentFragment instanceof FragmentMainScreen)){
            changeFragment(new FragmentMainScreen());

        }
    }

    @Subscribe
    public void changeProgressBar(Boolean a){
        Log.d(TAG,"bus recieved value " +a.toString());
        if (a){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else
        mProgressBar.setVisibility(View.GONE);
    }
}

