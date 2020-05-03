package com.bringit.orders.models;

import java.io.Serializable;

/**
 * Created by User on 08/04/2018.
 */

public class Address implements Serializable {
    private String order_id;
    private String city;
    private String city_name;
    private String street;
    private String house_num;
    private String floor;
    private String apt_num;
    private String day;
    private String entrance;
    private String phone;

    public Address(){

    }

    public Address(String phone, String day, String order_id, String city,String city_name, String street, String house_num, String floor, String apt_num, String entrance) {
        this.order_id = order_id;
        this.city = city;
        this.city_name = city_name;
        this.street = street;
        this.house_num = house_num;
        this.floor = floor;
        this.apt_num = apt_num;
        this.entrance = entrance;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouse_num() {
        return house_num;
    }

    public void setHouse_num(String house_num) {
        this.house_num = house_num;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getApt_num() {
        return apt_num;
    }

    public void setApt_num(String apt_num) {
        this.apt_num = apt_num;
    }

    public String getEntrance() {
        return entrance;
    }

    public void setEntrance(String entrance) {
        this.entrance = entrance;
    }

    @Override
    public String toString() {
        return "Address{" +
                "city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", house_num='" + house_num + '\'' +
                ", floor='" + floor + '\'' +
                ", apt_num='" + apt_num + '\'' +
                ", entrance='" + entrance + '\'' +
                '}';
    }
}
