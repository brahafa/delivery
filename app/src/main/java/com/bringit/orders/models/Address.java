package com.bringit.orders.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Address implements Parcelable {

    @SerializedName("order_id")
    private String mOrderId;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("time")
    private String mTime;
    @SerializedName("day")
    private String mDay;
    @SerializedName("city_name")
    private String mCityName;
    @SerializedName("street")
    private String mStreet;
    @SerializedName("house_num")
    private String mHouseNum;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("status_text")
    private String mStatusText;


    protected Address(Parcel in) {
        mOrderId = in.readString();
        mStatus = in.readString();
        mTime = in.readString();
        mDay = in.readString();
        mCityName = in.readString();
        mStreet = in.readString();
        mHouseNum = in.readString();
        mPhone = in.readString();
        mStatusText = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOrderId);
        dest.writeString(mStatus);
        dest.writeString(mTime);
        dest.writeString(mDay);
        dest.writeString(mCityName);
        dest.writeString(mStreet);
        dest.writeString(mHouseNum);
        dest.writeString(mPhone);
        dest.writeString(mStatusText);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getOrderId() {
        return mOrderId;
    }

    public void setOrderId(String orderId) {
        mOrderId = orderId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getDay() {
        return mDay;
    }

    public void setDay(String day) {
        mDay = day;
    }

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String street) {
        mStreet = street;
    }

    public String getHouseNum() {
        return mHouseNum;
    }

    public void setHouseNum(String houseNum) {
        mHouseNum = houseNum;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public String getStatusText() {
        return mStatusText;
    }

    public void setStatusText(String statusText) {
        mStatusText = statusText;
    }
}
