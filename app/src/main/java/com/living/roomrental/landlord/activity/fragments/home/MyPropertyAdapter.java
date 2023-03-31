package com.living.roomrental.landlord.activity.fragments.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.living.roomrental.R;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyDataModel;

import java.util.ArrayList;

public class MyPropertyAdapter extends RecyclerView.Adapter<MyPropertyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<CreatePropertyDataModel> modelArrayList;

    public MyPropertyAdapter(Context context){
        this.context = context;
    }
    public void setPropertyList(ArrayList<CreatePropertyDataModel> modelList){
        this.modelArrayList = modelList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_my_property,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CreatePropertyDataModel model = modelArrayList.get(position);

        if(model.getPropertyImages().size()>0)
          Glide.with(context).load(model.getPropertyImages().get(0)).into(holder.propertyImage);

        holder.propertyName.setText(model.getPropertyName());
        holder.propertyAddress.setText(model.getMapLocationAddress());
        holder.propertyType.setText(model.getType());
        holder.propertyRent.setText(model.getRent());
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView propertyName , propertyAddress , propertyRent , propertyType ;
        private ImageView propertyImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            propertyImage = itemView.findViewById(R.id.propertyImageView);
            propertyName = itemView.findViewById(R.id.propertyName);
            propertyAddress = itemView.findViewById(R.id.propertyAddress);
            propertyRent = itemView.findViewById(R.id.propertyRent);
            propertyType = itemView.findViewById(R.id.propertyType);
        }
    }
}
