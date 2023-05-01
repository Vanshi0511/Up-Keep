package com.living.roomrental.landlord.activity.create_property;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

public class CreatePropertyViewModel extends ViewModel {

    private String mapLocationAddress ,peopleFor , type , furnishing , bookingStatus ="vacant";
    private Double latitude , longitude ;
    private ArrayList<Uri> propertyImages = new ArrayList<>();

    private ArrayList<String> facilitiesArrayList = new ArrayList<>();

    private ArrayList<String> imagesUrlArrayList = new ArrayList<>();

    public ArrayList<String> getImagesUrlArrayList() {
        return imagesUrlArrayList;
    }

    public void setImagesUrlArrayList(ArrayList<String> imagesUrlArrayList) {
        this.imagesUrlArrayList = imagesUrlArrayList;
    }

    public String getMapLocationAddress() {
        return mapLocationAddress;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public void setMapLocationAddress(String mapLocationAddress) {
        this.mapLocationAddress = mapLocationAddress;
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

    public ArrayList<Uri> getPropertyImages() {
        return propertyImages;
    }

    public void setPropertyImages(Uri propertyImages) {
        this.propertyImages.add(propertyImages);
    }

    public ArrayList<String> getFacilitiesArrayList() {
        return facilitiesArrayList;
    }

    public void setFacilitiesArrayList(String facility) {
        if(!facilitiesArrayList.contains(facility))
            this.facilitiesArrayList.add(facility);
    }

    @Override
    public String toString() {
        return "CreatePropertyViewModel{" +
                "mapLocationAddress='" + mapLocationAddress + '\'' +
                ", peopleFor='" + peopleFor + '\'' +
                ", type='" + type + '\'' +
                ", furnishing='" + furnishing + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", propertyImages=" + propertyImages +
                ", facilitiesArrayList=" + facilitiesArrayList +
                '}';
    }

    private CreatePropertyRepository repository = new CreatePropertyRepository();

    public LiveData<String> setDataOfPropertyToServer(CreatePropertyDataModel model){

        if(propertyImages.size()>0){
            return repository.setDataToServer(model,propertyImages);
        } else {
            return repository.setDataToServerWithoutImage(model);
        }

    }
}
