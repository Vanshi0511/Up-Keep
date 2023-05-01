package com.living.roomrental.landlord.activity.fragments.chat;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatLandlordModel implements Parcelable {

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.occupation);
        dest.writeString(this.imageUrl);
        dest.writeString(this.receiverKey);
    }

    public void readFromParcel(Parcel source) {
        this.name = source.readString();
        this.occupation = source.readString();
        this.imageUrl = source.readString();
        this.receiverKey = source.readString();
    }

    protected ChatLandlordModel(Parcel in) {
        this.name = in.readString();
        this.occupation = in.readString();
        this.imageUrl = in.readString();
        this.receiverKey = in.readString();
    }

    public static final Parcelable.Creator<ChatLandlordModel> CREATOR = new Parcelable.Creator<ChatLandlordModel>() {
        @Override
        public ChatLandlordModel createFromParcel(Parcel source) {
            return new ChatLandlordModel(source);
        }

        @Override
        public ChatLandlordModel[] newArray(int size) {
            return new ChatLandlordModel[size];
        }
    };
}
