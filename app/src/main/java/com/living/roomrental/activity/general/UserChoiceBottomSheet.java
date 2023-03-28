package com.living.roomrental.activity.general;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.living.roomrental.BottomChoiceListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.profile.create.CreateProfileActivity;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;

public class UserChoiceBottomSheet extends BottomSheetDialogFragment {

    private ImageView landlord , tenant;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.choice_bottom_sheet_dialog,container,false);

        landlord = view.findViewById(R.id.landlordImageView);
        tenant = view.findViewById(R.id.tenantImageView);

       return view;
    }

    public void initListeners(BottomChoiceListener listener){
        landlord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickLandlord();
            }
        });

        tenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickTenant();
            }
        });

    }
}
