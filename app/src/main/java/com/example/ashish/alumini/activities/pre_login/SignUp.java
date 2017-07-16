package com.example.ashish.alumini.activities.pre_login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ashish.alumini.R;
import com.example.ashish.alumini.activities.post_login.MainScreenActivity;
import com.example.ashish.alumini.application.MyApplication;
import com.example.ashish.alumini.network.ApiClient;
import com.example.ashish.alumini.network.pojo.SignupPartial;
import com.example.ashish.alumini.supporting_classes.GlobalBus;
import com.example.ashish.alumini.supporting_classes.GlobalPrefs;
import com.example.ashish.alumini.supporting_classes.ProgressBarVisibility;
import com.example.ashish.alumini.supporting_classes.RetrofitErrorHandler;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUp extends Activity {

    String TAG = getClass().getSimpleName();

    @Bind(R.id.editText_signup_email)
    EditText mEditTextemail;

    @Bind(R.id.editText_signup_name)
    EditText mEditTextName;

    @Bind(R.id.editText_signup_password)
    EditText mEditTextPassword;

    @Bind(R.id.button_signup)
    Button mButtonSignup;

    @Bind(R.id.linearLayout_signup)
    LinearLayout mRelativeLayout;

    int mBackCounter =0;

    MyApplication mApplication;

    GlobalBus mGlobalBus;

    // to post within the event bus
    ProgressBarVisibility barVisibility = new ProgressBarVisibility();



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        //global mGlobalBus registering
        mGlobalBus = GlobalBus.getInstance();

        // butterknife binding
        ButterKnife.bind(this);


        mApplication  = (MyApplication) getApplication();

    }

    //body of signUp method
    @OnClick(R.id.button_signup)
    public void signUp() {

        if (!validate()) {
            TastyToast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG,TastyToast.ERROR);
            onSignupFailed();
            return;
        }
        onSignUpSuccess();


//        startMainScreenActivity(null);
    }

    public void onSignUpSuccess() {
        mButtonSignup.setEnabled(true);


        // make API call
        makeserverCallToPostSignupPartialData();
    }

    public void onSignupFailed() {
        TastyToast.makeText(getBaseContext(), "Signup failed", Toast.LENGTH_LONG,TastyToast.ERROR);

        mButtonSignup.setEnabled(true);
    }

    //valide method
    public boolean validate() {
        boolean valid = true;

        String nameString = mEditTextName.getText().toString();
        String emailString = mEditTextemail.getText().toString();
        String passwordString = mEditTextPassword.getText().toString();

        if (nameString.isEmpty() || nameString.length() < 3) {
            mEditTextName.setError(getString(R.string.error_name));
            valid = false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            mEditTextemail.setError(getString(R.string.error_email));
            valid = false;
        }

        if ( passwordString.length() < 6 ) {
            mEditTextPassword.setError(getString(R.string.error_password));
            valid = false;
        }


        return valid;
    }


    public void makeserverCallToPostSignupPartialData(){
        // making button un clickable
        mButtonSignup.setEnabled(false);

        // making progress bar visible
        postHideSignal(true);

        
        Call<SignupPartial> call = ApiClient.getServerApi().signupPartial(
                mEditTextName.getText().toString().trim(),
                mEditTextemail.getText().toString().trim(),
                mEditTextPassword.getText().toString().trim());

        call.enqueue(new Callback<SignupPartial>() {
            @Override
            public void onResponse(Call<SignupPartial> call, Response<SignupPartial> response) {
                Log.d(TAG,"API successful");

                // global error handler for code 500
                RetrofitErrorHandler errorHandler = new RetrofitErrorHandler();
                errorHandler.statusCodeHandler(getBaseContext(),response.code());


                // hiding progress bar
                postHideSignal(false);

                // making button clickable
                mButtonSignup.setEnabled(true);

                if (response.code()==600){
                    TastyToast.makeText(getBaseContext(), "Email Already Exists", Toast.LENGTH_LONG,TastyToast.ERROR);
                }
                else if (response.code()==200){

                SignupPartial signupPart = response.body();

                /*
                * saving the name, id, email in shared prefs
                * */
                GlobalPrefs globalPrefs = new GlobalPrefs(getApplicationContext());
                globalPrefs.putString(getString(R.string.userid),signupPart.get_id());
                globalPrefs.putString(getString(R.string.username),signupPart.getName());
                globalPrefs.putString(getString(R.string.useremail),signupPart.getEmail());



                // function for creating list class to make server class and fetch data
                mApplication.createListCLass();

                /*
                * Start Activity main screen in which the fragments will be displayed
                * */
                startMainScreenActivityWithGetProfileData();

                }

               else
                {
                    TastyToast.makeText(getBaseContext(), "WTF!!"+response.code(), Toast.LENGTH_LONG,TastyToast.ERROR);
                }

            }

            @Override
            public void onFailure(Call<SignupPartial> call, Throwable t) {
                Log.d(TAG,"API failed");
                // hiding progress bar
                postHideSignal(false);

                // making button clickable
                mButtonSignup.setEnabled(true);

                TastyToast.makeText(getBaseContext(),"Signup Failed",TastyToast.LENGTH_SHORT,TastyToast.WARNING);

                Snackbar snackbar = Snackbar
                        .make(mRelativeLayout, "Can't connect to cloud", Snackbar.LENGTH_LONG);
                snackbar.show();

            }
        });
    }

    // function to start activity through intent
    public void startMainScreenActivityWithGetProfileData(){
        Intent intent=new Intent(SignUp.this,MainScreenActivity.class);
                /*
                * SIGNUP is sent beacuse when the login is successful,
                 * then from another session the login/signup will be skipped
                 * to change the fragment of signup, "SIGNUP" is used
                * */
        intent.putExtra("SIGNUP",true);

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        mBackCounter++;
        if (mBackCounter ==1){
            Snackbar snackbar = Snackbar
                    .make(mRelativeLayout, "Press Back again to exit", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        if (mBackCounter ==2 ){
            this.finish();
        }

    }
    public void postHideSignal( Boolean state){
        // mGlobalBus posting
        barVisibility.setVisibility(state);
        mGlobalBus.post(barVisibility);
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
}