package com.example.david.tomosushi.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.david.tomosushi.Common.Constant;
import com.example.david.tomosushi.Database.Data.Menus;
import com.example.david.tomosushi.R;
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.text.MessageFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.grantland.widget.AutofitHelper;

/**
 * Created by David on 28/10/2017.
 */

public class MenuAdapter extends com.example.david.tomosushi.Adapter.BaseAdapter {
    private List<Menus> menusList;

    public MenuAdapter(Context context, List list) {
        super(context, list);
        menusList = list;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MenuAdapter.ViewHolder v = (MenuAdapter.ViewHolder) holder;
        v.bind(menusList.get(position));
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu, parent, false);
        return new MenuAdapter.ViewHolder(v);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.iv_menu)
        ImageView ivMenu;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_price)
        TextView tvPrice;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            customizeFonts(tvName, tvPrice);
            ivMenu.setOnClickListener(this);
        }

        private void bind(Menus menus) {
            tvName.setText(menus.getName());
            SpannableStringBuilder builder = new SpannableStringBuilder(MessageFormat.format(context.getString(R.string.rupiah), menus.getHarga()));
            tvPrice.setText(builder);
            Glide.with(context).load(menus.picture_url).placeholder(R.drawable.ic_sushi).into(ivMenu);
        }

        @Override
        public void onClick(View view) {
            if (view == ivMenu) {
                showMore();
            }
        }

        private void showMore() {
            final Dialog dialog = new Dialog(context, R.style.StyleDialog);
            dialog.setContentView(R.layout.dialog_choose);

            Button add = dialog.findViewById(R.id.b_add);
            Button cancel = dialog.findViewById(R.id.b_cancel);
            Button plus = dialog.findViewById(R.id.b_plus);
            Button minus = dialog.findViewById(R.id.b_minus);
            ImageView ivMenu = dialog.findViewById(R.id.iv_menu);
            TextView tvName = dialog.findViewById(R.id.tv_name);
            TextView tvDescription = dialog.findViewById(R.id.tv_description);
            TextView tvQuantity = dialog.findViewById(R.id.tv_quantity);
            RelativeLayout rlWrapper = dialog.findViewById(R.id.rl_wrapper);
            MaterialSpinner msModifier = dialog.findViewById(R.id.ms_modifier);
            final EditText etNote = dialog.findViewById(R.id.et_note);

            AutofitHelper.create(tvName);
            AutofitHelper.create(tvDescription);

            customizeFonts(add, cancel, plus, minus, tvName, tvDescription, tvQuantity, etNote);
            final EditText etQuantity = dialog.findViewById(R.id.et_quantity);
            etQuantity.setText("1");
            etQuantity.setInputType(0);

            final Menus menus = menusList.get(getAdapterPosition());
            final Menus temp = new Menus();
            Glide.with(context).load(menus.picture_url).placeholder(R.drawable.ic_sushi).into(ivMenu);
            tvName.setText(menus.getName());
            tvDescription.setText(menus.getKeterangan());

            if(menus.getModifier().size()!=0){
                msModifier.setVisibility(View.VISIBLE);
                msModifier.setItems(menus.getModifier());
                msModifier.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                        menus.setMod(item);
                    }
                });
            }else{
                msModifier.setVisibility(View.INVISIBLE);
            }

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.SHOW_SCREENSAVER = true;
                    dialog.dismiss();
                }
            });

            rlWrapper.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean find = false;
                    int position = 0;
                    for (int i = 0; i < Constant.cart.size(); i++) {
                        if (Constant.cart.get(i).getId().matches(menusList.get(getAdapterPosition()).getId())) {
                            find = true;
                            position = i;
                        }
                    }

                    if (find) {
                        Constant.cart.get(position).setQuantity(Constant.cart.get(position).getQuantity() + Integer.valueOf(etQuantity.getText().toString()));
                        Constant.cart.get(position).setKeterangan(etNote.getText().toString());
                    } else {
                        if (Integer.valueOf(etQuantity.getText().toString()) > 0) {
                            menus.setQuantity(Integer.valueOf(etQuantity.getText().toString()));
                            menus.setKeterangan(etNote.getText().toString());
                            Constant.cart.add(menus);
                        }
                    }
                    Constant.SHOW_SCREENSAVER = true;
                    Constant.mainActivity.refreshCart();
                    dialog.dismiss();
                }
            });

            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int qty = Integer.valueOf(etQuantity.getText().toString()) + 1;
                    etQuantity.setText(String.valueOf(qty));
                    temp.setQuantity(qty);
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Integer.valueOf(etQuantity.getText().toString()) > 1) {
                        int qty = Integer.valueOf(etQuantity.getText().toString()) - 1;
                        etQuantity.setText(String.valueOf(qty));
                        temp.setQuantity(qty);
                    }

                }
            });

            Constant.SHOW_SCREENSAVER = false;
            dialog.show();
        }
    }
}
