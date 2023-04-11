package com.living.roomrental.tenant.activity.view;

public class PropertyRequestModel {

    private String uidOfUser ;
    private String description;
    private String date;

    public PropertyRequestModel(String uidOfUser, String description , String date) {
        this.uidOfUser = uidOfUser;
        this.description = description;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public PropertyRequestModel() {
    }

    public String getUidOfUser() {
        return uidOfUser;
    }

    public void setUidOfUser(String uidOfUser) {
        this.uidOfUser = uidOfUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
