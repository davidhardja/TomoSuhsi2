package com.example.david.tomosushi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.david.tomosushi.Adapter.OrderAdapter;
import com.example.david.tomosushi.Database.Data.CallbackWrapper;
import com.example.david.tomosushi.Database.Data.Data;
import com.example.david.tomosushi.Database.Data.Menus;
import com.victor.loading.book.BookLoading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by David on 29/10/2017.
 */

public class OrderActivity extends BaseActivity {
    @BindView(R.id.rv_cart)
    RecyclerView rvCart;
    @BindView(R.id.b_pay)
    Button bPay;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_quantity)
    TextView tvQuantity;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    OrderAdapter orderAdapter;
    List<Menus> menusList = new ArrayList<>();
    @BindView(R.id.b_back)
    Button bBack;

    @BindView(R.id.rl_wrapper_loading)
    RelativeLayout rlWrapperLoading;
    @BindView(R.id.rotateloading)
    BookLoading rlLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        ButterKnife.bind(this);
        customizeFonts(tvName, tvNo, tvQuantity, bPay, tvTitle, bBack);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setListener();
        setView();
    }

    private void setListener() {
        bBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProcess();
            }
        });
    }

    private void setView() {
        showLoading();
        menusList.clear();

        Call<CallbackWrapper> orderCall = getService().getOrder(getSession().getNoMeja());
        orderCall.enqueue(new Callback<CallbackWrapper>() {
            @Override
            public void onResponse(Call<CallbackWrapper> call, Response<CallbackWrapper> response) {
                if (response.isSuccessful()) {
                    List<Data> dataList = response.body().getData();

                    for (int i = 0; i < dataList.size(); i++) {
                        Menus menu = new Menus();
                        menu.setName(dataList.get(i).nama);
                        menu.setQuantity(Integer.valueOf(dataList.get(i).qty));
                        menu.setHarga(Integer.valueOf(dataList.get(i).harga));
                        menusList.add(menu);
                    }
                }
                orderAdapter.notifyDataSetChanged();
                hideLoading();
            }

            @Override
            public void onFailure(Call<CallbackWrapper> call, Throwable throwable) {
                hideLoading();
            }
        });

        if (orderAdapter == null) {
            orderAdapter = new OrderAdapter(this, menusList);
        }
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(orderAdapter);

    }

    private void showProcess() {
        final Dialog dialog = new Dialog(this, R.style.StyleDialog);
        dialog.setContentView(R.layout.dialog_confirmation);
        TextView tvConfirmation = dialog.findViewById(R.id.tv_confirmation);
        Button bOk = dialog.findViewById(R.id.b_ok);
        Button bNo = dialog.findViewById(R.id.b_no);

        customizeFonts(tvConfirmation, bOk, bNo);
        bOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Call<CallbackWrapper> closeCall = getService().closeOrder("status", "4", getSession().getNoMeja());
                closeCall.enqueue(new Callback<CallbackWrapper>() {
                    @Override
                    public void onResponse(Call<CallbackWrapper> call, Response<CallbackWrapper> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(OrderActivity.this, BillActivity.class);
                            startActivity(intent);
                            OrderActivity.this.finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<CallbackWrapper> call, Throwable throwable) {

                    }
                });
            }
        });

        bNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
