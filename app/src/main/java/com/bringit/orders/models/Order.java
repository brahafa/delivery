package com.bringit.orders.models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 08/04/2018.
 */

public class Order implements Serializable {

    private String order_id;
    private String business_id;
    private String time;
    private String total_paid;
    private String order_notes;
    private String phone;
    private String name;
    private String payment_method;
    private String payment_display;
    private List<GlobalObj> order_items;
    private String pizza_count;
    private Address address;
    private int position;

    public Order(int position, String pizza_count,String order_id, String business_id, String time, String total_paid, String order_notes, String phone, String name, String payment_method, String payment_display, List<GlobalObj> order_items, Address address) {
        this.order_id = order_id;
        this.business_id = business_id;
        this.time = time;
        this.total_paid = total_paid;
        this.order_notes = order_notes;
        this.phone = phone;
        this.name = name;
        this.payment_method = payment_method;
        this.payment_display = payment_display;
        this.order_items = order_items;
        this.address = address;
        this.pizza_count = pizza_count;
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getPizza_count() {
        return pizza_count;
    }

    public void setPizza_count(String pizza_count) {
        this.pizza_count = pizza_count;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTotal_paid() {
        return total_paid;
    }

    public void setTotal_paid(String total_paid) {
        this.total_paid = total_paid;
    }

    public String getOrder_notes() {
        return order_notes;
    }

    public void setOrder_notes(String order_notes) {
        this.order_notes = order_notes;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getPayment_display() {
        return payment_display;
    }

    public void setPayment_display(String payment_display) {
        this.payment_display = payment_display;
    }

    public List<GlobalObj> getOrder_items() {
        return order_items;
    }

    public void setOrder_items(List<GlobalObj> order_items) {
        this.order_items = order_items;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Order{" +
                "order_id='" + order_id + '\'' +
                ", business_id='" + business_id + '\'' +
                ", time='" + time + '\'' +
                ", total_paid='" + total_paid + '\'' +
                ", order_notes='" + order_notes + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", payment_method='" + payment_method + '\'' +
                ", payment_display='" + payment_display + '\'' +
                ", order_items=" + order_items +
                ", address=" + address +
                '}';
    }
}
