package com.living.roomrental.landlord.activity.fragments.request;

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

import com.living.roomrental.databinding.FragmentRequestBinding;
import com.living.roomrental.tenant.activity.view.PropertyRequestModel;

import java.util.ArrayList;
import java.util.List;

public class MyRequestFragment extends Fragment {
    ;

    private FragmentRequestBinding binding;
    private MyRequestViewModel myRequestViewModel;

    private MyRequestAdapter adapter;

    private ArrayList<PropertyRequestModel> propertyRequestModelList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRequestBinding.inflate(inflater, container, false);

        myRequestViewModel = new ViewModelProvider(this).get(MyRequestViewModel.class);
        //setAdapter();
        getRequestDataFromServer();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private void setAdapter(){
        if(adapter==null)
            adapter = new MyRequestAdapter(getContext());

        binding.myRequestRecyclerView.setAdapter(adapter);
    }
    private void getRequestDataFromServer(){

        LiveData<List<PropertyRequestModel>> listLiveData = myRequestViewModel.getRequest();
        listLiveData.observe(getViewLifecycleOwner(), new Observer<List<PropertyRequestModel>>() {
            @Override
            public void onChanged(List<PropertyRequestModel> propertyRequestModels) {
                if(propertyRequestModels!=null){

                }
                else {
                    System.out.println("+============ No data found ===================");
                }
            }
        });
    }
}