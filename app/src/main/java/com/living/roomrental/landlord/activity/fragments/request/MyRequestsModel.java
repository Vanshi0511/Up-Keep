package com.living.roomrental.landlord.activity.fragments.request;

public class MyRequestsModel {

    private String description;
    private String uidOfTenant;
    private String nameOfTenant;
    private String propertyName;

    private String propertyKey;
    private String selectedDate;

    public MyRequestsModel() {
    }

    public MyRequestsModel(String description, String uidOfTenant, String nameOfTenant, String propertyName , String selectedDate) {
        this.description = description;
        this.uidOfTenant = uidOfTenant;
        this.nameOfTenant = nameOfTenant;
        this.propertyName = propertyName;
        this.selectedDate = selectedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUidOfTenant() {
        return uidOfTenant;
    }

    public void setUidOfTenant(String uidOfTenant) {
        this.uidOfTenant = uidOfTenant;
    }

    public String getNameOfTenant() {
        return nameOfTenant;
    }

    public void setNameOfTenant(String nameOfTenant) {
        this.nameOfTenant = nameOfTenant;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(String selectedDate) {
        this.selectedDate = selectedDate;
    }
}
