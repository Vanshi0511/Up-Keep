package com.living.roomrental.activity.profile.edit;


import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.create.CreateProfileModel;

public class EditProfileViewModel extends ViewModel {

    private EditProfileRepository repository;
    private String name , email , contactNo , occupation , address , bio ;
    private Uri imageUri;
    private String imageUrlFromDatabase;

    public void setImageUrl(String imageUrl){
        this.imageUrlFromDatabase = imageUrl;
    }

    public EditProfileViewModel(){
        repository = new EditProfileRepository();
    }
    private MutableLiveData<CreateProfileModel> modelMutableLiveData;

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

    public LiveData<CreateProfileModel> getProfileData(){
       return repository.getDataFromServer();
    }

    public LiveData<String> setProfileData(){
        if(imageUrlFromDatabase!=null && imageUri==null){
           repository.deleteImageFromServer(imageUrlFromDatabase);
        }
        CreateProfileModel model = new CreateProfileModel(name,contactNo,address,bio,occupation,imageUri.toString());
        return repository.updateDataToServer(model);
    }

}
