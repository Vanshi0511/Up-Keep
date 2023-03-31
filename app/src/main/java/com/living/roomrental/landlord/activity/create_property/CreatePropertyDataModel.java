package com.living.roomrental.landlord.activity.create_property;

import android.net.Uri;

import com.living.roomrental.utilities.Validation;

import java.util.ArrayList;

public class CreatePropertyDataModel {

    private String propertyName , landmarkAddress, mapLocationAddress , rent ;
    private String size , agreement  , description , peopleFor , type , furnishing , furnishingDescription;

    private Double latitude , longitude ;

    private ArrayList<String> propertyImagesUrl = new ArrayList<>();

    private ArrayList<String> facilities = new ArrayList<>();

    public CreatePropertyDataModel() {
    }

    public CreatePropertyDataModel(String propertyName, String landmarkAddress, String mapLocationAddress, String rent, String size, String agreement, String description, String peopleFor, String type, String furnishing, String furnishingDescription, Double latitude, Double longitude, ArrayList<String> propertyImagesUrl, ArrayList<String> facilities) {
        this.propertyName = propertyName;
        this.landmarkAddress = landmarkAddress;
        this.mapLocationAddress = mapLocationAddress;
        this.rent = rent;
        this.size = size;
        this.agreement = agreement;
        this.description = description;
        this.peopleFor = peopleFor;
        this.type = type;
        this.furnishing = furnishing;
        this.furnishingDescription = furnishingDescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.propertyImagesUrl = propertyImagesUrl;
        this.facilities = facilities;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getLandmarkAddress() {
        return landmarkAddress;
    }

    public void setLandmarkAddress(String landmarkAddress) {
        this.landmarkAddress = landmarkAddress;
    }

    public String getMapLocationAddress() {
        return mapLocationAddress;
    }

    public void setMapLocationAddress(String mapLocationAddress) {
        this.mapLocationAddress = mapLocationAddress;
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

    public String getFurnishing() {
        return furnishing;
    }

    public void setFurnishing(String furnishing) {
        this.furnishing = furnishing;
    }

    public String getFurnishingDescription() {
        return furnishingDescription;
    }

    public void setFurnishingDescription(String furnishingDescription) {
        this.furnishingDescription = furnishingDescription;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public ArrayList<String> getPropertyImages() {
        return propertyImagesUrl;
    }

    public void setPropertyImages(ArrayList<String> images) {
         this.propertyImagesUrl = images;
    }

    public ArrayList<String> getFacilities() {
        return facilities;
    }

    public void setFacilities(ArrayList<String> facilities) {
        this.facilities = facilities;
    }
}
