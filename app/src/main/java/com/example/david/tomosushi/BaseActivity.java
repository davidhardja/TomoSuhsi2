package com.example.david.tomosushi;

import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.david.tomosushi.Common.Constant;
import com.example.david.tomosushi.Database.DatabaseHelper;
import com.example.david.tomosushi.Service.ApiService;
import com.example.david.tomosushi.Tools.SessionManagement;

/**
 * Created by David on 27/10/2017.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public ApiService getService() {
        return ((Application) getApplication()).getService();
    }

    public DatabaseHelper getHelper() {
        return ((Application) getApplication()).getHelper();
    }

    public SessionManagement getSession() {
        return ((Application) getApplication()).getSession();
    }

    protected void customizeFonts(TextView... textViews) {
        for (TextView textView : textViews) {
            Typeface typeFace = Typeface.createFromAsset(getAssets(), Constant.DEFAULT_FONT);
            textView.setTypeface(typeFace);
        }
    }

}
