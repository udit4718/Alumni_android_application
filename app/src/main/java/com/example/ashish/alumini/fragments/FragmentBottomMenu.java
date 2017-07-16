package com.example.ashish.alumini.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ashish.alumini.activities.post_login.PostLoginActivity;
import com.example.ashish.alumini.R;
import com.example.ashish.alumini.fragments.filter_fragments.FragmentFilter;
import com.example.ashish.alumini.fragments.settings.FragmentSettings;
import com.example.ashish.alumini.supporting_classes.CommonData;
import com.mikepenz.iconics.view.IconicsImageView;
import com.squareup.otto.Bus;

import butterknife.Bind;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class FragmentBottomMenu extends Fragment {

    /*
    * GLOBAL DECLARATIONS & BUTTERKNIFE INJECTIONS
    *
    * */
    @Bind(R.id.linearLayout_home) LinearLayout mLinearLayoutHome;
    @Bind(R.id.linearLayout_filter) LinearLayout mLinearLayoutFilter;
    @Bind(R.id.linearLayout_jobs) LinearLayout mLinearLayoutJobs;
    @Bind(R.id.linearLayout_settings) LinearLayout mLinearLayoutSettings;

    @Bind(R.id.button_home) IconicsImageView mImageViewMembers;
    @Bind(R.id.button_filter) IconicsImageView mImageViewFilter;
    @Bind(R.id.button_jobs) IconicsImageView mImageViewJobs;
    @Bind(R.id.button_settings) IconicsImageView mImageViewSettings;

    View mLineViewPrevious;

    IconicsImageView mPreviousIconicsImageView;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    Bus mBus = new Bus();

    PostLoginActivity mActivity;

    ActionBar mActionBar;

//    FragmentMembers mFragmentMembers = new FragmentMembers();


    public FragmentBottomMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentBottomMenu.
     */
    public static FragmentBottomMenu newInstance(String param1, String param2) {
        FragmentBottomMenu fragment = new FragmentBottomMenu();
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
        View view = inflater.inflate(R.layout.fragment_menu_bottom, container, false);
        //Butterknife Binding
        ButterKnife.bind(this,view);
        //event bus registering
        mBus.register(getActivity());


        mActivity = (PostLoginActivity) getActivity();
        mActionBar = mActivity.getSupportActionBar();

        // getting default views as previous views - HorizontalBar
        mLineViewPrevious = view.findViewById(R.id.view_home);

        // Imageview for home
        mPreviousIconicsImageView = (IconicsImageView) view.findViewById(R.id.button_home);

        if (CommonData.mCurrentFragmentPostLogin instanceof FragmentMembers){
            setVisibleView(view.findViewById(R.id.view_home),mImageViewMembers);
        }
        else if (CommonData.mCurrentFragmentPostLogin instanceof FragmentFilter){
            setVisibleView(view.findViewById(R.id.view_filter), mImageViewFilter);
        }
        else if (CommonData.mCurrentFragmentPostLogin instanceof FragmentJobs){
            setVisibleView(view.findViewById(R.id.view_jobs),mImageViewJobs);
        }
        else if (CommonData.mCurrentFragmentPostLogin instanceof FragmentSettings){
            setVisibleView(view.findViewById(R.id.view_settings),mImageViewSettings);
        }

        return view;
    }

    // when home is clicked
    @OnClick(R.id.linearLayout_home)
    public void changeToHomeFragment(View view){

        setVisibleView(getView().findViewById(R.id.view_home),mImageViewMembers);

        mActivity.changeFragment(mActivity.mFragmentMembers);

        // post to Fragment Activity - PostLoginActivity to manage the backpressed action
        mBus.post(view.getId());
    }

    // when filter is clicked
    @OnClick(R.id.linearLayout_filter)
    public void changeToFilterFragment(View view){

        setVisibleView(getView().findViewById(R.id.view_filter),mImageViewFilter);

        mActivity.changeFragment(new FragmentFilter());

        // post to Fragment Activity - PostLoginActivity to manage the backpressed action
        mBus.post(view.getId());
    }

    // when jobs is clicked
    @OnClick(R.id.linearLayout_jobs)
    public void changeToJobsFragment(View view){

        setVisibleView(getView().findViewById(R.id.view_jobs),mImageViewJobs);

        mActivity.changeFragment(CommonData.fragmentJobs);



        // post to Fragment Activity - PostLoginActivity to manage the backpressed action
        mBus.post(view.getId());
    }

    // when setting is clicked
    @OnClick(R.id.linearLayout_settings)
    public void changeFragment(View view){

        setVisibleView(getView().findViewById(R.id.view_settings),mImageViewSettings);

        mActivity.changeFragment(new FragmentSettings());

        // post to Fragment Activity - PostLoginActivity to manage the backpressed action
        mBus.post(view.getId());
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
    * Making the view color focused - RED (appTheme in this case)
    * */
    public void setVisibleView(View horizontalLine,IconicsImageView imageView){
        //setting the previously clicked view to visibility=gone
        mLineViewPrevious.setVisibility(View.GONE);

        //setting the previously clicked Button to default
        mPreviousIconicsImageView.setColor(ContextCompat.getColor(mActivity,R.color.black_de));
        //changing color and visibility
        imageView.setColor(ContextCompat.getColor(mActivity,R.color.appTheme));
        horizontalLine.setVisibility(View.VISIBLE);

        //updating the previous elements for changing the visibility in next click
        mLineViewPrevious =horizontalLine;
        mPreviousIconicsImageView = imageView;

        mBus.post(imageView.getId()); // Posting the clicked layout to the BlankFragment activity (PostLoginActivity)
    }
}
