package com.example.david.tomosushi.Database.Data;

import com.j256.ormlite.field.DatabaseField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by David on 28/10/2017.
 */

public class Menus {

    @DatabaseField(id = true)
    public String id;
    @DatabaseField
    public String name;
    @DatabaseField
    public Integer harga;
    @DatabaseField
    public Integer id_category_menu;
    @DatabaseField
    public Integer quantity;
    @DatabaseField
    public String keterangan = "";
    @DatabaseField
    public String picture_url;
    @DatabaseField
    public String additional;
    @DatabaseField
    public List<String> modifier = new ArrayList<>();
    @DatabaseField
    public String mod = "";

    public String getMod() {
        return mod;
    }

    public void setMod(String mod) {
        this.mod = mod;
    }

    public List<String> getModifier() {
        return modifier;
    }

    public void setModifier(List<String> modifier) {
        this.modifier = modifier;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getHarga() {
        return harga;
    }

    public void setHarga(Integer harga) {
        this.harga = harga;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
