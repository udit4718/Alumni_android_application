package com.example.ashish.alumini.fragments.filter_fragments;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ashish.alumini.BuildConfig;
import com.example.ashish.alumini.R;
import com.example.ashish.alumini.activities.post_login.PostLoginActivity;
import com.example.ashish.alumini.network.ApiClient;
import com.example.ashish.alumini.network.pojo.MemberInstance;
import com.example.ashish.alumini.supporting_classes.CommonData;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.otto.Bus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 * to handle interaction events.
 * Use the {@link FragmentFilter#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentFilter extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    String TAG = getClass().getSimpleName();

    @Bind(R.id.button_apply_filter)
    Button mButtonFilter;


    FragmentManager mFragmentManager;
    FragmentTransaction mFragmentTransaction;

    Bus mBus = new Bus();

    PostLoginActivity mActivity ;

    public FragmentFilter() {
        // Required empty public constructor
    }


    public static FragmentFilter newInstance(String param1, String param2) {
        FragmentFilter fragment = new FragmentFilter();
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
        View view = inflater.inflate(R.layout.fragment_filter, container, false);

        // resetting the predefined filters
//        CommonData.listYear.clear();
//        CommonData.listBranch.clear();

        //getting activity instance
        mActivity = (PostLoginActivity) getActivity();
        // changing title of action bar
        mActivity.getSupportActionBar().setTitle("Filter");

        // butterknife injections
        ButterKnife.bind(this,view);


        // getting fragment manager for transactions in onclick
        mFragmentManager = getChildFragmentManager();

        mActivity.getSupportActionBar().setTitle("Filter");

        if (CommonData.listBranch.size()>0){
            // this means the filters on branch has been applied before by the user
            // so make that fragment visible
            branchFragmentListener();
        }


        if (CommonData.listYear.size()>0){
            // this means the filters on year has been applied before by the user
            // so make that fragment visible
            yearFragmentListener();
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
    @OnClick(R.id.button_year)
    public void yearFragmentListener() {

        mFragmentTransaction = mFragmentManager.beginTransaction();

        // getting fragment YEAR
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_container_year);

        if (fragment.getView().getVisibility()==View.VISIBLE){

            // HIDE
            mFragmentTransaction
                    .hide(fragment)
                    .commit();
        } else if (fragment.getView().getVisibility()==View.GONE){

            // SHOW
            mFragmentTransaction
                    .show(fragment)
                    .commit();
        }

    }

    @OnClick(R.id.button_branch)
    public void branchFragmentListener() {

        mFragmentTransaction = mFragmentManager.beginTransaction();

        // getting fragment BRANCH
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragment_container_branch);

        if (fragment.getView().getVisibility()==View.VISIBLE){
            // HIDE
            mFragmentTransaction
                    .hide(fragment)
                    .commit();
        } else if (fragment.getView().getVisibility()==View.GONE){

            // SHOW
            mFragmentTransaction
                    .show(fragment)
                    .commit();
        }

    }

    @OnClick(R.id.button_apply_filter)
    public void buttonFilterHandler(){

        makeServerCallToFilterData();

    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(getActivity());

        // hiding both the fragments
        branchFragmentListener();

        yearFragmentListener();

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

    public void makeServerCallToFilterData(){

        if (CommonData.listYear.size()==0 && CommonData.listBranch.size()==0){

            TastyToast.makeText(getActivity(), "Invalid Filter Applied", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

            return;
        }

        // disabling button until the api call is completed
        mButtonFilter.setEnabled(false);

        // making progress bar visible
        mBus.post(true);

        Call<List<MemberInstance>> call = ApiClient.getServerApi().filterMembers(CommonData.listYear, CommonData.listBranch);

        call.enqueue(new Callback<List<MemberInstance>>() {
            @Override
            public void onResponse(Call<List<MemberInstance>> call, Response<List<MemberInstance>> response) {

                if (BuildConfig.DEBUG){
                    Log.d(TAG, "Api call successful makeServerCallToFilterData");
                }

                if (response.body()!=null && response.body().size()==0){

                    TastyToast.makeText(getActivity(), "Oops! No Result Found", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                }


                if (response.body()!=null && response.body().size()>0){

                    // copying the result to global static var
                    CommonData.mFilterResultList = response.body();

                    // change the fragment
                    mActivity.changeFragment(new FragmentFilterResult());

                    // notify to the activity -> to handle back pressed events
                    mBus.post(7777);

                }

                // making progress bar invisible
                mBus.post(false);

                mButtonFilter.setEnabled(true);
            }

            @Override
            public void onFailure(Call<List<MemberInstance>> call, Throwable t) {

                if (BuildConfig.DEBUG){
                    Log.d(TAG, "Api call failed makeServerCallToFilterData");
                }

                TastyToast.makeText(getActivity(), "Can't communicate to server", TastyToast.LENGTH_SHORT, TastyToast.ERROR);

                // making progress bar invisible
                mBus.post(false);

                mButtonFilter.setEnabled(true);
            }
        });


    }
}
