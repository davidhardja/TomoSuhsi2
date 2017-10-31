package com.example.david.tomosushi.Database.Data;

import java.util.List;

/**
 * Created by David on 27/10/2017.
 */

public class CallbackWrapper {

    private String code;
    private String status;
    private List<Data> data;
    private DataBon databon;
    private String status_meja;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public DataBon getDatabon() {
        return databon;
    }

    public void setDatabon(DataBon databon) {
        this.databon = databon;
    }

    public String getStatus_meja() {
        return status_meja;
    }

    public void setStatus_meja(String status_meja) {
        this.status_meja = status_meja;
    }
}
