package com.example.ashish.alumini.network;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {

   public static final String BASE_URL = "http://"+ "172.20.10.2" + ":3000/";    // server

    private static Retrofit retrofit = null;

    // copied from some tutorials
    public static Retrofit getClientOld() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // socket time out exception in image uploading
    //http://stackoverflow.com/questions/26750650/retrofit-sockettimeoutexception-in-sending-multiparty-or-json-data-in-android
    private static OkHttpClient getClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        return client;
    }

    // modified function
    public static ServerApi getServerApi() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(getClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ServerApi.class);
    }

}