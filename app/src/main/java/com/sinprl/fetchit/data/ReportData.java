package com.sinprl.fetchit.data;

import java.util.ArrayList;
import java.util.List;

public class ReportData {

    public int status_new;
    public int status_fow;
    public int status_log;
    public int status_san;
    public int status_dis;
    public int status_otc;
    public int status_pay;
    public List<Profile> filtered_profiles;

    public ReportData() {
        this.status_new = 0;
        this.status_fow = 0;
        this.status_log = 0;
        this.status_san = 0;
        this.status_dis = 0;
        this.status_otc = 0;
        this.status_pay = 0;
        this.filtered_profiles = new ArrayList<>();
    }

    public int getStatus_new() {
        return status_new;
    }

    public void setStatus_new(int status_new) {
        this.status_new = status_new;
    }

    public int getStatus_fow() {
        return status_fow;
    }

    public void setStatus_fow(int status_fow) {
        this.status_fow = status_fow;
    }

    public int getStatus_log() {
        return status_log;
    }

    public void setStatus_log(int status_log) {
        this.status_log = status_log;
    }

    public int getStatus_san() {
        return status_san;
    }

    public void setStatus_san(int status_san) {
        this.status_san = status_san;
    }

    public int getStatus_dis() {
        return status_dis;
    }

    public void setStatus_dis(int status_dis) {
        this.status_dis = status_dis;
    }

    public int getStatus_otc() {
        return status_otc;
    }

    public void setStatus_otc(int status_otc) {
        this.status_otc = status_otc;
    }

    public int getStatus_pay() {
        return status_pay;
    }

    public void setStatus_pay(int status_pay) {
        this.status_pay = status_pay;
    }
}
