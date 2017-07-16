package com.example.ashish.alumini.fragments.viewpager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.ashish.alumini.activities.post_login.PostLoginActivity;
import com.example.ashish.alumini.application.MyApplication;
import com.example.ashish.alumini.fragments.settings.FragmentProfile;
import com.example.ashish.alumini.adapters.MemberAdapter;

import com.example.ashish.alumini.R;
import com.example.ashish.alumini.supporting_classes.RecyclerItemClickListener;
import com.example.ashish.alumini.network.pojo.MemberInstance;
import com.example.ashish.alumini.supporting_classes.GlobalBus;
import com.example.ashish.alumini.supporting_classes.MemberLists;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by ashish on 14/3/16.
 */
public class FragmentViewPager2 extends Fragment {
    private String TAG = getClass().getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Bind(R.id.material_progressBar_viewpager)
    ProgressBar mProgressBar;

    private List<MemberInstance> mArrayList3 = new ArrayList<>();
    private List<MemberInstance> mArrayList = new ArrayList<>();


    // activity
    PostLoginActivity mActivity;
    // application
    MyApplication mApplication;
    //event bus
    Bus mBus = new Bus();

    GlobalBus globalBus = GlobalBus.getInstance();
    public FragmentViewPager2() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getting instance of application
        mActivity = (PostLoginActivity) getActivity();
        mApplication = (MyApplication) mActivity.getApplication();

        // getting the instance of class to access the lists
        MemberLists memberLists = mApplication.getMemberLists();
        //obtaining the list
        if (memberLists!=null){
            mArrayList3 = memberLists.list;
        }

        if (mArrayList!=null){
            for (int i = 0; i< mArrayList3.size(); i++){
//            chgecking the state of isNerd in every item and then adding to the new list
                if (mArrayList3.get(i).getIsNerd()!=null && mArrayList3.get(i).getIsNerd()){
                    mArrayList.add(mArrayList3.get(i));
                }
            }
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);
        //butterknife injections
        ButterKnife.bind(this,view);

        //initialization of adapter
        MemberAdapter mAdapter = new MemberAdapter(mArrayList);

        // making progress bar invisible
        mProgressBar.setVisibility(View.GONE);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);

        // onlick listener of recycler view
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), recyclerView ,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
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
        mBus.unregister(getActivity());

        globalBus.unregister(this);
    }


    @Override
    public void onResume() {
        super.onResume();
        mBus.register(getActivity());

        globalBus.register(this);


    }


}

