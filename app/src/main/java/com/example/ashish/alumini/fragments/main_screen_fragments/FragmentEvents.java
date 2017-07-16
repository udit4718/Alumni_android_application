package com.example.ashish.alumini.fragments.main_screen_fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.ashish.alumini.adapters.EventListAdapter;
import com.example.ashish.alumini.R;
import com.example.ashish.alumini.activities.post_login.MainScreenActivity;
import com.example.ashish.alumini.fragments.FragmentWebView;
import com.example.ashish.alumini.network.pojo.EventListInstance;
import com.squareup.otto.Bus;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;


public class FragmentEvents extends Fragment {
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
    @Bind(R.id.listView_events)
    ListView mListView;

    ArrayList<EventListInstance> mInstanceArrayList = new ArrayList<>();

    Bus mBus = new Bus() ;

    MainScreenActivity mActivity ;

    ActionBar mActionBar;

    public FragmentEvents() {
        // Required empty public constructor
    }


    public static FragmentEvents newInstance(String param1, String param2) {
        FragmentEvents fragment = new FragmentEvents();
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
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        mActivity = (MainScreenActivity) getActivity();

        ButterKnife.bind(this,view);

        mActionBar = mActivity.getSupportActionBar();

        EventListInstance listInstance = new EventListInstance();

        listInstance.setName(getString(R.string.event_1) );
        listInstance.setUrl(getString(R.string.event_1_url));
        listInstance.setImageUrl(getString(R.string.event_1_image_url));
        mInstanceArrayList.add(listInstance);

        listInstance = new EventListInstance();
        listInstance.setName(getString(R.string.event_2) );
        listInstance.setUrl(getString(R.string.event_2_url));
        listInstance.setImageUrl(getString(R.string.event_2_image_url));
        mInstanceArrayList.add(listInstance);

        listInstance = new EventListInstance();
        listInstance.setName(getString(R.string.event_3) );
        listInstance.setUrl(getString(R.string.event_3_url));
        listInstance.setImageUrl(getString(R.string.event_3_image_url));
        mInstanceArrayList.add(listInstance);

        listInstance = new EventListInstance();
        listInstance.setName(getString(R.string.event_4) );
        listInstance.setUrl(getString(R.string.event_4_url));
        listInstance.setImageUrl(getString(R.string.event_4_image_url));
        mInstanceArrayList.add(listInstance);

        listInstance = new EventListInstance();
        listInstance.setName(getString(R.string.event_5) );
        listInstance.setUrl(getString(R.string.event_5_url));
        listInstance.setImageUrl(getString(R.string.event_5_image_url));
        mInstanceArrayList.add(listInstance);

        listInstance = new EventListInstance();
        listInstance.setName(getString(R.string.event_6) );
        listInstance.setUrl(getString(R.string.event_6_url));
        listInstance.setImageUrl(getString(R.string.event_6_image_url));
        mInstanceArrayList.add(listInstance);

        listInstance = new EventListInstance();
        listInstance.setName(getString(R.string.event_7) );
        listInstance.setUrl(getString(R.string.event_7_url));
        listInstance.setImageUrl(getString(R.string.event_7_image_url));
        mInstanceArrayList.add(listInstance);

        listInstance = new EventListInstance();
        listInstance.setName(getString(R.string.event_8) );
        listInstance.setUrl(getString(R.string.event_8_url));
        listInstance.setImageUrl(getString(R.string.event_8_image_url));
        mInstanceArrayList.add(listInstance);

        listInstance = new EventListInstance();
        listInstance.setName(getString(R.string.event_9) );
        listInstance.setUrl(getString(R.string.event_9_url));
        listInstance.setImageUrl(getString(R.string.event_9_image_url));
        mInstanceArrayList.add(listInstance);


        EventListAdapter adapter = new EventListAdapter(getActivity(),
                R.layout.list_layout_events,mInstanceArrayList);
        mListView.setDivider(null);
        mListView.setAdapter(adapter);


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

        mBus.post(false);

        //ActionBar operations
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle("Events");
        mActionBar.show();

    }

    @Override
    public void onPause() {
        super.onPause();

        mBus.unregister(getActivity());

        mActionBar.hide();
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @OnItemClick(R.id.listView_events)
    public void listClickListener(int position){
        // getting the web link of event
        String url = mInstanceArrayList.get(position).getUrl();
        mActivity.changeFragment(new FragmentWebView().newInstance(url,""));
    }

}
