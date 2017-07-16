package com.example.ashish.alumini.fragments.main_screen_fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ashish.alumini.R;
import com.example.ashish.alumini.activities.post_login.MainScreenActivity;
import com.example.ashish.alumini.activities.post_login.PostLoginActivity;
import com.example.ashish.alumini.fragments.FragmentWebView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.otto.Bus;

import at.markushi.ui.CircleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class FragmentMainScreen extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String TAG = getClass().getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    /*
    * Butterknife
    * */
    @Bind(R.id.circleButton_about)
    CircleButton mCirecularButtonAbout;

    @Bind(R.id.circleButton_event)
    CircleButton mCirecularButtonEvents;

    @Bind(R.id.circleButton_member)
    CircleButton mCirecularButtonMembers;

    Bus mBus = new Bus();

    MainScreenActivity mActivity ;

    public FragmentMainScreen() {
        // Required empty public constructor
    }


    public static FragmentMainScreen newInstance(String param1, String param2) {
        FragmentMainScreen fragment = new FragmentMainScreen();
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
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        mActivity = (MainScreenActivity) getActivity();

        // hiding action bar
        mActivity.getSupportActionBar().hide();

        ButterKnife.bind(this,view);
        //Bus Registering
//        mBus.register(getActivity());


        // setting icons to the circular buttons
        mCirecularButtonMembers.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(FontAwesome.Icon.faw_users)
                .color(Color.WHITE)
                .sizeDp(25));

        mCirecularButtonAbout.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(FontAwesome.Icon.faw_info)
                .color(Color.WHITE)
                .sizeDp(25));

        mCirecularButtonEvents.setImageDrawable(new IconicsDrawable(getActivity())
                .icon(FontAwesome.Icon.faw_calendar)
                .color(Color.WHITE)
                .sizeDp(25));


        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(getActivity());
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @OnClick(R.id.circleButton_member)
    public void startMemberActivity(){
//        getActivity().finish();
        // starting the other activity
        Intent moveToMember=new Intent(getActivity(),PostLoginActivity.class);
        startActivity(moveToMember);
    }

    @OnClick(R.id.circleButton_event)
    public void changeEventFragment(){
        mActivity.changeFragment(new FragmentEvents());
    }

    @OnClick(R.id.circleButton_about)
    public void showWebView(){
        mActivity.changeFragment(new FragmentWebView().newInstance("http://aryacollege.in",""));

        // setting title of action bar
        mActivity.getSupportActionBar().setTitle("About College");


    }

}
