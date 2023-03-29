package com.living.roomrental.landlord.activity.create_property;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.living.roomrental.R;

import java.util.ArrayList;

public class PropertyImageAdapter extends RecyclerView.Adapter<PropertyImageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Uri> imagesUri;

    public PropertyImageAdapter(Context context){
        this.context = context;
    }
    public void setImagesList(ArrayList<Uri> imagesUri){
        this.imagesUri = imagesUri;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_property_images,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return imagesUri.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
