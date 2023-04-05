package com.living.roomrental.tenant.activity.view;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ViewPropertyTenantViewModelFactory implements ViewModelProvider.Factory {

    private String propertyKey;
    private String uidOfLandlord ;

    public ViewPropertyTenantViewModelFactory(String propertyKey , String uidOfLandlord ){
        this.propertyKey = propertyKey;
        this.uidOfLandlord = uidOfLandlord;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        ViewPropertyTenantViewModel viewPropertyTenantViewModel = new ViewPropertyTenantViewModel(propertyKey,uidOfLandlord);
        return (T)viewPropertyTenantViewModel;
    }
}
