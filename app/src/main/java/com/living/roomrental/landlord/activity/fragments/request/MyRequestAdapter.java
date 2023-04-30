package com.living.roomrental.landlord.activity.fragments.request;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.living.roomrental.ContactListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.comman.chat.ChatActivity;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;
import com.living.roomrental.landlord.activity.view_property.ViewPropertyLandlordActivity;
import com.living.roomrental.tenant.activity.view.PropertyRequestModel;
import com.living.roomrental.utilities.AppBoiler;

import java.util.ArrayList;

public class MyRequestAdapter extends RecyclerView.Adapter<MyRequestAdapter.ViewHolder> {

    private Context context;
    private ArrayList<MyRequestsModel> modelArrayList;
    private ArrayList<ProfileModel> profileModelArrayList;

    private ConfirmationListener confirmationListener;
    private ContactListener contactListener;
    public MyRequestAdapter(Context context ) {
        this.context = context;
    }
    public void setRequestList(ArrayList<MyRequestsModel> modelList) {
        this.modelArrayList = modelList;
    }
    public void setProfileList(ArrayList<ProfileModel> profileModelList){
        this.profileModelArrayList = profileModelList;
    }

    public void clearList(){
        modelArrayList.clear();
        profileModelArrayList.clear();
        notifyDataSetChanged();
    }
    public void initConfirmationInterface(ConfirmationListener confirmationListener){
        this.confirmationListener = confirmationListener;
    }
    public void initContactInterface(ContactListener contactListener){
        this.contactListener = contactListener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_my_requests, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MyRequestsModel model = modelArrayList.get(position);

        holder.whoRequestedTextView.setText(model.getNameOfTenant()+", requested to rent your property "+model.getPropertyName());
        holder.descriptionTextView.setText(model.getDescription());
        holder.selectedDateTextView.setText("From "+model.getSelectedDate());

        holder.viewProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppBoiler.showProfileDialog(context,profileModelArrayList.get(position) ,contactListener);
            }
        });

        holder.acceptCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //acceptConfirmationDialog(model.getNameOfTenant());
                confirmationListener.onClickAccept(model.getNameOfTenant() , position);
            }
        });

        holder.rejectCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //rejectConfirmationDialog(model.getNameOfTenant());
                confirmationListener.onClickReject(model.getNameOfTenant() , position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("receiver_key", model.getUidOfTenant());
                AppBoiler.navigateToActivity(context, ChatActivity.class,bundle);
            }
        });

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView whoRequestedTextView, descriptionTextView , selectedDateTextView;
        private ImageView viewProfileImageView;

        private MaterialCardView acceptCardView  , rejectCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            acceptCardView = itemView.findViewById(R.id.acceptCardView);
            rejectCardView = itemView.findViewById(R.id.rejectCardView);
            whoRequestedTextView = itemView.findViewById(R.id.whoRequestTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            viewProfileImageView = itemView.findViewById(R.id.viewProfileImageView);
            selectedDateTextView = itemView.findViewById(R.id.selectedDateTextView);
        }
    }

    public interface ConfirmationListener{
        public void onClickAccept(String tenantName , int position);
        public void onClickReject(String tenantName , int position);
    }
}
