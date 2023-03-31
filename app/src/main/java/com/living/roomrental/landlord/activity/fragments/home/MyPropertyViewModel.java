package com.living.roomrental.landlord.activity.fragments.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;

import java.util.List;

public class MyPropertyViewModel extends ViewModel {


    public LiveData<List<CreatePropertyDataModel>> getProperty(){

        MyPropertyRepository repository = new MyPropertyRepository();
        return repository.getPropertyFromServer();
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        System.out.println("================= MY PROPERTY VIEW MODEL DESTROYED ===============");
    }
}
