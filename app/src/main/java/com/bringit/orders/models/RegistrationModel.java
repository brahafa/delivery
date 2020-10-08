package com.bringit.orders.models;

import com.google.gson.annotations.SerializedName;

public class RegistrationModel {


    @SerializedName("fname")
    private String mName;
    @SerializedName("lname")
    private String mLastName;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("identity")
    private String mIdentity;
    @SerializedName("password")
    private String mPassword;
    @SerializedName("confirmPass")
    private String mConfirmPass;

    @SerializedName("self_image")
    private String mSelfImage;
    @SerializedName("identity_image")
    private String mIdentityImage;

    @SerializedName("address")
    private AddressBean mAddress;

    public RegistrationModel() {
        this.mName = "";
        this.mLastName = "";
        this.mPhone = "";
        this.mAddress = new AddressBean();
    }

    public String getName() {
        return mName;
    }

    public void setName(String fName) {
        mName = fName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lName) {
        mLastName = lName;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public AddressBean getAddress() {
        return mAddress;
    }

    public void setAddress(AddressBean address) {
        mAddress = address;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmIdentity() {
        return mIdentity;
    }

    public void setmIdentity(String mIdentity) {
        this.mIdentity = mIdentity;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmConfirmPass() {
        return mConfirmPass;
    }

    public void setmConfirmPass(String mConfirmPass) {
        this.mConfirmPass = mConfirmPass;
    }

    public String getmSelfImage() {
        return mSelfImage;
    }

    public void setmSelfImage(String mSelfImage) {
        this.mSelfImage = mSelfImage;
    }

    public String getmIdentityImage() {
        return mIdentityImage;
    }

    public void setmIdentityImage(String mIdentityImage) {
        this.mIdentityImage = mIdentityImage;
    }

    public static class NotesBean {
        @SerializedName("order")
        private String mOrder;

        public NotesBean() {
            this.mOrder = "";
        }

        public String getOrder() {
            return mOrder;
        }

        public void setOrder(String order) {
            mOrder = order;
        }
    }

    public static class AddressBean {
        @SerializedName("city")
        private String mCityName;
        @SerializedName("street")
        private String mStreet;
        @SerializedName("houseNumber")
        private String mHouseNum;
        @SerializedName("apartment")
        private String mAptNum;

        public AddressBean() {
            this.mCityName = "";
            this.mStreet = "";
            this.mHouseNum = "";
            this.mAptNum = "";
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

        public String getAptNum() {
            return mAptNum;
        }

        public void setAptNum(String aptNum) {
            mAptNum = aptNum;
        }

    }
}
