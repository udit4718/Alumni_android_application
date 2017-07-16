package com.example.ashish.alumini.supporting_classes;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.example.ashish.alumini.BuildConfig;
import com.example.ashish.alumini.network.ApiClient;
import com.example.ashish.alumini.network.db_models.MemberInstanceModel;
import com.example.ashish.alumini.network.pojo.MemberInstance;
import com.example.ashish.alumini.network.pojo.MemberListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ashish on 3/9/16.
 * this is a class will make the api calls to get the data so that the loading time can be reduced
 */
public class MemberLists {

    String TAG = getClass().getSimpleName();

    public List<MemberInstance> list = new ArrayList<>();

    public boolean mApiCallFlag = false;

    GlobalBus mGlobalBus = GlobalBus.getInstance();

    public MemberLists() {

        // this date is the largest possible date
        // this makes the sorting easy
        makeServerCallToGetAllMemberData("3016-09-18T06:25:22.767Z");

        mGlobalBus.register(this);
    }

    /*
    * old api to get all data in one call
    * */
    public void makeServerCallToGetAllMemberData(){


        Log.d(TAG, "Class/API created");
        Call<List<MemberInstance>> call = ApiClient.getServerApi().getMemberList();

        call.enqueue(new Callback<List<MemberInstance>>() {
            @Override
            public void onResponse(Call<List<MemberInstance>> call, Response<List<MemberInstance>> response) {
                Log.d(TAG,"API call successful");
                list = response.body();
            }

            @Override
            public void onFailure(Call<List<MemberInstance>> call, Throwable t) {
                Log.d(TAG,"API call failed");
            }
        });
    }

    public void makeServerCallToGetAllMemberData(String time){

        // this will be subscribed by FragmentMembers - for starting the progress bar
        mGlobalBus.post(true);


        // changing the api call flag to get to know that whether api call has finished or not
        mApiCallFlag = true;


        Call<MemberListResponse> call = ApiClient.getServerApi().getMemberListinChunks(time);

        call.enqueue(new Callback<MemberListResponse>() {
            @Override
            public void onResponse(Call<MemberListResponse> call, Response<MemberListResponse> response) {

                Log.d(TAG,"API call successful makeServerCallToGetAllMemberData");

                MemberListResponse response1 = response.body();

                // preventing nullification
                if (response1!=null){
                    List<MemberInstance> instanceList = response1.getList();

                    // saving the result in active android
                    ActiveAndroid.beginTransaction();

                    try {
                        // iterating all the list instances and adding to main list
                        for (MemberInstance memberInstance: instanceList) {
                            list.add(memberInstance);
                            MemberInstanceModel memberInstanceModel = new MemberInstanceModel();
                            memberInstanceModel.set_id(memberInstance.get_id());
                            memberInstanceModel.setName(memberInstance.getName());
                            memberInstanceModel.setYear(memberInstance.getYear());
                            memberInstanceModel.setIsNerd(memberInstance.getIsNerd());
                            memberInstanceModel.setWork(memberInstance.getWork());
                            memberInstanceModel.setDesignation(memberInstance.getDesignation());
                            memberInstanceModel.save();

                        }
                        ActiveAndroid.setTransactionSuccessful();
                    }
                    finally {
                        ActiveAndroid.endTransaction();
                    }


                }

                // checking if this is the last result or not
                // 99 because it is set by server
                if (response1.getTime().contentEquals("99")){
                    // this means no more API calls are required

                    mApiCallFlag = false;
                    return;
                }

                // start call again
                makeServerCallToGetAllMemberData(response1.getTime());

                // this will be subscribed by FragmentMembers - for starting the progress bar
                mGlobalBus.post(false);
            }

            @Override
            public void onFailure(Call<MemberListResponse> call, Throwable t) {


                // API call failed and populate the list from local db

                      List<MemberInstanceModel> lst  = new Select()
                        .from(MemberInstanceModel.class)
                        .execute();
                for (MemberInstanceModel model: lst ) {
                    MemberInstance memberInstance = new MemberInstance();
                    memberInstance.set_id(model.get_id());
                    memberInstance.setName(model.getName());
                    memberInstance.setYear(model.getYear());
                    memberInstance.setWork(model.getWork());
                    memberInstance.setIsNerd(model.getIsNerd());
                    memberInstance.setDesignation(model.getDesignation());
                    list.add(memberInstance);
                }
                if (lst.isEmpty()){
                    Log.d(TAG,"No data to show");
                }

                // changing flag
                mApiCallFlag = false;

                if (BuildConfig.DEBUG){
                    Log.d(TAG,"API call failed makeServerCallToGetAllMemberData" + t.toString());
                }

                // this will be subscribed by FragmentMembers - for starting the progress bar
                mGlobalBus.post(false);
            }
        });
    }

}
