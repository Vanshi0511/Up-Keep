package com.living.roomrental.landlord.activity.fragments.request;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.tenant.activity.view.PropertyRequestModel;

import java.util.List;

public class MyRequestViewModel extends ViewModel {

    public LiveData<List<PropertyRequestModel>> getRequest(){
        MyRequestRepository repository = new MyRequestRepository();
        return repository.getRequestFromServer();
    }
}
