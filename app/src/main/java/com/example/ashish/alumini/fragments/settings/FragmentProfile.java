package com.example.ashish.alumini.fragments.settings;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.ashish.alumini.BuildConfig;
import com.example.ashish.alumini.activities.post_login.PostLoginActivity;
import com.example.ashish.alumini.R;
import com.example.ashish.alumini.network.ApiClient;

import com.example.ashish.alumini.network.pojo.MemberInstance;
import com.example.ashish.alumini.supporting_classes.GlobalPrefs;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentProfile extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String  TAG = getClass().getSimpleName();



    MemberInstance mListInstance;

    /*
    * Butterknife
    * */
    @Bind(R.id.imageView_edit) ImageView mImageViewEdit;
    @Bind(R.id.textView_member_name) TextView mTextView_name;
    @Bind(R.id.textView_bio) TextView mTextView_bio;
    @Bind(R.id.textView_designation_n_CompanyName) TextView mTextViewDesignationNCompanyName;
    @Bind(R.id.textView_branch) TextView mTextViewBranch;
    @Bind(R.id.textView_homeLocation) TextView mTextViewHomeLocation;
    @Bind(R.id.editText_jobLocation) TextView mTextViewJobLocation;
    @Bind(R.id.textView_year) TextView mTextViewYear;

    @Bind(R.id.textView_contact) TextView mTextViewContact;
    @Bind(R.id.textView_mail) TextView mTextViewMail;
    @Bind(R.id.textView_fbLink) TextView mTextViewFb;

    @Bind(R.id.imageView_profilepic)
    CircleImageView mImageView;

    Bus mBus = new Bus();

    PostLoginActivity mActivity ;

    public FragmentProfile() {
        // Required empty public constructor
    }


    public static FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // butterknife binding
        ButterKnife.bind(this,view);

        //Bus Registering
        mBus.register(getActivity());

        // start server call to get complete data
        // this is case - when user opens my profile
        // hence no data is assigned from previous fragment
        if (mListInstance==null){

            // show the option to edit the profile
            mImageViewEdit.setVisibility(View.VISIBLE);

            // gettign if of loggedin user from shared prefs
            String id = new GlobalPrefs(getActivity()).getString(getString(R.string.userid));

            // make server call to get the remaining data of user
            makeServerCallToGetCompleteProfile(id);

            // starting picasso image loading
            Picasso.with(mImageView.getContext())
                    .load(ApiClient.BASE_URL + "upload/uploads/fullsize/" + id)
                    .placeholder(new IconicsDrawable(mImageView.getContext()).icon(FontAwesome.Icon.faw_user)
                            .color(Color.LTGRAY)
                            .sizeDp(70))
                    .into(mImageView);
        }





        // getting activity instance
        mActivity = (PostLoginActivity) getActivity();


        // prevent nulification
        if (mListInstance!=null){
            // hiding the edit button
            mImageViewEdit.setVisibility(View.GONE);

            // make server call to get more data with id param of clicked element
            makeServerCallToGetMoreData(mListInstance.get_id());

            // starting picasso image loading
            Picasso.with(mImageView.getContext())
                    .load(ApiClient.BASE_URL + "upload/uploads/fullsize/" + mListInstance.get_id())
                    .placeholder(new IconicsDrawable(mImageView.getContext()).icon(FontAwesome.Icon.faw_user)
                            .color(Color.LTGRAY)
                            .sizeDp(70))
                    .into(mImageView);

            // set the data which was transfered from previous recycler view though setData
            mTextView_name.setText(mListInstance.getName());
            mTextViewDesignationNCompanyName.setText(mListInstance.getDesignation());
            mTextViewJobLocation.setText(mListInstance.getWork());
            mTextViewYear.setText(mListInstance.getYear());
        }





        return view;
    }

    @OnClick(R.id.imageView_edit)
    public void editProfile(){
        mActivity.changeFragment(new FragmentEditProfile());
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onPause() {
        super.onPause();

        // progress bar visibility
        mBus.post(false);

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

    // getting the data of clicked members
    public void setData(MemberInstance data){
        mListInstance = data;

    }

    // whent the user will click on the recycler view in view pager to get more details about the user
    public void makeServerCallToGetMoreData( String id){

        // progress bar visibility
        mBus.post(true);

        Call<MemberInstance> call = ApiClient.getServerApi().
                getRemainingDataForRecyclerView(id);

        call.enqueue(new Callback<MemberInstance>() {
            @Override
            public void onResponse(Call<MemberInstance> call, Response<MemberInstance> response) {
                Log.d(TAG, "API call successful");
                MemberInstance instance = response.body();
                if (instance!=null){
                    mTextViewBranch.setText("Branch : " + instance.getBranch());
                    mTextViewDesignationNCompanyName.append( " at "+instance.getCompany());
                    mTextView_bio.setText(instance.getBio());
                    mTextViewContact.setText(instance.getPhone());
                    mTextViewHomeLocation.setText(instance.getHome());
                    mTextViewMail.setText(instance.getEmail());
                    mTextViewFb.setText(instance.getWeblink());
                }
//                if (response.code()==200){
                    // setting visivility of progress bar to GONE
                    mBus.post(false);
//                }
            }


            @Override
            public void onFailure(Call<MemberInstance> call, Throwable t) {
                Log.d(TAG, "API call failed " + t.toString());

                // hiding progress bar
                mBus.post(false);

                // display toast
                TastyToast.makeText(mActivity,"Can't communicate to server",500,TastyToast.ERROR);

                // and come to previous screen
                mActivity.onBackPressed();
            }
        });
    }

    // when the user clicks on my profile options from settings
    public void makeServerCallToGetCompleteProfile(String id){

        // progress bar visibility
        mBus.post(true);

        Call<MemberInstance> call = ApiClient.getServerApi().getCompleteProfileData(id);

        call.enqueue(new Callback<MemberInstance>() {
            @Override
            public void onResponse(Call<MemberInstance> call, Response<MemberInstance> response) {

                if (response.code()==200 && response.body()!=null){
                    setCompleteData(response.body());

                    // progress bar visibility
                    mBus.post(false);
                }

                if (BuildConfig.DEBUG){
                    Log.d(TAG, "API call successful makeServerCallToGetCompleteProfile");
                }
            }

            @Override
            public void onFailure(Call<MemberInstance> call, Throwable t) {
                if (BuildConfig.DEBUG){
                    Log.d(TAG, "API call failed makeServerCallToGetCompleteProfile" + t.toString());
                }

                // hiding progress bar
                mBus.post(false);

                // display toast
                TastyToast.makeText(mActivity,"Can't communicate to server",TastyToast.LENGTH_SHORT,TastyToast.ERROR);

                // back to previous screen
                mActivity.onBackPressed();
            }
        });
    }

    public void setCompleteData(MemberInstance completeData){
        mTextViewBranch.setText("Branch : " + completeData.getBranch());
        mTextViewDesignationNCompanyName.setText(completeData.getDesignation());
        mTextViewDesignationNCompanyName.append(" at "+completeData.getCompany());
        mTextView_bio.setText(completeData.getBio());
        mTextViewContact.setText(completeData.getPhone());
        mTextViewHomeLocation.setText(completeData.getHome());
        mTextViewMail.setText(completeData.getEmail());
        mTextView_name.setText(completeData.getName());
        mTextViewJobLocation.setText(completeData.getWork());
        mTextViewYear.setText(completeData.getYear());
        mTextViewFb.setText(completeData.getWeblink());


    }

}
