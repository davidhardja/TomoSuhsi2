package com.example.david.tomosushi.Fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.example.david.tomosushi.Application;
import com.example.david.tomosushi.Common.Constant;
import com.example.david.tomosushi.Database.DatabaseHelper;
import com.example.david.tomosushi.Service.ApiService;
import com.example.david.tomosushi.Tools.SessionManagement;

/**
 * Created by David on 28/10/2017.
 */

public class BaseFragment extends Fragment {

    protected DatabaseHelper helper;
    protected SessionManagement session;
    protected ApiService service;

    protected void customizeFonts(TextView... textViews) {
        for (TextView textView : textViews) {
            Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), Constant.DEFAULT_FONT);
            textView.setTypeface(typeFace);
        }
    }

    protected void customizeFont(TextView textView, String fontFace) {
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), fontFace);
        textView.setTypeface(typeFace);
    }

    public static void showSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
            inputMethodManager.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception n) {

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeAllService();
    }

    protected void initializeAllService() {
        if (service == null) {
            service = ((Application) getActivity().getApplication()).getService();
            helper = ((Application) getActivity().getApplication()).getHelper();
            session = ((Application) getActivity().getApplication()).getSession();
        }
    }

    public ApiService getService() {
        return service;
    }


}
