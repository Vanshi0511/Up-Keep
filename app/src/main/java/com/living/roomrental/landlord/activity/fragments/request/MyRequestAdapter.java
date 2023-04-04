package com.living.roomrental.landlord.activity.fragments.request;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.living.roomrental.R;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;
import com.living.roomrental.landlord.activity.view_property.ViewPropertyLandlordActivity;
import com.living.roomrental.tenant.activity.view.PropertyRequestModel;
import com.living.roomrental.utilities.AppBoiler;

import java.util.ArrayList;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestAdapter.ViewHolder> {

    private Context context;
    private ArrayList<PropertyRequestModel> modelArrayList;

    public MyRequestAdapter(Context context) {
        this.context = context;
    }

    public void setPropertyList(ArrayList<PropertyRequestModel> modelList) {
        this.modelArrayList = modelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_my_property, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PropertyRequestModel model = modelArrayList.get(position);



        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
               // bundle.putParcelable("data", model);
                AppBoiler.navigateToActivity(context, ViewPropertyLandlordActivity.class, bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView propertyName, descriptionTextView;

        private MaterialCardView itemLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            propertyImage = itemView.findViewById(R.id.propertyImageView);
//            propertyName = itemView.findViewById(R.id.propertyName);
//            propertyAddress = itemView.findViewById(R.id.propertyAddress);
//            propertyRent = itemView.findViewById(R.id.propertyRent);
//            propertyType = itemView.findViewById(R.id.propertyType);
//            itemLayout = itemView.findViewById(R.id.itemLayout);
        }
    }

}
