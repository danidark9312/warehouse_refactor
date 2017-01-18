package com.lotrading.controlwhapp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Daniel Gutierrez Soluciones Futuras S.A.S on 13/12/2016.
 */

abstract class AbstractActivity extends Activity {
    abstract protected void initComponents();
    abstract protected void initListener();

    @Override
    protected void onCreate(Bundle resources){
        super.onCreate(resources);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE); //landscape force
    }

    @Override
    protected void onStart(){
        super.onStart();
        initComponents();
        initListener();
    }

    //hide keyboard
    public static void hideKeyboard(Activity activity){
        try{
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
        catch (Exception e){
            Log.e("KeyBoardUtil", e.toString(), e);
        }
    }


}
