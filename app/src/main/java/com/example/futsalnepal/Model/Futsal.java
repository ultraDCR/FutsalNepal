package com.example.futsalnepal.Model;

public class Futsal extends com.example.futsalnepal.Model.FutsalId {

    public String futsal_name;
    public String futsal_address;
    public String opening_hour;
    public String closing_hour;
    public String futsal_phone;
    public String futsal_logo;
    public float rating;

    public  Futsal(){}
    public Futsal( String futsal_name, String futsal_address, String opening_hour, String closing_hour, String futsal_phone, String futsal_logo) {

        this.futsal_name = futsal_name;
        this.futsal_address = futsal_address;
        this.opening_hour = opening_hour;
        this.closing_hour = closing_hour;
        this.futsal_phone = futsal_phone;
        this.futsal_logo = futsal_logo;

    }

    public String getFutsal_name() {
        return futsal_name;
    }

    public void setFutsal_name(String futsal_name) {
        this.futsal_name = futsal_name;
    }

    public String getFutsal_address() {
        return futsal_address;
    }

    public void setFutsal_address(String futsal_address) {
        this.futsal_address = futsal_address;
    }

    public String getOpening_hour() {
        return opening_hour;
    }

    public void setOpening_hour(String opening_hour) {
        this.opening_hour = opening_hour;
    }

    public String getClosing_hour() {
        return closing_hour;
    }

    public void setClosing_hour(String closing_hour) {
        this.closing_hour = closing_hour;
    }

    public String getFutsal_phone() {
        return futsal_phone;
    }

    public void setFutsal_phone(String futsal_phone) {
        this.futsal_phone = futsal_phone;
    }

    public String getFutsal_logo() {
        return futsal_logo;
    }

    public void setFutsal_logo(String futsal_logo) {
        this.futsal_logo = futsal_logo;
    }


}