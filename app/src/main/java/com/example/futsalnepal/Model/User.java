package com.example.futsalnepal.Model;

public class User extends UserId{
    public String user_full_name;
    public String user_profile_image;
    public String user_address;
    public String user_phone_number;

    public User() {
    }

    public User(String user_full_name, String user_profile_image, String user_address, String user_phone_number) {
        this.user_full_name = user_full_name;
        this.user_profile_image = user_profile_image;
        this.user_address = user_address;
        this.user_phone_number = user_phone_number;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        this.user_full_name = user_full_name;
    }

    public String getUser_profile_image() {
        return user_profile_image;
    }

    public void setUser_profile_image(String user_profile_image) {
        this.user_profile_image = user_profile_image;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_phone_number() {
        return user_phone_number;
    }

    public void setUser_phone_number(String user_phone_number) {
        this.user_phone_number = user_phone_number;
    }
}