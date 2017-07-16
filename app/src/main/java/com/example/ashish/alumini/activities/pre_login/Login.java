package com.example.ashish.alumini.activities.pre_login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.ashish.alumini.activities.post_login.MainScreenActivity;
import com.example.ashish.alumini.R;
import com.example.ashish.alumini.application.MyApplication;
import com.example.ashish.alumini.network.ApiClient;
import com.example.ashish.alumini.network.pojo.LoginResponse;
import com.example.ashish.alumini.supporting_classes.GlobalBus;
import com.example.ashish.alumini.supporting_classes.GlobalPrefs;
import com.example.ashish.alumini.supporting_classes.ProgressBarVisibility;
import com.example.ashish.alumini.supporting_classes.RetrofitErrorHandler;
import com.sdsmdg.tastytoast.TastyToast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends Activity {

    String TAG = getClass().getSimpleName();

    @Bind(R.id.editText_login_email)
    EditText email;

    EditText password;

    @Bind(R.id.button_login)
    Button loginButton;



    // for snack bar
    RelativeLayout mRelativeLayout;
    int mCounter=0;

    MyApplication mApplication;

    GlobalBus mGlobalBus ;


     ProgressBarVisibility barVisibility = new ProgressBarVisibility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // butterknife binding
        ButterKnife.bind(this);

        mGlobalBus= GlobalBus.getInstance();

        // view binding
        password=(EditText)findViewById(R.id.editText_login_password);


        mRelativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout_login);

        // getting application instance
        mApplication = (MyApplication) getApplication();



    }

    // forget password
    @OnClick(R.id.textView_forgetpasswd)
    public void forgetPasswordListener(){


        final String emailString = email.getText().toString().trim();


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText()).matches()){
            TastyToast.makeText(Login.this,"Invalid email",Toast.LENGTH_SHORT,TastyToast.INFO);
            return;
        }


        loginButton.setEnabled(false);

        // confirming from user to reset
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setTitle("Reset Password")
                .setMessage(getString(R.string.forget_password_prompt))
                .setPositiveButton("Reset", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        materialDialog.dismiss();

                        makeServerCallToResetPassword(emailString);

                    }
                })
                .setCanceledOnTouchOutside(true)
                .show();



    }

    //body of login method
    @OnClick(R.id.button_login)
    public void login() {

        if (!validate()) {
            onLoginFailed();
            return;
        }


        loginButton.setEnabled(false);



        String emailString = email.getText().toString().trim();
        String password = this.password.getText().toString().trim();


      makeServerCallToLogin(emailString, password);

    }



    public void onLoginSuccess(Intent intent) {


        this.finish();
        loginButton.setEnabled(true);
        startActivity(intent);

    }

    public void onLoginFailed() {
        TastyToast.makeText(getApplicationContext(),"Login Failed",TastyToast.LENGTH_LONG,TastyToast.ERROR);
    }

    public boolean validate() {
        boolean valid = true;

        String emailString = email.getText().toString().trim();
        String passwordString = password.getText().toString().trim();

        if (emailString.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailString).matches()) {
            email.setError("enter a valid email address");
            valid = false;
        } else {
            email.setError(null);
        }

        if (passwordString.isEmpty() || passwordString.length() < 6 || passwordString.length() > 30) {
            password.setError("Enter a strong password");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }


    @Override
    public void onBackPressed() {
        mCounter++;
        if (mCounter==1){
            Snackbar snackbar = Snackbar
                    .make(mRelativeLayout, "Press Back again to exit", Snackbar.LENGTH_LONG);
            snackbar.show();
        }

        if (mCounter==2){
            this.finish();
            System.exit(2);
        }

    }
    public void makeServerCallToLogin(String email, String password){

        //
        postHideSignal(true);

        Call<LoginResponse> call = ApiClient.getServerApi().login(email, password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Log.d(TAG, "API successful");

                //hide the progress bar
                postHideSignal(false);

                //making button clickable
                loginButton.setEnabled(true);

                // global error handler for code 500
                RetrofitErrorHandler errorHandler = new RetrofitErrorHandler();
                errorHandler.statusCodeHandler(getBaseContext(),response.code());


                if (response.code()==200 ){
                    TastyToast.makeText(getBaseContext(),"Successful",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                    LoginResponse loginResponse = response.body();

                    GlobalPrefs globalPrefs = new GlobalPrefs(getApplicationContext());
                    if (loginResponse!=null){
                        // storing id and name in shared pref
                        globalPrefs.putString(getString(R.string.userid),loginResponse.get_id());
                        globalPrefs.putString(getString(R.string.username),loginResponse.getName());
                    }

                    // function for creating list class to make server calls and fetch data
                    mApplication.createListCLass();

                    // intent to start new activity
                    Intent intent=new Intent(Login.this,MainScreenActivity.class);


                    if (loginResponse.getWork()!=null){
                        // for maintaining session
                        globalPrefs.putBooloean(getString(R.string.is_logged_in),true);

                    }
                    else if (loginResponse.getWork()==null){
                        // now MainScreenActivity will handle this
                        intent.putExtra("SIGNUP",true);
                    }
                    //function to change activity and
                    onLoginSuccess(intent);


                }
                else if (response.code()==600){ // 600 is generated by server side
                    TastyToast.makeText(getBaseContext(),"User not found",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                }
                else if (response.code()==700){ //700 is generated by server side
                    TastyToast.makeText(getBaseContext(),"Password not Matched",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
                }

            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {

                // making progress bar visibility handled at mainActivity
                postHideSignal(false);

                Log.d(TAG, "API failed makeServerCallToLogin");

                // show toast
                TastyToast.makeText(getBaseContext(),"Login Failed",TastyToast.LENGTH_SHORT,TastyToast.WARNING);

                // snack bar as well
                Snackbar snackbar = Snackbar
                        .make(mRelativeLayout, "Can't connect to cloud", Snackbar.LENGTH_LONG);
                snackbar.show();

                // allow user to use button
                loginButton.setEnabled(true);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();

        // unregistering event bus
        mGlobalBus.unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // registering global bus
        mGlobalBus.register(this);

    }

    public void postHideSignal(Boolean state){
        // mGlobalBus posting
        barVisibility.setVisibility(state);
        // post the event
        mGlobalBus.post(barVisibility);
    }

    public void makeServerCallToResetPassword( String emailString){

        // progress bar visibility
        postHideSignal(true);

        Call<String> call = ApiClient.getServerApi().resetPassword(emailString);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                // global error handler for code 500
                RetrofitErrorHandler errorHandler = new RetrofitErrorHandler();
                errorHandler.statusCodeHandler(getBaseContext(),response.code());

                TastyToast.makeText(Login.this,response.body(),Toast.LENGTH_SHORT,TastyToast.INFO);

                // progress bar visibility
                postHideSignal(false);

                loginButton.setEnabled(true);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Snackbar snackbar = Snackbar
                        .make(mRelativeLayout, "Can't connect to cloud", Snackbar.LENGTH_LONG);
                snackbar.show();

                // progress bar visibility
                postHideSignal(false);


                loginButton.setEnabled(true);
            }
        });

    }

}