package com.sinprl.fetchit.data;

public class DataEntry {
    public String name;
    public String address;
    public String mobile;
    public String typeofproduct;
    public String choiceofbank;

    public DataEntry() {
    }

    public DataEntry(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public DataEntry(String name, String address, String mobile, String typeofproduct, String choiceofbank) {
        this.name = name;
        this.address = address;
        this.mobile = mobile;
        this.typeofproduct = typeofproduct;
        this.choiceofbank = choiceofbank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTypeofproduct() {
        return typeofproduct;
    }

    public void setTypeofproduct(String typeofproduct) {
        this.typeofproduct = typeofproduct;
    }

    public String getChoiceofbank() {
        return choiceofbank;
    }

    public void setChoiceofbank(String choiceofbank) {
        this.choiceofbank = choiceofbank;
    }
}
