package com.example.david.tomosushi;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.david.tomosushi.Common.Constant;
import com.example.david.tomosushi.Database.Data.CallbackWrapper;
import com.victor.loading.book.BookLoading;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by David on 27/10/2017.
 */

public class LoginActivity extends BaseActivity {

    @BindView(R.id.pin_lock_view)
    PinLockView pinLockView;
    @BindView(R.id.indicator_dots)
    IndicatorDots indicatorDots;
    @BindView(R.id.b_exit)
    Button bExit;

    @BindView(R.id.rl_wrapper_loading)
    RelativeLayout rlWrapperLoading;
    @BindView(R.id.rotateloading)
    BookLoading rlLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setListener();
    }

    private void goToWelcomePage() {
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setListener() {
        pinLockView.attachIndicatorDots(indicatorDots);
        pinLockView.setPinLockListener(new PinLockListener() {
            @Override
            public void onComplete(String s) {
                showLoading();
                Call<CallbackWrapper> callLogin = getService().login(s);
                callLogin.enqueue(new Callback<CallbackWrapper>() {
                    @Override
                    public void onResponse(Call<CallbackWrapper> call, Response<CallbackWrapper> response) {
                        hideLoading();
                        if (response.isSuccessful()&&response.body().getCode().equals(Constant.API_SUCCESS)) {
                            goToWelcomePage();
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
                        hideLoading();
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

            @Override
            public void onEmpty() {

            }

            @Override
            public void onPinChange(int i, String s) {

            }
        });

        bExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
