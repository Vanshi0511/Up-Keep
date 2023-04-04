package com.living.roomrental.landlord.activity.create_property;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrentBookingModel implements Parcelable {

    private String tenantId ;
    private String bookingDate;

    public String getTenantId() {
        return tenantId;
    }

    public CurrentBookingModel(String tenantId, String bookingDate) {
        this.tenantId = tenantId;
        this.bookingDate = bookingDate;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tenantId);
        dest.writeString(this.bookingDate);
    }

    public void readFromParcel(Parcel source) {
        this.tenantId = source.readString();
        this.bookingDate = source.readString();
    }

    public CurrentBookingModel() {
    }

    protected CurrentBookingModel(Parcel in) {
        this.tenantId = in.readString();
        this.bookingDate = in.readString();
    }

    public static final Parcelable.Creator<CurrentBookingModel> CREATOR = new Parcelable.Creator<CurrentBookingModel>() {
        @Override
        public CurrentBookingModel createFromParcel(Parcel source) {
            return new CurrentBookingModel(source);
        }

        @Override
        public CurrentBookingModel[] newArray(int size) {
            return new CurrentBookingModel[size];
        }
    };
}
