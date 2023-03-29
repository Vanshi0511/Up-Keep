package com.living.roomrental.landlord.activity.create_property;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CreatePropertyViewModel extends ViewModel {

    private String propertyName , streetAddress , pincodeAddress , landmarkAddress, mapLocation , rent ;
    private String size , agreement  , description , peopleFor , type , furnishing;

    private ArrayList<String> facilities;

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPincodeAddress() {
        return pincodeAddress;
    }

    public void setPincodeAddress(String pincodeAddress) {
        this.pincodeAddress = pincodeAddress;
    }

    public String getLandmarkAddress() {
        return landmarkAddress;
    }

    public void setLandmarkAddress(String landmarkAddress) {
        this.landmarkAddress = landmarkAddress;
    }

    public String getMapLocation() {
        return mapLocation;
    }

    public void setMapLocation(String mapLocation) {
        this.mapLocation = mapLocation;
    }

    public String getRent() {
        return rent;
    }

    public void setRent(String rent) {
        this.rent = rent;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAgreement() {
        return agreement;
    }

    public void setAgreement(String agreement) {
        this.agreement = agreement;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPeopleFor() {
        return peopleFor;
    }

    public void setPeopleFor(String peopleFor) {
        this.peopleFor = peopleFor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
