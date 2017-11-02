package com.example.david.tomosushi.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.david.tomosushi.Adapter.MenuAdapter;
import com.example.david.tomosushi.Database.Data.CallbackWrapper;
import com.example.david.tomosushi.Database.Data.Data;
import com.example.david.tomosushi.Database.Data.Menus;
import com.example.david.tomosushi.MainActivity;
import com.example.david.tomosushi.R;
import com.example.david.tomosushi.Tools.ItemOffsetDecoration;
import com.example.david.tomosushi.Tools.VarColumnGridLayoutManager;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by David on 27/10/2017.
 */

public class DrawerFragment extends BaseFragment {
    private static final String TITLE = "title";
    private static final String ID = "id";

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_menu)
    RecyclerView rvMenu;

    List<Menus> menusList = new ArrayList<>();
    MenuAdapter menuAdapter;

    public DrawerFragment() {
        // Required empty public constructor
    }

    public static DrawerFragment newInstance(String title, String id) {
        DrawerFragment f = new DrawerFragment();

        Bundle args = new Bundle();

        args.putString(TITLE, title);
        args.putString(ID, id);
        f.setArguments(args);

        return (f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        ButterKnife.bind(this, view);
        tvTitle.setText(getArguments().getString(TITLE));
        customizeFonts(tvTitle);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setMenu();
        if (menusList.size() == 0) {
            setData();
        }
    }

    private void setMenu() {

        if (menuAdapter == null) {
            menuAdapter = new MenuAdapter(getActivity(), menusList);
        }
        //rvMenu.setLayoutManager(new VarColumnGridLayoutManager(getActivity(),GridLayoutManager.VERTICAL,false,TypedValue.COMPLEX_UNIT_DIP,210f));
        FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setFlexDirection(FlexDirection.ROW);
        layoutManager.setJustifyContent(JustifyContent.SPACE_AROUND);
        rvMenu.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getContext(), R.dimen.item_offset);
        rvMenu.addItemDecoration(itemDecoration);
        rvMenu.setAdapter(menuAdapter);

    }

    private void setData() {
        Call<CallbackWrapper> call = getService().getMenuCategory(getArguments().getString(ID));
        call.enqueue(new Callback<CallbackWrapper>() {
            @Override
            public void onResponse(Call<CallbackWrapper> call, Response<CallbackWrapper> response) {
                if (response.isSuccessful()) {
                    List<Data> dataList = response.body().getData();
                    menusList.clear();
                    for (int i = 0; i < dataList.size(); i++) {
                        Menus m = new Menus();
                        m.id = dataList.get(i).id;
                        m.name = dataList.get(i).nama;
                        m.harga = Integer.valueOf(dataList.get(i).harga);
                        m.picture_url = dataList.get(i).picture_url;
                        String str = dataList.get(i).modifier;
                        str = str.trim();
                        str = str.replace("\"", "");
                        String[] strArgs = str.substring(1, str.length() - 1).trim().split("\\s*,\\s*");
                        m.modifier = Arrays.asList(strArgs);
                        m.keterangan = dataList.get(i).keterangan;
                        m.quantity = 0;

                        menusList.add(m);
                        menuAdapter.notifyItemChanged(i);
                    }

                    System.out.println("SET MENU" + menusList.size());
                } else {
                    System.out.println("SET MENU ERROR " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<CallbackWrapper> call, Throwable throwable) {
                System.out.println("SET MENU ERROR2 " + throwable.getMessage());
            }
        });
    }
}
