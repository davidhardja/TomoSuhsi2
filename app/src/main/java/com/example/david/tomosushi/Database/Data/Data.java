package com.example.david.tomosushi.Database.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 27/10/2017.
 */

public class Data {

    //===Data Universal===//
    public String id;
    public String nama;
    public List<Data> subs;


    public String status;
    //===Data Menu===//

    public String additional;
    public String harga;
    public String Id_category_menu;
    public String keterangan;
    public String qty;
    public String picture_url;
    public List<String> modifier = new ArrayList<>();


}
