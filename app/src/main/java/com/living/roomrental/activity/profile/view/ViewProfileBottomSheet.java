package com.living.roomrental.activity.profile.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.R;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.activity.profile.edit.EditProfileActivity;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.Validation;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileBottomSheet extends BottomSheetDialogFragment {

    private ViewProfileViewModel viewProfileViewModel;
    private MaterialCardView cardViewEditProfile;
    private ProfileModel model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_view_profile,container,false);

        viewProfileViewModel = new ViewModelProvider(this).get(ViewProfileViewModel.class);

        cardViewEditProfile = view.findViewById(R.id.editProfileCardView);
        getDataFromServer(view);
        initListener();
        return view;
    }

    private void initListener(){

        cardViewEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putParcelable("data",model);
                AppBoiler.navigateToActivity(getContext(), EditProfileActivity.class,bundle);
            }
        });
    }
    private void getDataFromServer(View view) {

        LiveData<ProfileModel> modelLiveData = viewProfileViewModel.getProfileData(getContext());

        modelLiveData.observe(this, new Observer<ProfileModel>() {
            @Override
            public void onChanged(ProfileModel profileModel) {

                if(profileModel !=null){
                    model = profileModel;
                    System.out.println("======== model data======"+profileModel.toString());
                    setData(view);
                }else{
                    System.out.println("================= NO DATA FOUND =================");
                }
            }
        });
    }

    private void setData(View view) {
        TextView nameTextView , contactTextView ,emailTextView ,addressTextView ,aboutTextView ;
        CircleImageView imageView;

        nameTextView = view.findViewById(R.id.nameTextView);
        contactTextView = view.findViewById(R.id.contactTextView);
        emailTextView = view.findViewById(R.id.emailTextView);
        addressTextView = view.findViewById(R.id.addressTextView);
        aboutTextView = view.findViewById(R.id.aboutTextView);
        imageView = view.findViewById(R.id.profileImage);

        nameTextView.setText(model.getName()+" ("+model.getOccupation()+")");
        contactTextView.setText(model.getContactNo());
        addressTextView.setText(model.getAddress());
        aboutTextView.setText(model.getBio());
        emailTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        System.out.println("=========== image     "+model.getImageUrl());
        if(!Validation.isStringEmpty(model.getImageUrl())){
            Glide.with(this).load(model.getImageUrl()).into(imageView);
        }
    }
}
