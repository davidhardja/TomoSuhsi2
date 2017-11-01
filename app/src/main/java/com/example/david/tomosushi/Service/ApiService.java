package com.example.david.tomosushi.Service;

import com.example.david.tomosushi.Database.Data.CallbackWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by David on 27/10/2017.
 */

public interface ApiService {

    @POST("public/api/login")
    Call<CallbackWrapper> login(
            @Query("password") String password
    );

    @GET("public/api/cekstatusmeja/{no_meja}")
    Call<CallbackWrapper> getMejaStatus(
            @Path("no_meja") String noMeja
    );

    @PUT("public/api/meja")
    Call<CallbackWrapper> openTable(
            @Query("updatedKey") String status,
            @Query("updatedValue") String value2,
            @Query("no_meja") String noMeja
    );

    @GET("public/api/categories")
    Call<CallbackWrapper> getAllCategory(

    );

    @GET("public/api/menu/{id}")
    Call<CallbackWrapper> getMenuCategory(
            @Path("id") String id
    );

    @POST("public/api/order")
    Call<CallbackWrapper> postOrder(
            @Query("no_meja") String idMeja,
            @Query("total_qty") String totalQty,
            @Query("creator") String creator,
            @Query("id_menu") String idMenu,
            @Query("qty") String qty,
            @Query("modifiermenu") String keterangan,
            @Query("persons") Integer persons,
            @Query("jenis") String jenis,
            @Query("takeaway") Integer takeaway
    );

    @GET("public/api/order/{no_meja}")
    Call<CallbackWrapper> getOrder(
            @Path("no_meja") String noMeja
    );

    @PUT("public/api/meja")
    Call<CallbackWrapper> closeOrder(
            @Query("updatedKey") String status,
            @Query("updatedValue") String value4,
            @Query("no_meja") String noMeja
    );

    @GET("public/api/getbon/{no_meja}/yes/{creator}")
    Call<CallbackWrapper> getReceipt(
            @Path("no_meja") String noMeja,
            @Path("creator") String creator
    );

    @GET("public/api/callwaiter/{id}")
    Call<CallbackWrapper> callWaiter(
            @Path("id") String noMeja
    );
}
