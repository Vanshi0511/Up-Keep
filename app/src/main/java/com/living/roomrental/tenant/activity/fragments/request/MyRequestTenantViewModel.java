package com.living.roomrental.tenant.activity.fragments.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.model.ProfileModel;

import java.util.ArrayList;
import java.util.List;

public class MyRequestTenantViewModel extends ViewModel {

    private MyRequestTenantRepository repository = new MyRequestTenantRepository();

    public LiveData<List<MyRequestTenantModel>> getRequestsFromRepository(){
        return repository.getTenantRequests();
    }

    public LiveData<List<MyRequestTenantPropertyModel>> getRequestsPropertyDetailsFromRepository(List<MyRequestTenantModel> modelList){
        return repository.getPropertyDetailsFromPropertyKeys(modelList);
    }

    public LiveData<List<ProfileModel>> getProfileListFromRepository(List<MyRequestTenantPropertyModel> myRequestTenantPropertyModels){
        return repository.getProfileDataFromServer(myRequestTenantPropertyModels);
    }
}
