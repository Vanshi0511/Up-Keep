package com.living.roomrental.tenant.activity.view;

public class PropertyRequestModel {

    private String uidOfUser ;
    private String description;

    public PropertyRequestModel(String uidOfUser, String description) {
        this.uidOfUser = uidOfUser;
        this.description = description;
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
