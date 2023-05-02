package com.living.roomrental.tenant.activity.fragments.request;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.living.roomrental.R;
import com.living.roomrental.databinding.FragmentMyRequestTenantBinding;
import com.living.roomrental.utilities.AppBoiler;

import java.util.ArrayList;
import java.util.List;

public class MyRequestTenantFragment extends Fragment {

    private FragmentMyRequestTenantBinding binding;

    private MyRequestTenantViewModel myRequestTenantViewModel;
    private Dialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        myRequestTenantViewModel = new ViewModelProvider(this).get(MyRequestTenantViewModel.class);

        binding = FragmentMyRequestTenantBinding.inflate(inflater , container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMyRequests();
    }

    private void getMyRequests() {

        if(AppBoiler.isInternetConnected(getContext())){

            progressDialog = AppBoiler.setProgressDialog(getContext());
            LiveData<List<MyRequestTenantModel>> requestsLiveData = myRequestTenantViewModel.getRequestsFromRepository();

            requestsLiveData.observe(getViewLifecycleOwner(), new Observer<List<MyRequestTenantModel>>() {
                @Override
                public void onChanged(List<MyRequestTenantModel> modelList) {

                    if(modelList==null){
                        Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else if(modelList.size() == 0){
                        Toast.makeText(getContext(), "No Requests Yet", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    } else {
                        getPropertyDetails(modelList);
                    }
                }
            });

        } else
            AppBoiler.showSnackBarForInternet(getContext(),binding.getRoot());
    }

    private void getPropertyDetails(List<MyRequestTenantModel> modelList) {

        LiveData<List<MyRequestTenantPropertyModel>> propertyDetailsLiveData = myRequestTenantViewModel.getRequestsPropertyDetailsFromRepository(modelList);

        propertyDetailsLiveData.observe(getViewLifecycleOwner(), new Observer<List<MyRequestTenantPropertyModel>>() {
            @Override
            public void onChanged(List<MyRequestTenantPropertyModel> myRequestTenantPropertyModels) {

                progressDialog.dismiss();
                if(myRequestTenantPropertyModels==null){
                    Toast.makeText(getContext(), "Failed to load", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();

                }  else {

                    setAdapter(myRequestTenantPropertyModels);
                }

            }
        });
    }

    private void setAdapter(List<MyRequestTenantPropertyModel> myRequestTenantPropertyModels) {

        MyRequestTenantAdapter adapter = new MyRequestTenantAdapter(getContext(),myRequestTenantPropertyModels);
        binding.myRequestRecyclerView.setAdapter(adapter);
    }
}