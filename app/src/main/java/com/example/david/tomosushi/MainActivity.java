package com.example.david.tomosushi;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.bumptech.glide.Glide;
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
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;
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
    @BindView(R.id.b_back)
    Button bBack;
    Dialog dialog;
    MyCountDownTimer countDownTimer;
    long postIdentifier = 0;
    private Drawer result;
    private DrawerBuilder drawerBuilder;
    private List<String> mImages = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Constant.mainActivity = this;
        setView(savedInstanceState);
        setListener();
        customizeFonts(bBack, bBill, bCall);
        for (int i=0;i<3;i++){
            mImages.add("https://loremflickr.com/320/240");
        }


        dialog = new Dialog(this, R.style.StyleDialog);
        dialog.setContentView(R.layout.dialog_carousol);
        countDownTimer = new MyCountDownTimer(getSession().getTimer(), Constant.INTERVAL);
        countDownTimer.start();
    }

    private void setListener() {
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVerifyBack();
            }
        });

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
                                    if (drawerItem.getType() == Constant.PRIMARY) {
                                        PrimaryDrawerItem item = (PrimaryDrawerItem) drawerItem;
                                        String name = item.getName().getText().toString();
                                        Fragment f = DrawerFragment.newInstance(name, String.valueOf(drawerItem.getTag()));
                                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, f).commit();
                                    } else if (drawerItem.getType() == Constant.SECONDARY) {
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

    private void showVerifyBack() {
        final Dialog dialog = new Dialog(this, R.style.StyleDialog);
        dialog.setContentView(R.layout.dialog_password);
        RelativeLayout rlWrapper = dialog.findViewById(R.id.rl_wrapper);
        final PinLockView pinLockView = dialog.findViewById(R.id.pin_lock_view);
        IndicatorDots indicatorDots = dialog.findViewById(R.id.indicator_dots);
        pinLockView.attachIndicatorDots(indicatorDots);
        pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String s) {
                if (s.matches(Constant.MASTER_PASSWORD)) {
                    finish();
                } else {
                    Call<CallbackWrapper> callLogin = getService().login(s);
                    callLogin.enqueue(new Callback<CallbackWrapper>() {
                        @Override
                        public void onResponse(Call<CallbackWrapper> call, Response<CallbackWrapper> response) {
                            if (response.isSuccessful() && response.body().getCode().equals(Constant.API_SUCCESS)) {
                                dialog.dismiss();
                                finish();
                            } else {
                                YoYo.with(Techniques.Shake)
                                        .duration(500)
                                        .repeat(1).onEnd(new YoYo.AnimatorCallback() {
                                    @Override
                                    public void call(Animator animator) {
                                        pinLockView.resetPinLockView();
                                    }
                                })
                                        .playOn(pinLockView);
                            }
                        }

                        @Override
                        public void onFailure(Call<CallbackWrapper> call, Throwable throwable) {
                            YoYo.with(Techniques.Shake)
                                    .duration(500)
                                    .repeat(1).onEnd(new YoYo.AnimatorCallback() {
                                @Override
                                public void call(Animator animator) {
                                    pinLockView.resetPinLockView();
                                }
                            })
                                    .playOn(pinLockView);
                        }
                    });
                }
            }

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int i, String s) {

            }
        });

        rlWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void showCarousol() {

        final CarouselView carouselView = dialog.findViewById(R.id.carouselView);
        ImageButton ibNext = dialog.findViewById(R.id.ib_next);
        ImageButton ibBefore = dialog.findViewById(R.id.ib_before);
        ImageButton ibClose = dialog.findViewById(R.id.ib_close);

        carouselView.setPageCount(mImages.size());

        final ImageListener imageListener = new ImageListener() {
            @Override
            public void setImageForPosition(int position, ImageView imageView) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                Glide.with(getApplicationContext())
                        .load(mImages.get(position))
                        .centerCrop()
                        .into(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        countDownTimer.start();
                        dialog.dismiss();
                    }
                });

            }
        };

        ibClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimer.cancel();
                countDownTimer.start();
                dialog.dismiss();
            }
        });

        ibNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carouselView.getCurrentItem() < mImages.size()) {
                    carouselView.setCurrentItem(carouselView.getCurrentItem() + 1);
                }
            }
        });

        ibBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (carouselView.getCurrentItem() > 0) {
                    carouselView.setCurrentItem(carouselView.getCurrentItem() - 1);
                }
            }
        });

        carouselView.setImageListener(imageListener);

        if (Constant.SHOW_SCREENSAVER) {
            dialog.show();
        } else {
            countDownTimer.cancel();
            countDownTimer.start();
        }
    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            try{
                showCarousol();
            }catch (Exception e){}
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }
    }
}
