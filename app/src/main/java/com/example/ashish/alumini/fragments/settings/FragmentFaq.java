package com.example.ashish.alumini.fragments.settings;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.ashish.alumini.adapters.ExpandableListAdapter;
import com.example.ashish.alumini.R;
import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FragmentFaq extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";




    /*
    * Butterknife
    * */
    @Bind(R.id.expandableListView)
    ExpandableListView mExpListView;

    android.widget.ExpandableListAdapter mListAdapter;

    List<String> mListHeaders;
    HashMap<String, List<String>> mListChild;


    Bus mBus = new Bus();

    public FragmentFaq() {
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
    public static FragmentFaq newInstance(String param1, String param2) {
        FragmentFaq fragment = new FragmentFaq();
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
        View view = inflater.inflate(R.layout.fragment_expandable_list, container, false);

        ButterKnife.bind(this,view);
        //Bus Registering
        mBus.register(getActivity());



        mListHeaders = new ArrayList<>();
        mListChild = new HashMap<>();
        mListHeaders.add(getString(R.string.ques_1));
        mListHeaders.add(getString(R.string.ques_2));
        mListHeaders.add(getString(R.string.ques_3));
//        mListHeaders.add(getString(R.string.ques_4));

        mListChild = new HashMap<>();

        List<String> ans1 = new ArrayList<>();
        ans1.add(getResources().getString(R.string.answer1));

        List<String> ans2 = new ArrayList<>();
        ans2.add(getResources().getString(R.string.answer2));

        List<String> ans3 = new ArrayList<>();
        ans3.add(getResources().getString(R.string.answer3));

//        List<String> ans4 = new ArrayList<>();
//        ans4.add(getResources().getString(R.string.answer4));

        mListChild.put(mListHeaders.get(0), ans1);
        mListChild.put(mListHeaders.get(1), ans2);
        mListChild.put(mListHeaders.get(2), ans3);
//        mListChild.put(mListHeaders.get(3), ans4);
        mListAdapter = new ExpandableListAdapter(getActivity(), mListHeaders, mListChild);

        // setting list adapter

        mExpListView.setAdapter(mListAdapter);




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

}
