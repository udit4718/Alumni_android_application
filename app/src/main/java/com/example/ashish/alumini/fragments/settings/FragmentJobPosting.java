package com.example.ashish.alumini.fragments.settings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ashish.alumini.R;
import com.example.ashish.alumini.activities.post_login.PostLoginActivity;
import com.example.ashish.alumini.network.ApiClient;
import com.example.ashish.alumini.supporting_classes.CommonData;
import com.example.ashish.alumini.supporting_classes.GlobalPrefs;
import com.example.ashish.alumini.supporting_classes.MenuVisibility;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.pnikosis.materialishprogress.ProgressWheel;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.otto.Bus;

import java.io.File;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentJobPosting extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String TAG = getClass().getSimpleName();


    /*
    * Butterknife
    * */
    @Bind(R.id.editText_companyName)
    TextInputEditText mInputEditTextCompanyName;

    @Bind(R.id.editText_designation)
    TextInputEditText mInputEditTextRole;

    @Bind(R.id.editText_Designation)
    TextInputEditText mInputEditTextJobDescription;

    @Bind(R.id.editText_jobLocation)
    TextInputEditText mInputEditTextJobLocation;

    @Bind(R.id.editText_companyWebLink)
    EditText mEditTextWebLink;

    @Bind(R.id.editText_member_email)
    EditText mEditTextemail;

    @Bind(R.id.imageView_companyLogo)
    ImageView mImageView;

    @Bind(R.id.progress_wheel)
    ProgressWheel mProgressWheel;

    File mFile = null;

    Bus mBus = new Bus();

    PostLoginActivity mActivity;

    public FragmentJobPosting() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentJobPosting newInstance(String param1, String param2) {
        FragmentJobPosting fragment = new FragmentJobPosting();
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
        View view = inflater.inflate(R.layout.fragment_post_job, container, false);

        ButterKnife.bind(this,view);

        mActivity = (PostLoginActivity) getActivity();

        //Bus Registering
        mBus.register(getActivity());

        Dexter.initialize(mActivity);

        // setting progress bar color
        mProgressWheel.setBarColor(ContextCompat.getColor(mActivity,R.color.appTheme));


        return view;
    }

    @OnClick(R.id.button_post_job)
    public void buttonPostJobHandler(){
        collectData();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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

        // make menu visible
        mBus.post(new MenuVisibility(true));



    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public void collectData(){
        /*
        * Function will validate the data and then make the server call
        * */
        boolean validDeta = false;
        if (mInputEditTextCompanyName.getText().toString().isEmpty()){
            mInputEditTextCompanyName.setError("Invalid Name");
        }
        else if (mInputEditTextRole.getText().toString().isEmpty()){
            mInputEditTextRole.setError("Empty Fields");
        }
        else if (mInputEditTextJobDescription.getText().toString().length()<6){
            mInputEditTextJobDescription.setError("Invalid details");
        }
        else if (mInputEditTextJobLocation.getText().toString().length()<3){
            mInputEditTextJobLocation.setError("Invalid details");
        }
        else if (!Patterns.WEB_URL.matcher(mEditTextWebLink.getText().toString()).matches()){
            mEditTextWebLink.setError("Invalid WebLink");
        }
        else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(mEditTextemail.getText()).matches()){
            mEditTextemail.setError("Invalid Email address");
        }
        else {
            validDeta = true;
        }


        if (validDeta){
            makeServerCallToPostData();
        }
    }

    public void makeServerCallToPostData(){
        // show progress bar
        mBus.post(true);

        String imageName = null;
        if (mFile!=null){
            imageName = mFile.getName();
        }

        Call<String> call = ApiClient.getServerApi().postJob(
                imageName,
                mInputEditTextCompanyName.getText().toString().trim(),
                mInputEditTextRole.getText().toString().trim(),
                mInputEditTextJobDescription.getText().toString().trim(),
                mInputEditTextJobLocation.getText().toString().trim(),
                mEditTextWebLink.getText().toString().trim(),
                mEditTextemail.getText().toString().trim(),
                new GlobalPrefs(getActivity()).getString(getString(R.string.username)),
                new GlobalPrefs(getActivity()).getString(getString(R.string.userid))
        );

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // hiding progress bar
                mBus.post(false);

                Log.d(TAG,"Successful");
                /*
                * Show toast
                * */
                TastyToast.makeText(getActivity(),"Posted! yeah",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);
                /*
                * Clear all the textFields
                * */
                cleardata();

                mActivity.changeFragment(new FragmentSettings());

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("API Call ","Failed");
                /*
                * only show toast for now
                * */
                TastyToast.makeText(getActivity(),"Posting Failed",TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                // hiding progress bar
                mBus.post(false);
            }
        });

    }

    public void cleardata(){
        mInputEditTextCompanyName.setText("");
                mInputEditTextRole.setText("");
                mInputEditTextJobDescription.setText("");
                mInputEditTextJobLocation.setText("");
                mEditTextWebLink.setText("");
                mEditTextemail.setText("");

    }


    @OnClick(R.id.imageView_companyLogo)
    public void imageGettingFunction(){

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
            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                Log.d(TAG, "Permission RATIONALE");
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

                // getting file from uri
                mFile = new File(CommonData.getPath(mActivity,uri));

                makeServerCallToUploadImage();

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
            return;
        }

        //https://futurestud.io/tutorials/retrofit-2-how-to-upload-files-to-server

        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), mFile);

        String userid = new GlobalPrefs(mActivity).getString("Userid");

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", userid + "-job", requestFile);

        String descriptionString = "hello, this is description speaking";
        RequestBody description =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), descriptionString);


        Call<String> call = ApiClient.getServerApi().uploadJobImage(body);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "Upload Successful");

                TastyToast.makeText(mActivity,"Upload Successful",TastyToast.LENGTH_SHORT,TastyToast.SUCCESS);

                // make the progress wheel stop spinning
                mProgressWheel.stopSpinning();

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Upload Failed" + t.toString());

                TastyToast.makeText(mActivity,"Upload Failed",TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                // make the progress wheel stop spinning
                mProgressWheel.stopSpinning();


            }
        });
    }


}
