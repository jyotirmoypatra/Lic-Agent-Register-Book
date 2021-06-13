package com.jyotirmoy.licagentregister;

public class PolicyHolderDetailsModel {
   private String uid;
    private String name;
    private String phone;
    private String address;
    private String dob;
    private String premium;
    private String policyTableTerm;
    private String doc;
    private String dateMaturity;
    private String dateLastPayment;

    public PolicyHolderDetailsModel() {
    }

    public PolicyHolderDetailsModel(String uid, String name, String phone, String address, String dob, String premium, String policyTableTerm, String doc, String dateMaturity, String dateLastPayment) {
        this.uid = uid;
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.dob = dob;
        this.premium = premium;
        this.policyTableTerm = policyTableTerm;
        this.doc = doc;
        this.dateMaturity = dateMaturity;
        this.dateLastPayment = dateLastPayment;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getPremium() {
        return premium;
    }

    public void setPremium(String premium) {
        this.premium = premium;
    }

    public String getPolicyTableTerm() {
        return policyTableTerm;
    }

    public void setPolicyTableTerm(String policyTableTerm) {
        this.policyTableTerm = policyTableTerm;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getDateMaturity() {
        return dateMaturity;
    }

    public void setDateMaturity(String dateMaturity) {
        this.dateMaturity = dateMaturity;
    }

    public String getDateLastPayment() {
        return dateLastPayment;
    }

    public void setDateLastPayment(String dateLastPayment) {
        this.dateLastPayment = dateLastPayment;
    }
}
