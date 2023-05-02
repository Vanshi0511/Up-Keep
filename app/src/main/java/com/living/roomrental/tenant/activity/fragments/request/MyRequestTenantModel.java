package com.living.roomrental.tenant.activity.fragments.request;

public class MyRequestTenantModel {

    private String propertyKey;
    private String uidOfLandlord;

    private String status;

    public MyRequestTenantModel() {
    }

    public MyRequestTenantModel(String propertyKey, String uidOfLandlord ,String status) {
        this.propertyKey = propertyKey;
        this.uidOfLandlord = uidOfLandlord;
        this.status = status;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getUidOfLandlord() {
        return uidOfLandlord;
    }

    public void setUidOfLandlord(String uidOfLandlord) {
        this.uidOfLandlord = uidOfLandlord;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
