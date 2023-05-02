package com.living.roomrental.tenant.activity.fragments.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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
}
