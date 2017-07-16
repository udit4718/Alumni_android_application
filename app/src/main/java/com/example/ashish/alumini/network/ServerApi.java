package com.example.ashish.alumini.network;

import com.example.ashish.alumini.network.pojo.JobListInstance;
import com.example.ashish.alumini.network.pojo.JobDetail;
import com.example.ashish.alumini.network.pojo.LoginResponse;
import com.example.ashish.alumini.network.pojo.MemberInstance;
import com.example.ashish.alumini.network.pojo.MemberListResponse;
import com.example.ashish.alumini.network.pojo.SignupPartial;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ashish on 8/8/16.
 */
public interface ServerApi {

    /*
    * to get the list of job posted
    * */
    @GET("/jobs")
    Call<List<JobListInstance>> getJobList();

    /*
    * To Open the details of job
    * */
    @GET("/jobs/detail/{id}")
    Call<JobDetail> getJobDetails(@Path("id") String id);


    /*
    * to post a job from settings menu
    * */
    @POST("jobs/post")
    Call<String> postJob(@Query("imagepath") String imagepath,
                         @Query("name") String name,
                         @Query("role") String role,
                         @Query("kahani") String description,
                         @Query("location") String location,
                         @Query("contactweb") String webLink,
                         @Query("contactemail") String email,
                         @Query("postedby") String postedBy,
                         @Query("postedbyid") String postdById
                         );

    /*
    * API call for partial signup
    * */
    @POST("members/signup/partial")
    Call<SignupPartial> signupPartial(@Query("name") String name,
                                      @Query("email") String email,
                                      @Query("password") String password
    );


    /*
    * complete signup api
    * */
    @POST("members/signup/complete")
    Call<String> signupComplete(@Query("_id") String id,
                                @Query("name") String name,
                                @Query("bio") String bio,
                                @Query("branch") String branch,
                                @Query("year") String year,
                                @Query("isNerd") boolean isNerd,
                                @Query("designation") String designation,
                                @Query("company") String company,
                                @Query("home") String home,
                                @Query("work") String work,
                                @Query("phone") String phone,
                                @Query("weblink") String webLink
                                );


    /*
    * old approach
    * getting the list of members in a single shot
    *
    * */
    @GET("members/")
    Call<List<MemberInstance>> getMemberList();

    /*
    * This will give the next 15 member data after the specified time
    * */
    @POST("members/getlist/")
    Call<MemberListResponse> getMemberListinChunks(@Query("time") String time);


    // login
    @POST("members/login/")
    Call<LoginResponse> login(@Query("email") String email,
                              @Query("password") String password);

    // getting complete data
    @POST("members/profile")
    Call<MemberInstance> getCompleteProfileData(@Query("_id") String id);

    // on click recyvler view (to get the remaining data of member)
    @POST("members/remaining-data")
    Call<MemberInstance> getRemainingDataForRecyclerView(@Query("_id") String id);

    /*
    * Multipart file uploading for jobs
    * */
    @Multipart
    @POST("/upload/job")
    Call<String> uploadJobImage(@Part MultipartBody.Part photo);

    /*
    * Image uploading for members
    * */
    @Multipart
    @POST("/upload/image")
    Call<String> uploadProfileImage(@Part MultipartBody.Part photo);

    // forget my password
    @POST("/mail")
    Call<String> resetPassword(@Query("email") String email);

    /*
    * call to get the member list after applying the filters
    * */
    @POST("/members/filter")
    Call<List<MemberInstance>> filterMembers(@Query("year") List<String> yearList,
                                       @Query("branch") List<String> branchList);

}
