package com.living.roomrental.landlord.activity.fragments.home;

import android.app.Dialog;
import android.content.Context;
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
import com.living.roomrental.databinding.FragmentMyPropertyBinding;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;
import com.living.roomrental.landlord.activity.main.LandlordMainActivity;
import com.living.roomrental.utilities.AppBoiler;

import java.util.ArrayList;
import java.util.List;


public class MyPropertyFragment extends Fragment {

    private FragmentMyPropertyBinding binding;

    private MyPropertyViewModel myPropertyViewModel;
    private Context context;

    private MyPropertyAdapter adapter;

    private Dialog progressDialog , responseDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentMyPropertyBinding.inflate(inflater,container,false);

        myPropertyViewModel = new ViewModelProvider(this).get(MyPropertyViewModel.class);
        context = getActivity();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = AppBoiler.setProgressDialog(getContext());
       observeDataFromServer();
    }

    public void observeDataFromServer(){

        LiveData<List<CreatePropertyDataModel>> propertyModel = myPropertyViewModel.getProperty();

        propertyModel.observe(getViewLifecycleOwner(), new Observer<List<CreatePropertyDataModel>>() {
            @Override
            public void onChanged(List<CreatePropertyDataModel> createPropertyDataModels) {

                progressDialog.dismiss();

                if(createPropertyDataModels != null){

                    adapter = new MyPropertyAdapter(getActivity());
                    adapter.setPropertyList((ArrayList<CreatePropertyDataModel>) createPropertyDataModels);
                    binding.myPropertyRecyclerView.setAdapter(adapter);
                }
                else {
                    binding.createNewPropertyTextView.setVisibility(View.VISIBLE);
                  System.out.println("=========== NO PROPERTY FOUND ================");
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        System.out.println("========================= on resume");
        //observeDataFromServer();
    }
}