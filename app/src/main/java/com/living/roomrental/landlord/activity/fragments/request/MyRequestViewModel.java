package com.living.roomrental.landlord.activity.fragments.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.tenant.activity.view.PropertyRequestModel;

import java.util.List;

public class MyRequestViewModel extends ViewModel {

    private MyRequestRepository repository = new MyRequestRepository();

    public LiveData<List<MyRequestsModel>> getRequest(){
        return repository.getRequestFromServer();
    }

    public LiveData<List<ProfileModel>> getProfileDataOfTenant(List<MyRequestsModel> myRequestsModelList){
        return repository.getProfileDataFromServer(myRequestsModelList);
    }

    public LiveData<String> removePropertyRequest(String uidOfTenant , String propertyKey){
        return repository.deleteRequestFromProperty(uidOfTenant,propertyKey);
    }

    public LiveData<String> deleteAllRequest(String propertyKey){
        return repository.deleteAllRequests(propertyKey);
    }

    public LiveData<String> updateBooking(MyRequestsModel model){
        return repository.updateBookingStatus(model);
    }

}
