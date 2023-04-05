package com.living.roomrental.tenant.activity.view;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ViewPropertyTenantViewModel extends ViewModel {

    private ViewPropertyTenantRepository repository;

    public ViewPropertyTenantViewModel(String propertyKey , String uidOfLandlord){
      repository = new ViewPropertyTenantRepository(propertyKey,uidOfLandlord);
    }

    public MutableLiveData<Boolean> isUserRequested(){
        return repository.isUserRequested();
    }

    public MutableLiveData<String> sendRequestToLandlord(PropertyRequestModel model){
        return repository.sendRequest(model);
    }

    public MutableLiveData<String> storeRequestDataToServer(){
        return repository.setTenantRequestData();
    }

}
