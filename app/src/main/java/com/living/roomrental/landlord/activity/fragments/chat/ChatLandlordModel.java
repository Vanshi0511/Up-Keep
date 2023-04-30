package com.living.roomrental.landlord.activity.fragments.chat;

public class ChatLandlordModel {

    private String name;
    private String occupation;

    private String imageUrl;

    private String receiverKey;

    public ChatLandlordModel(String name, String occupation ,String imageUrl, String receiverKey) {
        this.name = name;
        this.occupation = occupation;
        this.imageUrl = imageUrl;
        this.receiverKey = receiverKey;
    }

    public ChatLandlordModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getReceiverKey() {
        return receiverKey;
    }

    public void setReceiverKey(String receiverKey) {
        this.receiverKey = receiverKey;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
