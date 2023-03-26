package com.living.roomrental.activity.profile.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class CreateProfileViewModel extends ViewModel {

    private String name , email , contactNo , occupation , address , bio ,whoIsUser;

//    private MutableLiveData<String> success = new MutableLiveData<>();
//
//    private MutableLiveData<String> failure = new MutableLiveData<>();

    public void setWhoIsUser(String whoIsUser){
        this.whoIsUser = whoIsUser;
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

//    public LiveData<String> getSuccess() {
//        return success;
//    }
//
//    public LiveData<String> getFailure() {
//        return failure;
//    }

    public LiveData<String> createOrEditUserProfile(){

        CreateProfileModel model = new CreateProfileModel(name,contactNo,address,bio,occupation,whoIsUser);

        CreateProfileRepository repository =new CreateProfileRepository();
        return repository.createProfileToServer(model);
    }
}
