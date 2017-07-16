package com.example.ashish.alumini.fragments.filter_fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.example.ashish.alumini.BuildConfig;
import com.example.ashish.alumini.R;
import com.example.ashish.alumini.activities.post_login.PostLoginActivity;
import com.example.ashish.alumini.supporting_classes.CommonData;
import com.squareup.otto.Bus;

import butterknife.Bind;
import butterknife.ButterKnife;


public class FragmentFilterBranch extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private String TAG = getClass().getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";



    /*
    * Butterknife
    * */
//    @Bind(R.id.button_settings)
//    Button j;



    @Bind(R.id.linearlayout_checkboxlist)
    LinearLayout mLinearLayout;

    Bus mBus = new Bus();

    PostLoginActivity mActivity ;

    public FragmentFilterBranch() {
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
    public static FragmentFilterBranch newInstance(String param1, String param2) {
        FragmentFilterBranch fragment = new FragmentFilterBranch();
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
        View view = inflater.inflate(R.layout.fragment_filter_list, container, false);

        mActivity = (PostLoginActivity) getActivity();

        ButterKnife.bind(this,view);



        String[] stringArray = getResources().getStringArray(R.array.branch_array);


        ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(

                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for (int i= 0; i<stringArray.length; i++){
            final CheckBox checkBox = new CheckBox(mActivity);
            checkBox.setLayoutParams( lparams);
            checkBox.setText(stringArray[i]);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkBox.isChecked()){
                        CommonData.listBranch.add(checkBox.getText().toString());
                    }
                    if (!checkBox.isChecked()){
                        CommonData.listBranch.remove(checkBox.getText().toString());
                    }

                    if (BuildConfig.DEBUG){
                        Log.d(TAG, "String list " + CommonData.listBranch);
                    }

                }
            });

            if (CommonData.listBranch.contains(stringArray[i])){
                checkBox.setChecked(true);
            }
            mLinearLayout.addView(checkBox);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onResume() {
        super.onResume();

        //Bus Registering
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


}
