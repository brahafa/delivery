package com.bringit.orders.models;


public class GpsLocation {
    public Double lat;
    public Double longi;
  //  public List<String> orderId;

    public GpsLocation(Double lat, Double longi) {
        this.lat = lat;
        this.longi = longi;
       // this.orderId = orderId;
    }
}
