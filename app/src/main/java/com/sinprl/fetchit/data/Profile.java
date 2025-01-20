package com.sinprl.fetchit.data;

public class Profile {
    public String id;
    public String name;
    public String address;
    public String mobile;
    public String typeofproduct;
    public String choiceofbank;
    public String amount;
    public String status;
    public String entry_date;
    public String code;
    public String reference;
    public String bankmanager;

    public Profile() {
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() { return mobile; }

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getBankmanager() {
        return bankmanager;
    }

    public void setBankmanager(String bankmanager) {
        this.bankmanager = bankmanager;
    }
}
