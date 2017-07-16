package com.example.ashish.alumini.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.ashish.alumini.BuildConfig;
import com.example.ashish.alumini.R;
import com.example.ashish.alumini.activities.post_login.MainScreenActivity;
import com.example.ashish.alumini.fragments.main_screen_fragments.FragmentMainScreen;
import com.example.ashish.alumini.network.ApiClient;
import com.example.ashish.alumini.supporting_classes.CommonData;
import com.example.ashish.alumini.supporting_classes.GlobalPrefs;
import com.example.ashish.alumini.supporting_classes.RetrofitErrorHandler;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.mikepenz.iconics.view.IconicsImageView;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.otto.Bus;

import java.io.File;
import java.io.IOException;

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


public class FragmentGetProfileData extends android.support.v4.app.Fragment {


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
    EditText mEditTextDesignation;
    @Bind(R.id.editText_company)
    EditText mEditTextCompany;

    @Bind(R.id.textView_memberDesignation)
    TextInputLayout mTextInputLayoutDesignation;
    // designation and company
    @Bind(R.id.textInputLayout_company)
    TextInputLayout mTextInputLayoutCompany;

    @Bind(R.id.imageView_companyLogo)
    IconicsImageView mImageView;

    @Bind(R.id.progress_wheel)
    ProgressWheel mProgressWheel;

    File mFile = null;

    // event bus registering
    Bus mBus = new Bus();

    // activities
    MainScreenActivity mActivity;

    private int PICK_IMAGE_REQUEST = 1;

    // for permission rationale
    MaterialDialog materialDialog;



    public FragmentGetProfileData() {
        // Required empty public constructor
    }


    public static FragmentGetProfileData newInstance(String param1, String param2) {
        FragmentGetProfileData fragment = new FragmentGetProfileData();
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

        ButterKnife.bind(this, view);


        // initialization of mPostLoginActivity
        if (getActivity() instanceof MainScreenActivity) {
            mActivity = (MainScreenActivity) getActivity();
        }

        // displaying the data which is strored in shared preference from previous page
        mEditTextName.setText(new GlobalPrefs(getContext()).getString(getString(R.string.username)));
        mEditTextEmail.setText(new GlobalPrefs(getContext()).getString(getString(R.string.useremail)));

        // runtime permission checker
        Dexter.initialize(mActivity);

        // setting progress bar color
        mProgressWheel.setBarColor(ContextCompat.getColor(mActivity,R.color.appTheme));


        // for permission rationale
        materialDialog = new MaterialDialog(mActivity);


        return view;
    }

    @OnClick(R.id.button_save_profile_data)
    public void saveDataHandler() {
        if (validate()) {
            makeServerCalltoPostCompleteData();
        }
    }

    @OnClick(R.id.checkBox_isNerd)
    public void textChangingOfEditText() {
        if (checkbox.isChecked()) {
            mTextInputLayoutDesignation.setHint("Course");
            mTextInputLayoutCompany.setHint("University / College");
        } else if (!checkbox.isChecked()) {
            mTextInputLayoutDesignation.setHint("Designation");
            mTextInputLayoutCompany.setHint("Organization");
        }
    }

    @OnClick(R.id.imageView_companyLogo)
    public void imagePickingFunction() {

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
                TastyToast.makeText(mActivity, "We need permission for uploading your photo",
                        TastyToast.LENGTH_SHORT, TastyToast.WARNING);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, final PermissionToken token) {
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
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /*
    * Post the edited text
    * */
    public void makeServerCalltoPostCompleteData() {
        // getting the id from shared preffernece which was stored during partial signup
        String id = new GlobalPrefs(getContext()).getString(getString(R.string.userid));


        Call<String> call = ApiClient.getServerApi().signupComplete(id,        // id
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

                // global error handler for code 500
                RetrofitErrorHandler errorHandler = new RetrofitErrorHandler();
                errorHandler.statusCodeHandler(getActivity(),response.code());

                // making progress bar invisible
                mBus.post(false);


                if (response.code() == 201) {
                    mActivity.changeFragment(new FragmentMainScreen());
                    TastyToast.makeText(getContext(), "Details Updated", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    // storing id and name in shared pref


                    GlobalPrefs globalPrefs = new GlobalPrefs(getActivity());
                    globalPrefs.putString(getString(R.string.username),
                            mEditTextName.getText().toString().trim()                     // name
                    );
                    new GlobalPrefs(getActivity()).putString(getString(R.string.useremail),
                            mEditTextEmail.getText().toString().trim()                     // email
                    );

                    // for session maintaining
                    globalPrefs.putBooloean(getString(R.string.is_logged_in), true);
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // making progress bar invisible
                mBus.post(false);

                if (BuildConfig.DEBUG){
                    Log.d(TAG, "API call failed makeServerCalltoPostCompleteData");
                }
            }
        });

    }

    public boolean validate() {


        if (mEditTextName.getText().toString().trim().length() <= 5) {
            mEditTextName.setError("Name must be greater than 5 characters");
            return false;
        } else if (mEditTextBio.getText().toString().trim().length() == 0) {
            mEditTextBio.setError("Make it a bit Big");
            return false;
        } else if (mEditTextDesignation.getText().toString().trim().length() == 0) {
            mEditTextDesignation.setError("Invalid Designation");
            return false;
        } else if (mEditTextCompany.getText().toString().trim().length() == 0) {
            mEditTextCompany.setError("Invalid Name");
            return false;
        }
        if (mEditTextLocationHome.getText().toString().trim().length() == 0) {
            mEditTextLocationHome.setError("Invalid Location");
            return false;
        } else if (mEditTextLocationWork.getText().toString().trim().length() == 0) {
            mEditTextLocationWork.setError("Invalid Location");
            return false;
        } else if (mEditTextPhone.getText().toString().trim().length() < 5 ||
                mEditTextPhone.getText().toString().trim().length() > 20) {
            mEditTextPhone.setError("Invalid Phone Number");
            return false;
        } else if (!Patterns.WEB_URL.matcher(mEditTextWebLink.getText()).matches()) {
            mEditTextWebLink.setError("Invalid Link Provided");
            return false;
        }

        // when all condition went wrong - i.e. every data is in correct format
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), uri);

                // getting file from uri
                mFile = new File(CommonData.getPath(mActivity,uri));

                // make server call to upload image
                makeServerCallToUploadImage();

                // set image to imageview
                mImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void makeServerCallToUploadImage() {


        mProgressWheel.spin();

        if (mFile==null){
            Log.d(TAG,"bhai null ho gaya ");

            mProgressWheel.stopSpinning();

            return;
        }

        //https://futurestud.io/tutorials/retrofit-2-how-to-upload-files-to-server

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), mFile);

        String path = new GlobalPrefs(mActivity).getString("Userid");

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", path, requestFile);

        Call<String> call = ApiClient.getServerApi().uploadProfileImage(body);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                // global error handler for code 500
                RetrofitErrorHandler errorHandler = new RetrofitErrorHandler();
                errorHandler.statusCodeHandler(getActivity(),response.code());

                if (BuildConfig.DEBUG){
                    Log.d(TAG, "Upload Successful makeServerCallToUploadImage");
                }
                TastyToast.makeText(mActivity, "Upload Successful", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);

                mProgressWheel.stopSpinning();

                mFile = null;

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

                if (BuildConfig.DEBUG){
                    Log.d(TAG, "Upload Failed makeServerCallToUploadImage" + t.getMessage());
                }


                TastyToast.makeText(mActivity, "Upload Failed", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                mProgressWheel.stopSpinning();

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        //Bus Registering
        mBus.register(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        //Bus Registering
        mBus.unregister(getActivity());
    }
}
