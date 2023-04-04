package com.living.roomrental.tenant.activity.view;

public class PropertyRequestModel {

    private String uidOfUser ;
    private String description;

    public PropertyRequestModel() {
    }

    public PropertyRequestModel(String description) {
        this.description = description;
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
