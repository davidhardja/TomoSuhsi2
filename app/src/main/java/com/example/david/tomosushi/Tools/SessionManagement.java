package com.example.david.tomosushi.Tools;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.david.tomosushi.Common.Constant;
import com.example.david.tomosushi.R;

/**
 * Created by David on 27/10/2017.
 */
public class SessionManagement {

    private static final String PREF_NAME = "TOMO_PREF";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    private static final String KEY_TIMER = "TIMER";
    private static final String KEY_WELCOME = "WELCOME";
    private static final String NO_MEJA = "NO_MEJA";
    private static final String BASE_URL = "BASE_URL";


    public SessionManagement(Context context){
        this._context = context;
        preferences = _context.getSharedPreferences(PREF_NAME,PRIVATE_MODE);
        editor = preferences.edit();
        editor.apply();
    }

    public void setBaseURL(String s){
        preferences.edit().putString(BASE_URL,s).apply();
        editor.commit();
    }

    public String getBaseUrl(){
        return preferences.getString(BASE_URL,Constant.API_URL);
    }

    public void setTimer(long timer) {
        preferences.edit().putLong(KEY_TIMER, timer).apply();
        editor.commit();
    }

    public Long getTimer() {
        return preferences.getLong(KEY_TIMER, Constant.START_TIME);

    }

    public void setWelcomeText(String welcome) {
        preferences.edit().putString(KEY_WELCOME, welcome).apply();
        editor.commit();
    }

    public String getWelcomeText() {
        return preferences.getString(KEY_WELCOME, _context.getString(R.string.welcome));

    }

    public void setNoMeja(String noMeja) {
        preferences.edit().putString(NO_MEJA, noMeja).apply();
        editor.commit();
    }

    public String getNoMeja() {
        return preferences.getString(NO_MEJA,Constant.ID_MEJA);

    }
}
