package com.living.roomrental.tenant.activity.fragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;

import java.util.List;

public class AllPropertyViewModel extends ViewModel {

    public LiveData<List<CreatePropertyDataModel>> getPropertyFromRepository(){
        AllPropertyRepository repository = new AllPropertyRepository();
        return repository.getAllPropertyFromServer();
    }
}
