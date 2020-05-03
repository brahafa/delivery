package com.bringit.orders.models;

import java.io.Serializable;


/**
 * Created by User on 28/08/2017.
 */

public class GlobalObj implements Serializable {

    private String item_id;
    private String item_type;
    private String father_id;
    private String location=null;
    private String cart_id;
    //private String filling;
    private String is_compensation;
    private String item_name;
    private String item_picture;
    private String description;
    private Double price;


    public GlobalObj() {
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    //public String getFilling() {
     //   return filling;
  //  }

//    public void setFilling(String filling) {
//        this.filling = filling;
//    }

    public String getIs_compensation() {
        return is_compensation;
    }

    public void setIs_compensation(String is_compensation) {
        this.is_compensation = is_compensation;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_picture() {
        return item_picture;
    }

    public void setItem_picture(String item_picture) {
        this.item_picture = item_picture;
    }

    public String getCart_id() {
        return cart_id;
    }

    public void setCart_id(String cart_id) {
        this.cart_id = cart_id;
    }

    public String getToppingLocation() {
        return location;
    }

    public void setToppingLocation(String toppingLocation) {
        this.location = toppingLocation;
    }

    public String getFather_id() {
        return father_id;
    }

    public void setFather_id(String father_id) {
        this.father_id = father_id;
    }

    public String getType() {
        return item_type;
    }

    public void setType(String type) {
        this.item_type = type;
    }


    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return "GlobalObj{" +
                "item_id='" + item_id + '\'' +
                ", item_type='" + item_type + '\'' +
                ", father_id='" + father_id + '\'' +
                ", location='" + location + '\'' +
                ", cart_id='" + cart_id + '\'' +
       //         ", filling='" + filling + '\'' +
                ", is_compensation='" + is_compensation + '\'' +
                ", item_name='" + item_name + '\'' +
                ", item_picture='" + item_picture + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                '}';
    }
}
