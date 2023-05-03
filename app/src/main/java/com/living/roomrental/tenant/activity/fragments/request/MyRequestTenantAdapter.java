package com.living.roomrental.tenant.activity.fragments.request;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.living.roomrental.R;
import com.living.roomrental.ViewProfileListener;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.utilities.AppBoiler;

import java.util.List;

public class MyRequestTenantAdapter extends RecyclerView.Adapter<MyRequestTenantAdapter.MyRequestTenantViewHolder>{

    private Context context;
    private ViewProfileListener viewProfileListener;
    private List<MyRequestTenantPropertyModel> myRequestTenantPropertyModelList;

    private List<ProfileModel> profileModelList ;

    public MyRequestTenantAdapter(Context context , List<MyRequestTenantPropertyModel> myRequestTenantPropertyModelList){
        this.context = context;
        this.myRequestTenantPropertyModelList = myRequestTenantPropertyModelList;
    }

    public void setProfileList(List<ProfileModel> profileModelList){
        this.profileModelList = profileModelList;
    }
    public void initContactInterface(ViewProfileListener viewProfileListener){
        this.viewProfileListener = viewProfileListener;
    }

    @NonNull
    @Override
    public MyRequestTenantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyRequestTenantViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_my_request_tenant,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyRequestTenantViewHolder holder, int position) {

        MyRequestTenantPropertyModel model = myRequestTenantPropertyModelList.get(position);

        holder.propertyName.setText(model.getPropertyName());
        holder.propertyLocation.setText(model.getMapLocationAddress());

        try{
            holder.propertyDate.setText("For : "+model.getPropertyRequests().getDate());
            holder.propertyRegistrationDescription.setText(model.getPropertyRequests().getDescription());
        } catch (Exception ae){
            ae.printStackTrace();
        }


        String status = model.getStatus();
        if(status.equals("Pending")){
            holder.statusImageView.setImageResource(R.drawable.ic_pending);
            holder.propertyStatus.setTextColor(context.getColor(R.color.yellow_800));

        }else if(status.equals("Accepted")){
            holder.statusImageView.setImageResource(R.drawable.ic_done);
            holder.propertyStatus.setTextColor(context.getColor(R.color.green_600));
        } else {
            holder.statusImageView.setImageResource(R.drawable.ic_rejected);
            holder.propertyStatus.setTextColor(context.getColor(R.color.red_400));
        }
        holder.propertyStatus.setText(status);


        holder.viewProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppBoiler.showProfileDialog(context,profileModelList.get(position) , viewProfileListener, model.getLandlordId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return myRequestTenantPropertyModelList.size();
    }

    public class MyRequestTenantViewHolder extends RecyclerView.ViewHolder{

        TextView propertyName , propertyRegistrationDescription , propertyDate , propertyLocation , propertyStatus;
        ImageView statusImageView , viewProfileImageView;

        public MyRequestTenantViewHolder(@NonNull View itemView) {
            super(itemView);

            propertyName = itemView.findViewById(R.id.propertyNameTextView);
            propertyRegistrationDescription = itemView.findViewById(R.id.propertyRegistrationDescriptionTextView);
            propertyDate = itemView.findViewById(R.id.propertyRegistrationDateTextView);
            propertyLocation = itemView.findViewById(R.id.propertyLocationTextView);
            propertyStatus = itemView.findViewById(R.id.propertyStatusTextView);
            statusImageView = itemView.findViewById(R.id.statusImageView);
            viewProfileImageView = itemView.findViewById(R.id.viewProfileImageView);

        }
    }
}
