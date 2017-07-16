package com.example.ashish.alumini.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.activeandroid.query.Select;
import com.example.ashish.alumini.BuildConfig;
import com.example.ashish.alumini.adapters.JobListAdapter;
import com.example.ashish.alumini.R;


import com.example.ashish.alumini.network.ApiClient;
import com.example.ashish.alumini.network.db_models.JobInstanceModel;
import com.example.ashish.alumini.network.pojo.JobListInstance;
import com.example.ashish.alumini.activities.post_login.PostLoginActivity;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FragmentJobs extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String TAG = getClass().getSimpleName();

    @Bind(R.id.listView_jobs)
    ListView mListViewJobs;


    List<JobListInstance> mJobArrayList = new ArrayList<>();
    JobListAdapter mListAdapter;

    PostLoginActivity mActivity;

    Bus mBus = new Bus();


    public FragmentJobs() {

        // start server calls first of all
        makeServerCallToGetTheList();

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentJobs.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentJobs newInstance(String param1, String param2) {
        FragmentJobs fragment = new FragmentJobs();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mActivity  = (PostLoginActivity) getActivity();


    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mActivity  = (PostLoginActivity) getActivity();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_jobs,container,false);

        mActivity.getSupportActionBar().setTitle("Jobs");


        //Butter knife binding
        ButterKnife.bind(this,view);

        mListAdapter = new JobListAdapter(getActivity(),R.layout.list_layout_job, mJobArrayList);
        mListViewJobs.setAdapter(mListAdapter);

        mActivity.getSupportActionBar().setTitle("Jobs");


        return view;
    }




    @OnItemClick(R.id.listView_jobs)
    public void listClickHandler(int position){
        FragmentJobDetails fragmentJobDetails = new FragmentJobDetails();
        fragmentJobDetails.setData(mJobArrayList.get(position));
        mActivity.changeFragment(fragmentJobDetails);
        mBus.post(8888);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(getActivity());
    }

    @Override
    public void onResume() {
        super.onResume();

        //Bus Registering
        mBus.register(getActivity());

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public void makeServerCallToGetTheList(){
        Call<List<JobListInstance>> call = ApiClient.getServerApi().getJobList();



        call.enqueue(new Callback<List<JobListInstance>>() {
            @Override
            public void onResponse(Call<List<JobListInstance>> call, Response<List<JobListInstance>> response) {

                if (BuildConfig.DEBUG){
                    Log.d(TAG," API Call Successfull makeServerCallToGetTheList");
                }


                if (response!=null){
                    mJobArrayList = response.body();
                }

                if (mJobArrayList!=null){
                    //iterating the list to save in database
                    for (JobListInstance model: mJobArrayList ) {

                        // creating a new model + saving in db
                        JobInstanceModel jobModel = new JobInstanceModel();
                        jobModel.setName(model.getName());
                        jobModel.set_id(model.get_id());
                        jobModel.setDesignation(model.getRole());
                        jobModel.setLocation(model.getLocation());
                        jobModel.save();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<JobListInstance>> call, Throwable t) {

                // fetch from database
                List<JobInstanceModel> list = new Select().from(JobInstanceModel.class).execute();

                for (JobInstanceModel model: list ) {

                    JobListInstance jobListInstance = new JobListInstance();
                    jobListInstance.setLocation(model.getLocation());
                    jobListInstance.setName(model.getName());
                    jobListInstance.set_id(model.get_id());
                    jobListInstance.setRole(model.getDesignation());
                    mJobArrayList.add(jobListInstance);

                }

                if (BuildConfig.DEBUG){
                    Log.d(TAG,"API call Failed makeServerCallToGetTheList" + t.toString() );
                }
            }
        });
    }


}
