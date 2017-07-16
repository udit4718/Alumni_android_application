package com.example.ashish.alumini.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ashish.alumini.BuildConfig;
import com.example.ashish.alumini.R;
import com.example.ashish.alumini.activities.post_login.PostLoginActivity;
import com.example.ashish.alumini.network.ApiClient;
import com.example.ashish.alumini.network.pojo.JobListInstance;
import com.example.ashish.alumini.network.pojo.JobDetail;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.view.IconicsImageView;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentJobDetails extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    String TAG = getClass().getSimpleName();


    private JobListInstance mJobListInstance;

    /*
    * Butterknife
    * */
    @Bind(R.id.imageView_companyImage)
    IconicsImageView imageView_companyImage;
    @Bind(R.id.textView_companyName_jobDetails) TextView mTextViewName;
    @Bind(R.id.textView_location_jobDetails) TextView mTextViewLocation;
    @Bind(R.id.textView_designation_jobDetails) TextView mTextViewJobDesignation;
    @Bind(R.id.textView_jobDescription) TextView mTextViewJobDescription;
    @Bind(R.id.textView_email_jobDetails) TextView mTextViewemail;
    @Bind(R.id.textView_website) TextView mTextViewWebsite;
    @Bind(R.id.textView_postedby) TextView mTextViewPostedBy;

    Bus mBus = new Bus();

    PostLoginActivity mActivity = (PostLoginActivity) getActivity();

    public FragmentJobDetails() {
        // Required empty public constructor
    }

    public static FragmentJobDetails newInstance(Object param1, String param2) {
        FragmentJobDetails fragment = new FragmentJobDetails();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1.toString());
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
        View view = inflater.inflate(R.layout.fragment_job_details, container, false);

        ButterKnife.bind(this,view);

        // show that data which was recieved by the list
        showData();

        //Bus Registering
        mBus.register(getActivity());


        // make server call when bus is registered
        makeServerCallToGetRemainingData();

        return view;
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
    * For setting the data passed from the list
    * */
    public void setData(JobListInstance item){
        mJobListInstance = item;
    }


    public void showData(){
        /*
        * setting the text to the text fields
        * data was recieved by the list directly
        * */
        mTextViewName.setText(mJobListInstance.getName());
        mTextViewLocation.setText(mJobListInstance.getLocation());
        mTextViewJobDesignation.setText(mJobListInstance.getRole());
        String imageUrl = ApiClient.BASE_URL + "upload/uploads/fullsize/"+
                mJobListInstance.getPostedbyid() + "-job";

        // picasso image loading
        Picasso.with(getActivity())
                .load(imageUrl)
                .placeholder(new IconicsDrawable(getContext()).icon(FontAwesome.Icon.faw_cloud_download)
                        .color(Color.LTGRAY)
                        .sizeDp(70))
                .error(new IconicsDrawable(getContext()).icon(FontAwesome.Icon.faw_cloud_download)
                        .color(Color.LTGRAY)
                        .sizeDp(70))
                .into(imageView_companyImage);


    }
    public void makeServerCallToGetRemainingData(){

//        making progress bar visible
        mBus.post(true);

        Call<JobDetail> call = ApiClient.getServerApi().getJobDetails(mJobListInstance.get_id());

        call.enqueue(new Callback<JobDetail>() {
            @Override
            public void onResponse(Call<JobDetail> call, Response<JobDetail> response) {

                //making progress bar invisible
                mBus.post(false);

                if (BuildConfig.DEBUG){
                    Log.d(TAG,"API cal Successful makeServerCallToGetRemainingData");
                }
                JobDetail jobDetail = response.body();

                mTextViewWebsite.setText(jobDetail.getContactweb());
                mTextViewemail.setText(jobDetail.getContactemail());
                mTextViewJobDescription.setText("Job Description : " + jobDetail.getKahani());
                mTextViewPostedBy.setText("Posted By : " + jobDetail.getPostedby());


            }

            @Override
            public void onFailure(Call<JobDetail> call, Throwable t) {
                //making progress bar invisible
                mBus.post(false);
                if (getActivity()!=null){
                    TastyToast.makeText(getActivity(), "Failed to connect", TastyToast.LENGTH_SHORT,
                            TastyToast.ERROR);
                    getActivity().onBackPressed();
                }

                if (BuildConfig.DEBUG){
                    Log.d(TAG,"API cal Failed" + t.toString());
                }
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();



    }

    @Override
    public void onPause() {
        super.onPause();

        //making progress bar invisible
        mBus.post(false);



    }
}
