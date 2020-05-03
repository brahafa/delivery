package com.bringit.orders.utils;

/**
 * Created by User on 03/04/2018.
 */

public class UserDetails {
    private static UserDetails instance = null;
    private String utoken;
    private Boolean isRefresh=true;

    private UserDetails() {
        // Exists only to defeat instantiation.
    }

    public static UserDetails getInstance() {
        if(instance == null) {
            instance = new UserDetails();
        }
        return instance;
    }

    public Boolean getRefresh() {
        return isRefresh;
    }

    public void setRefresh(Boolean refresh) {
        isRefresh = refresh;
    }

    public String getToken() {
        return utoken;
    }

    public void setToken(String token) {
        this.utoken = token;
    }

}
