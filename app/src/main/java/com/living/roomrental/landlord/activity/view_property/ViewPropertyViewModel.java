package com.living.roomrental.landlord.activity.view_property;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;

public class ViewPropertyViewModel extends ViewModel {

    private ViewPropertyRepository repository = new ViewPropertyRepository();

    public LiveData<String> updateBookingStatusToVacant(String propertyKey){
        return repository.removeTenantFromBooking(propertyKey);
    }

    public LiveData<ProfileModel> getProfileDataFromRepository(String tenantId){
        return repository.getProfileData(tenantId);
    }
}
