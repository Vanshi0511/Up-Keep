package com.living.roomrental.activity.general;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.living.roomrental.R;
import com.living.roomrental.activity.auth.login.LoginActivity;
import com.living.roomrental.activity.profile.create.CreateProfileActivity;
import com.living.roomrental.utilities.AppBoiler;

public class BottomSheetChoiceFragment extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.choice_bottom_sheet_dialog,container,false);

        ImageView landlord , tenant ;
        landlord = view.findViewById(R.id.landlordImageView);
        tenant = view.findViewById(R.id.tenantImageView);

        landlord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("whoIsUser","Landlord");
                bundle.putString("intentFor","createProfile");
                AppBoiler.navigateToActivity(getContext(), CreateProfileActivity.class,bundle);
            }
        });

        tenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "tenant", Toast.LENGTH_SHORT).show();
            }
        });
       return view;
    }
}
