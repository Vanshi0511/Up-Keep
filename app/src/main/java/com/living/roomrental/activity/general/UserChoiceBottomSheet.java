package com.living.roomrental.activity.general;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.card.MaterialCardView;
import com.living.roomrental.BottomChoiceListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.auth.login.LoginActivity;
import com.living.roomrental.activity.profile.create.CreateProfileActivity;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;

public class UserChoiceBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choice_bottom_sheet_dialog,container,false);

        MaterialCardView landlord = view.findViewById(R.id.landlordImageLayout);
        MaterialCardView tenant = view.findViewById(R.id.tenantImageLayout);

        landlord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.WHO_IS_USER, AppConstants.LANDLORD);
                AppBoiler.navigateToActivity(getContext(), CreateProfileActivity.class, bundle);
            }
        });

        tenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.WHO_IS_USER, AppConstants.TENANT);
                AppBoiler.navigateToActivity(getContext(), CreateProfileActivity.class, bundle);
            }
        });

        return view;
    }

    public void initListeners(BottomChoiceListener listener){




    }
}
