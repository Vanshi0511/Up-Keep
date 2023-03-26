package com.living.roomrental.activity.profile.create;

public class CreateProfileModel {
    private String name;
    private String contactNo;
    private String address;
    private String bio;
    private String occupation;
    private String whoIsUser;
    private String imageUrl;

    public CreateProfileModel() {
    }

    public CreateProfileModel(String name, String contactNo, String address, String bio, String occupation, String whoIsUser , String imageUrl) {
        this.name = name;
        this.contactNo = contactNo;
        this.address = address;
        this.bio = bio;
        this.occupation = occupation;
        this.whoIsUser = whoIsUser;
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getWhoIsUser() {
        return whoIsUser;
    }

    public void setWhoIsUser(String whoIsUser) {
        this.whoIsUser = whoIsUser;
    }
}
