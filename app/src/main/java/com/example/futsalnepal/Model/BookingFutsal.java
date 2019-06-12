package com.example.futsalnepal.Model;

import java.util.Map;

public class BookingFutsal {
    public String futsal_id;
    public String futsal_name;
    public Map<String, Object> location;
    public String opening_hour;
    public String closing_hour;
    public String futsal_phone;
    public String futsal_logo;
    public float overall_rating;
    public String time;

    public BookingFutsal() {
    }



    public BookingFutsal(String futsal_id, String futsal_name,  Map<String, Object> location, String opening_hour, String closing_hour, String futsal_phone, float overall_rating, String time) {

        this.futsal_name = futsal_name;
        this.location = location;
        this.opening_hour = opening_hour;
        this.closing_hour = closing_hour;
        this.futsal_phone = futsal_phone;
        this.futsal_logo = futsal_logo;
        this.overall_rating = overall_rating;

    }

    public String getFutsal_name() {
        return futsal_name;
    }

    public void setFutsal_name(String futsal_name) {
        this.futsal_name = futsal_name;
    }

    public Map<String, Object> getLocation() {
        return location;
    }

    public void setLocation(Map<String, Object> location) {
        this.location = location;
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

    public String getFutsal_id() {
        return futsal_id;
    }

    public void setFutsal_id(String futsal_id) {
        this.futsal_id = futsal_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
    public float getOverall_rating() {
        return overall_rating;
    }

    public void setOverall_rating(float overall_rating) {
        this.overall_rating = overall_rating;
    }
}