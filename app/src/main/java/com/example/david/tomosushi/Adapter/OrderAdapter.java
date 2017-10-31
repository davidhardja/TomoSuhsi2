package com.example.david.tomosushi.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.david.tomosushi.Database.Data.Menus;
import com.example.david.tomosushi.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitHelper;

/**
 * Created by David on 29/10/2017.
 */

public class OrderAdapter extends BaseAdapter {
    private List<Menus> menusList;

    public OrderAdapter(Context context, List<Menus> list) {
        super(context, list);
        menusList = list;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        OrderAdapter.ViewHolder v = (OrderAdapter.ViewHolder) holder;
        v.bind(menusList.get(position));
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderAdapter.ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.ll_wrapper)
        LinearLayout llWrapper;
        @BindView(R.id.tv_no)
        TextView tvNo;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_quantity)
        TextView tvQuantity;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            customizeFonts(tvNo, tvName, tvQuantity);
        }

        private void bind(Menus menus) {
            tvNo.setText(String.valueOf(getAdapterPosition() + 1));
            tvName.setText(menus.getName());
            tvQuantity.setText(String.valueOf(menus.getQuantity()));
            AutofitHelper.create(tvName);
            AutofitHelper.create(tvQuantity);

            if (getAdapterPosition() % 2 == 0) {
                llWrapper.setBackgroundResource(R.color.cF28888);
            } else {
                llWrapper.setBackgroundResource(R.color.cF9AD9D);
            }
        }

        @Override
        public void onClick(View view) {

        }

    }
}
