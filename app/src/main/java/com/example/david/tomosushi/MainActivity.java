package com.example.david.tomosushi;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.david.tomosushi.Common.Constant;
import com.example.david.tomosushi.Database.Data.CallbackWrapper;
import com.example.david.tomosushi.Database.Data.Data;
import com.example.david.tomosushi.Fragment.DrawerFragment;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableBadgeDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.victor.loading.book.BookLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity {
    @BindView(R.id.ib_chart)
    ImageButton ibChart;
    @BindView(R.id.tv_notif)
    TextView tvNotif;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_wrapper_loading)
    RelativeLayout rlWrapperLoading;
    @BindView(R.id.rotateloading)
    BookLoading rlLoading;
    @BindView(R.id.b_bill)
    Button bBill;
    @BindView(R.id.b_call)
    Button bCall;


    private Drawer result;
    private DrawerBuilder drawerBuilder;
    long postIdentifier = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Constant.mainActivity = this;
        setView(savedInstanceState);
        setListener();
    }

    private void setListener() {
        ibChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CartActivity.class);
                startActivity(intent);
            }
        });

        bBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(intent);
            }
        });

        bCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogWaiter();
                Call<CallbackWrapper> callWaiter = getService().callWaiter(getSession().getNoMeja());
                callWaiter.enqueue(new Callback<CallbackWrapper>() {
                    @Override
                    public void onResponse(Call<CallbackWrapper> call, Response<CallbackWrapper> response) {

                    }

                    @Override
                    public void onFailure(Call<CallbackWrapper> call, Throwable throwable) {

                    }
                });
            }
        });
    }

    public void showDialogWaiter() {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.dialog_call_waiter);
        ImageView ivWaiter = dialog.findViewById(R.id.iv_waiter);

        YoYo.with(Techniques.Shake)
                .duration(700)
                .repeat(5).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                dialog.dismiss();
            }
        })
                .playOn(ivWaiter);

        dialog.show();
    }


    private void setView(final Bundle savedInstanceState) {

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("TOMO SUSHI");
        }
        showLoading();
        Call<CallbackWrapper> call = getService().getAllCategory();
        call.enqueue(new retrofit2.Callback<CallbackWrapper>() {
            @Override
            public void onResponse(Call<CallbackWrapper> call, Response<CallbackWrapper> response) {
                if (response.isSuccessful()) {
                    drawerBuilder = new DrawerBuilder(MainActivity.this)
                            .withRootView(R.id.drawer_container)
                            .withToolbar(toolbar)
                            .withSliderBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.cf2b68c))
                            .withShowDrawerOnFirstLaunch(true)
                            .withDisplayBelowStatusBar(false)
                            .withSavedInstance(savedInstanceState)
                            .withActionBarDrawerToggleAnimated(true);

                    for (int i = 0; i < response.body().getData().size(); i++) {
                        Data data = response.body().getData().get(i);
                        if (data.subs.size() == 0) {
                            PrimaryDrawerItem primaryDrawerItem = new PrimaryDrawerItem();
                            primaryDrawerItem.withName(data.nama);
                            primaryDrawerItem.withTag(data.id);
                            drawerBuilder.addDrawerItems(primaryDrawerItem);
                        } else {
                            ExpandableBadgeDrawerItem primaryDrawerItem = new ExpandableBadgeDrawerItem().withSelectable(false);
                            primaryDrawerItem.withName(data.nama);

                            primaryDrawerItem.withTag(data.id);
                            List<IDrawerItem> subList = new ArrayList<>();
                            for (int j = 0; j < data.subs.size(); j++) {
                                SecondaryDrawerItem drawerItem = new SecondaryDrawerItem();
                                drawerItem.withTag(data.subs.get(j).id);
                                drawerItem.withName(data.subs.get(j).nama);
                                drawerItem.withLevel(2);
                                subList.add(drawerItem);
                            }
                            primaryDrawerItem.withSubItems(subList);
                            drawerBuilder.addDrawerItems(primaryDrawerItem);
                        }
                    }
                    drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            if (drawerItem != null) {
                                if (drawerItem.isSelectable()) {
                                    if(drawerItem.getType() == Constant.PRIMARY ){
                                        PrimaryDrawerItem item = (PrimaryDrawerItem) drawerItem;
                                        String name = item.getName().getText().toString();
                                        Fragment f = DrawerFragment.newInstance(name, String.valueOf(drawerItem.getTag()));
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                                    }else if(drawerItem.getType() == Constant.SECONDARY ){
                                        SecondaryDrawerItem item = (SecondaryDrawerItem) drawerItem;
                                        postIdentifier = item.getIdentifier();
                                        String name = item.getName().getText().toString();
                                        Fragment f = DrawerFragment.newInstance(name, String.valueOf(drawerItem.getTag()));
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                                    }
                                }
                            }
                            return false;
                        }
                    });
                    result = drawerBuilder.build();
                    result.getAdapter().withOnlyOneExpandedItem(true);
                    result.openDrawer();

                    hideLoading();
                } else {
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<CallbackWrapper> call, Throwable t) {
                hideLoading();
            }
        });
    }

    public void refreshCart() {
        if (Constant.cart.size() > 0) {
            tvNotif.setText(String.valueOf(Constant.cart.size()));
            tvNotif.setVisibility(View.VISIBLE);
        } else {
            tvNotif.setVisibility(View.INVISIBLE);
        }
    }

    public void showLoading() {
        rlWrapperLoading.setVisibility(View.VISIBLE);
        rlLoading.start();
    }

    public void hideLoading() {
        rlWrapperLoading.setVisibility(View.GONE);
        rlLoading.stop();
    }
}
