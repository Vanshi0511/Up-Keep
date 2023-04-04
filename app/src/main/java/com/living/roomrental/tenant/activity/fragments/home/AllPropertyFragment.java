package com.living.roomrental.tenant.activity.fragments.home;

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

import com.living.roomrental.databinding.FragmentAllPropertyBinding;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;
import com.living.roomrental.landlord.activity.fragments.home.MyPropertyAdapter;
import com.living.roomrental.utilities.AppBoiler;

import java.util.ArrayList;
import java.util.List;

public class AllPropertyFragment extends Fragment {

    private FragmentAllPropertyBinding binding;

    private AllPropertyAdapter adapter;
    private Dialog progressDialog;
    private AllPropertyViewModel allPropertyViewModel;
    private List<CreatePropertyDataModel> createPropertyDataModelList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllPropertyBinding.inflate(inflater, container, false);

        allPropertyViewModel = new ViewModelProvider(this).get(AllPropertyViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setAdapter();
        progressDialog = AppBoiler.setProgressDialog(getContext());
        observeDataFromServer();
    }

    private void setAdapter(){
        adapter = new AllPropertyAdapter(getActivity());
        adapter.setPropertyList((ArrayList<CreatePropertyDataModel>) createPropertyDataModelList);
        binding.allPropertyRecyclerView.setAdapter(adapter);
    }

    private void observeDataFromServer(){

        LiveData<List<CreatePropertyDataModel>> modelList = allPropertyViewModel.getPropertyFromRepository();

        modelList.observe(getViewLifecycleOwner(), new Observer<List<CreatePropertyDataModel>>() {
            @Override
            public void onChanged(List<CreatePropertyDataModel> createPropertyDataModels) {
                progressDialog.dismiss();

                if(createPropertyDataModels!=null){
                    createPropertyDataModelList = createPropertyDataModels;
                    adapter.setPropertyList((ArrayList<CreatePropertyDataModel>) createPropertyDataModelList);
                    adapter.notifyDataSetChanged();
                    System.out.println("============= DATA FOUND ===========" +createPropertyDataModels.size());
                } else {
                    System.out.println("============= NO PROPERTY DATA FOUND ===========");
                }
            }
        });
    }
}