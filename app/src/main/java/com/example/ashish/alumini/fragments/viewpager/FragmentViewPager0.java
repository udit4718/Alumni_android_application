package com.example.ashish.alumini.fragments.viewpager;

import android.os.Bundle;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ashish.alumini.application.MyApplication;
import com.example.ashish.alumini.adapters.MemberAdapter;
import com.example.ashish.alumini.supporting_classes.RecyclerItemClickListener;
import com.example.ashish.alumini.R;
import com.example.ashish.alumini.activities.post_login.PostLoginActivity;
import com.example.ashish.alumini.fragments.settings.FragmentProfile;
import com.example.ashish.alumini.network.pojo.MemberInstance;
import com.example.ashish.alumini.supporting_classes.GlobalBus;
import com.example.ashish.alumini.supporting_classes.MemberLists;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.zhanghai.android.materialprogressbar.IndeterminateHorizontalProgressDrawable;

/**
 * Created by ashish on 14/3/16.
 */
public class FragmentViewPager0 extends android.support.v4.app.Fragment {

    private String TAG = getClass().getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.material_progressBar_viewpager)
    ProgressBar mProgressBar;

    // List and adapter
    private List<MemberInstance> mArrayList = new ArrayList<>();
    private MemberAdapter mAdapter;

    // activity
    PostLoginActivity mActivity;
    // application
    MyApplication mApplication;
    //event bus
    Bus mBus = new Bus();
    GlobalBus globalBus = GlobalBus.getInstance();

    MemberLists mMemberLists;

    RecyclerView.LayoutManager mLayoutManager;

    ActionBar mActionBar;

    public FragmentViewPager0() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // getting instance of application
        mApplication = (MyApplication) getActivity().getApplication();

        //getting instance of activity
        mActivity = (PostLoginActivity) getActivity();


         mMemberLists = mApplication.getMemberLists();
        if (mMemberLists!=null){
            mArrayList = mMemberLists.list;
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);


        mLayoutManager = new LinearLayoutManager(getActivity());

        //butterknife injections
        ButterKnife.bind(this,view);

        mProgressBar.setIndeterminateDrawable(new IndeterminateHorizontalProgressDrawable(getActivity()));


        // to reduce nullification
        if (mArrayList!=null){
            //initialization of adapter
            mAdapter = new MemberAdapter(mArrayList);
        }

        // setting layout
        recyclerView.setLayoutManager(mLayoutManager);
        // and finally adapter
        recyclerView.setAdapter(mAdapter);

        // onlick listener of recycler view
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // creaing a new fragment
                        FragmentProfile fragmentProfile = new FragmentProfile();

                        // setting the data of clicked item
                        fragmentProfile.setData(mArrayList.get(position));


                        // change the fragment
                        mActivity.changeFragment(fragmentProfile);

                        // notify to the activity -> to handle back pressed events
                        mBus.post(R.id.recycler_view);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                })
        );



        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onPause() {

        super.onPause();

        // unregistering buses
        mBus.unregister(getActivity());

        globalBus.unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        // registering event bus
        mBus.register(getActivity());

        globalBus.register(this);

        // checking if api call is still in progress or not
        if ( mMemberLists!=null && mMemberLists.mApiCallFlag){
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else
            mProgressBar.setVisibility(View.GONE);


    }

    @Subscribe
    public void ApiCallProgressListemer(Boolean aBoolean){
//        mProgressBar.setVisibility();
        Log.d(TAG, "Global Bus working ");
        if (mProgressBar!=null){
            if ( mMemberLists!=null){
                mProgressBar.setVisibility(View.VISIBLE);
            }
            else
                mProgressBar.setVisibility(View.GONE);
        }
        mAdapter.notifyDataSetChanged();


    }



}
