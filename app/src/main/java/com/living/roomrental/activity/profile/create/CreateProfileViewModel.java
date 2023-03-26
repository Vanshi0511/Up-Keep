package com.living.roomrental.activity.profile.create;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class CreateProfileViewModel extends ViewModel {

    private String name , email , contactNo , occupation , address , bio ,whoIsUser ;
    private Uri imageUri;

    public void setWhoIsUser(String whoIsUser){
        this.whoIsUser = whoIsUser;
    }

    public Uri getImageUri(){
        return imageUri;
    }
    public void setImageUri(Uri imageUri){
    this.imageUri = imageUri;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
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


    public LiveData<String> createOrEditUserProfile(){

        CreateProfileRepository repository =new CreateProfileRepository();
        CreateProfileModel model = new CreateProfileModel(name,contactNo,address,bio,occupation,whoIsUser,null);
        if(imageUri==null){
            return  repository.createProfileToServer(model);
        }else{
            return repository.createProfileToServerWithImage(model , imageUri);
        }
    }

    public LiveData<CreateProfileModel> getProfileData(){
        CreateProfileRepository repository =new CreateProfileRepository();
        return repository.getProfileDataFromServer();
    }
}
