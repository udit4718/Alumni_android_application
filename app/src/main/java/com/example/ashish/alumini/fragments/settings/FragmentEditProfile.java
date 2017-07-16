package com.example.ashish.alumini.fragments.settings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.ashish.alumini.R;
import com.example.ashish.alumini.activities.post_login.PostLoginActivity;
import com.example.ashish.alumini.network.ApiClient;
import com.example.ashish.alumini.network.pojo.MemberInstance;
import com.example.ashish.alumini.supporting_classes.CommonData;
import com.example.ashish.alumini.supporting_classes.GlobalPrefs;
import com.example.ashish.alumini.supporting_classes.MenuVisibility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentEditProfile extends android.support.v4.app.Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    String TAG = getClass().getSimpleName();

    /*
    * Butterknife
    * */
    @Bind(R.id.button_save_profile_data)
    Button mButtonSave;

    //contacts
    @Bind(R.id.editText_member_email)
    EditText mEditTextEmail;
    @Bind(R.id.editText_memberPhone)
    EditText mEditTextPhone;
    @Bind(R.id.editText_memberWebLink)
    EditText mEditTextWebLink;

    //location
    @Bind(R.id.editText_locationHome)
            EditText mEditTextLocationHome;
    @Bind(R.id.editText_locationWork)
           EditText mEditTextLocationWork;

    //Spinners
    @Bind(R.id.spinner_branch)
    Spinner mSpinnerBranch;
    @Bind(R.id.spinner_year)
    Spinner mSpinnerYear;


    //checkBox
    @Bind(R.id.checkBox_isNerd)
    CheckBox checkbox;

    // name and bio
    @Bind(R.id.editText_memberName)
            EditText mEditTextName;
    @Bind(R.id.editText_memberBio)
    EditText mEditTextBio;

    // designation and company EditTexts
    @Bind(R.id.editText_Designation)
        TextInputEditText mEditTextDesignation;
    @Bind(R.id.editText_company)
        EditText mEditTextCompany;

    @Bind(R.id.textView_memberDesignation)
    TextInputLayout mTextInputLayoutDesignation;
    // designation and company
    @Bind(R.id.textInputLayout_company)
        TextInputLayout mTextInputLayoutCompany;

    @Bind(R.id.imageView_companyLogo)
    ImageView mImageView;

    @Bind(R.id.progress_wheel)
    ProgressWheel mProgressWheel;

    File mFile = null;

    String stringArrayBranch[];
    String stringArrayYear[];
    ArrayList<String> mArrayListBranch = new ArrayList<>();
    ArrayList<String> mArrayListYear = new ArrayList<>();

    // event bus registering
    Bus mBus = new Bus();

    // activities
    PostLoginActivity mActivity;

    // for permission rationale
    MaterialDialog materialDialog;


    public FragmentEditProfile() {
        // Required empty public constructor
    }


    public static FragmentEditProfile newInstance(String param1, String param2) {
        FragmentEditProfile fragment = new FragmentEditProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_getprofiledata, container, false);

        // butterknife injections
        ButterKnife.bind(this,view);
        //Bus Registering
        mBus.register(getActivity());

        // getting instance of activity
        mActivity = (PostLoginActivity) getActivity();

        // runtime permission checker
        Dexter.initialize(mActivity);

        // setting progress bar color
        mProgressWheel.setBarColor(ContextCompat.getColor(mActivity,R.color.appTheme));

        // for permission rationale
        materialDialog = new MaterialDialog(mActivity);


        // getting id from shared pref and initiating api call
        String id = new GlobalPrefs(getActivity()).getString(getString(R.string.userid));
        makeServerToGetCompleteData(id);

        // starting picasso image loading
        Picasso.with(mImageView.getContext())
                .load(ApiClient.BASE_URL + "upload/uploads/fullsize/" + id)
                .placeholder(new IconicsDrawable(mImageView.getContext()).icon(FontAwesome.Icon.faw_user)
                        .color(Color.LTGRAY)
                        .sizeDp(70))
                .into(mImageView);

//        fetching the String array and converting to ArrayList
        stringArrayBranch =  getResources().getStringArray(R.array.branch_array);
        stringArrayYear =  getResources().getStringArray(R.array.year_array);

        for ( String a : stringArrayBranch){
            mArrayListBranch.add(a);
        }
        for ( String a : stringArrayYear){
            mArrayListYear.add(a);
        }





        return view;
    }

    @OnClick(R.id.button_save_profile_data)
    public void saveDataHandler(){
        if (validate()){
            makeServerCalltoPostCompleteData();
        }
    }

    @OnClick(R.id.checkBox_isNerd)
    public void checkBoxStateHandler(){
        if (checkbox.isChecked()){
            mTextInputLayoutDesignation.setHint("Course");
            mTextInputLayoutCompany.setHint("University / College");
        }
        else if (!checkbox.isChecked()){
            mTextInputLayoutDesignation.setHint("Designation");
            mTextInputLayoutCompany.setHint("Organization");
        }
    }

    @OnClick(R.id.imageView_companyLogo)
    public void imageUploading(){

        if (mFile!=null){
            makeServerCallToUploadImage();
            return;
        }

        Dexter.checkPermission(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse response) {
                Log.d(TAG, "Permission granted");
                Intent intent = new Intent();
                // Show only images, no videos or anything else
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                // Always show the chooser (if there are multiple options available)
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse response) {
                Log.d(TAG, "Permission denied");

            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission,final
                                                           PermissionToken token) {
                Log.d(TAG, "Permission RATIONALE");


                materialDialog.setTitle("Permission Alert")
                        .setMessage("We just need permission to access image file to upload")
                        .setPositiveButton("Proceed", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                materialDialog.dismiss();

                                token.continuePermissionRequest();

                            }
                        })
                        .setCanceledOnTouchOutside(true)
                        .show();

            }
        }, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1
//                && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), uri);

                mFile = new File(CommonData.getPath(mActivity,uri));


                makeServerCallToUploadImage( );
                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void makeServerCallToUploadImage( ){

        mProgressWheel.spin();




        if (mFile==null){
            Log.d(TAG,"bhai null ho gaya ");

            mProgressWheel.stopSpinning();


            return;
        }

        //https://futurestud.io/tutorials/retrofit-2-how-to-upload-files-to-server

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), mFile);

        String userid = new GlobalPrefs(mActivity).getString("Userid");

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", userid , requestFile);

        // description in form of multipart
//        String descriptionString = "hello, this is description speaking";
//        RequestBody description =
//                RequestBody.create(
//                        MediaType.parse("multipart/form-data"), descriptionString);


        Call<String> call = ApiClient.getServerApi().uploadProfileImage(body);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "Upload Successful");

                // show toast
                TastyToast.makeText(mActivity,"Upload Successful",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);

                mProgressWheel.stopSpinning();

                mFile = null;
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Upload Failed" + t.getMessage());

                // show toast
                TastyToast.makeText(mActivity,"Upload Failed! Tap Image to retry",TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                mProgressWheel.stopSpinning();
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();

        // make menu invisible
        mBus.post(new MenuVisibility(false));

    }

    @Override
    public void onPause() {
        super.onPause();

        // hiding progress bar
        mBus.post(false);

        // make menu visible
        mBus.post(new MenuVisibility(true));

        // unregistering event bus
        mBus.unregister(this);



    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /**
    * function is callled whren button is pressed
    * */
    public void makeServerCalltoPostCompleteData(){
        // making the progrss bar visible
        mBus.post(true);


        // getting the id from shared preffernece which was stored during partial signup
        String id = new GlobalPrefs(getContext()).getString(getString(R.string.userid));


        Call<String> call = ApiClient.getServerApi().signupComplete(id,        //id
                mEditTextName.getText().toString().trim(),                     // name
                mEditTextBio.getText().toString().trim(),                      // bio
                mSpinnerBranch.getSelectedItem().toString().trim(),            // branch
                mSpinnerYear.getSelectedItem().toString().trim(),              // year
                checkbox.isChecked(),                                          // isNerd
                mEditTextDesignation.getText().toString().trim(),              // designation
                mEditTextCompany.getText().toString().trim(),                  // company
                mEditTextLocationHome.getText().toString().trim(),             // home location
                mEditTextLocationWork.getText().toString().trim(),             // work location
                mEditTextPhone.getText().toString().trim(),                    // phone
                mEditTextWebLink.getText().toString().trim()                  // web
                );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                // hiding the progrss bar
                mBus.post(false);

                // 201 because it is send by the back end fo a successfull call
                if (response.code()==201){

                    mActivity.changeFragment(new FragmentProfile());

                    // storing id and name in shared pref
                    // globalPrefs.putString(getString(R.string.userid),response1.get_id());
                    new GlobalPrefs(getActivity()).putString(getString(R.string.username),
                            mEditTextName.getText().toString().trim()                     // name
                    );
                    new GlobalPrefs(getActivity()).putString(getString(R.string.useremail),
                            mEditTextEmail.getText().toString().trim()                     // email
                    );

                    // show toast
                    TastyToast.makeText(getContext(),"Details Updated",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                // hiding progress bar
                mBus.post(false);

                Log.d(TAG, "API call failed makeServerCalltoPostCompleteData");
            }
        });

    }

    public boolean validate(){


        if (mEditTextName.getText().toString().trim().length()<=6){
            mEditTextName.setError("Name must be greater than 6 characters");
            return false;
        }
         if (mEditTextBio.getText().toString().trim().length()==0){
            mEditTextBio.setError("Make it a little Big");
            return false;
        }
         if (mEditTextDesignation.getText().toString().trim().length()==0){
            mEditTextDesignation.setError("Invalid Designation");
            return false;
        }
         if (mEditTextCompany.getText().toString().trim().length()==0){
            mEditTextCompany.setError("Invalid Designation");
            return false;
        }
        if (mEditTextLocationHome.getText().toString().trim().length()==0){
            mEditTextLocationHome.setError("Invalid Location");
            return false;
        }
        if (mEditTextLocationWork.getText().toString().trim().length()==0){
            mEditTextLocationWork.setError("Invalid Location");
            return false;
        }
        if (mEditTextPhone.getText().toString().trim().length() <= 5){
            mEditTextPhone.setError("Invalid Phone Number");
            return false;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEditTextEmail.getText()).matches()){
            mEditTextEmail.setError("Invalid email");
            return false;
        }

        return true;
    }

    /*
    * Method to fetch values from server when the user will press edit option
    * */
    public void makeServerToGetCompleteData(String id){

        // making progress bar visible
        mBus.post(true);


        Call<MemberInstance> call = ApiClient.getServerApi().getCompleteProfileData(id);

        call.enqueue(new Callback<MemberInstance>() {
            @Override
            public void onResponse(Call<MemberInstance> call, Response<MemberInstance> response) {

                if (response.code()==200 && response.body()!=null){
                    setCompleteData(response.body());

                    // hiding progress bar
                   mBus.post(false);
                }
                Log.d(TAG, "API call successful");
            }

            @Override
            public void onFailure(Call<MemberInstance> call, Throwable t) {
                Log.d(TAG, "API call failed makeServerToGetCompleteData");
                // hiding progress bar
                mBus.post(false);
            }
        });
    }

    public void setCompleteData(MemberInstance completeData){
        mEditTextName.setText(completeData.getName());                     // name
        mEditTextBio.setText(completeData.getBio());                      // bio
        mSpinnerBranch.setSelection(
                mArrayListBranch.indexOf(completeData.getBranch()));          // branch
        mSpinnerYear.setSelection(
                mArrayListYear.indexOf(completeData.getBranch()));            // year
        checkbox.setChecked(completeData.getIsNerd());                    // isNerd
        //updating the hint according to checkBox state
        checkBoxStateHandler();
        mEditTextDesignation.setText(completeData.getDesignation());       // designation
        mEditTextCompany.setText(completeData.getCompany());               // company
        mEditTextLocationHome.setText(completeData.getHome());             // home location
        mEditTextLocationWork.setText(completeData.getWork());             // work location
        mEditTextPhone.setText(completeData.getPhone());                    // phone
        mEditTextWebLink.setText(completeData.getWeblink());                // web link
        mEditTextEmail.setText(completeData.getEmail());                    // email

    }

}
