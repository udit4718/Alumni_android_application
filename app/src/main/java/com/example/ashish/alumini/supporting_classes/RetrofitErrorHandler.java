package com.example.ashish.alumini.supporting_classes;

import android.content.Context;

import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by ashish on 9/9/16.
 * to handle the retrofit errors
 */
public class RetrofitErrorHandler {
    public void statusCodeHandler(Context context, int errorCode){
        if (errorCode == 500){
            TastyToast.makeText(context,"Oops, Our servers are sick!",TastyToast.LENGTH_SHORT,TastyToast.ERROR);
        }
    }
}
