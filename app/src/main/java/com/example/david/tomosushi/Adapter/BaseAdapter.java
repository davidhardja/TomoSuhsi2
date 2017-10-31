package com.example.david.tomosushi.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.tomosushi.Application;
import com.example.david.tomosushi.Common.Constant;
import com.example.david.tomosushi.Database.DatabaseHelper;
import com.example.david.tomosushi.Service.ApiService;
import com.example.david.tomosushi.Tools.SessionManagement;

import java.util.List;

/**
 * Created by David on 28/10/2017.
 */

public class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected Context context;
    protected List<T> list;


    BaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    protected void customizeFont(TextView textView, String fontFace) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(), fontFace);
        textView.setTypeface(typeFace);
    }

    protected void customizeFonts(TextView... textViews) {
        for (TextView textView : textViews) {
            Typeface typeFace = Typeface.createFromAsset(context.getAssets(), Constant.DEFAULT_FONT);
            textView.setTypeface(typeFace);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public ApiService getService() {
        return ((Application) context.getApplicationContext()).getService();
    }

    public DatabaseHelper getHelper() {
        return ((Application) context.getApplicationContext()).getHelper();
    }

    public SessionManagement getSession() {
        return ((Application) context.getApplicationContext()).getSession();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ViewHolder(View v) {
            super(v);
        }
    }

    protected boolean isNetworkConnected() {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cm != null) {
                NetworkInfo ni = cm.getActiveNetworkInfo();
                return ni != null;
            }
            return false;
        }
        return false;
    }

}
