package com.living.roomrental.landlord.activity.edit_property;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.living.roomrental.R;
import java.util.ArrayList;

public class EditPropertyImageAdapter extends RecyclerView.Adapter<EditPropertyImageAdapter.ViewHolder>{

    private Context context;
    private ArrayList<String> imagesUrl;

    public EditPropertyImageAdapter(Context context){
        this.context = context;
    }

    public void setImagesList(ArrayList<String> imagesUrl){
        System.out.println("array size=========="+imagesUrl.size());
        this.imagesUrl = imagesUrl;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_property_images,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load(imagesUrl.get(position)).into(holder.propertyImage);
        holder.imageCounter.setText((position+1)+" / "+imagesUrl.size());
    }

    @Override
    public int getItemCount() {
        return imagesUrl.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView propertyImage , cancelImageView;
        TextView imageCounter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            propertyImage = itemView.findViewById(R.id.propertyImage);
            cancelImageView = itemView.findViewById(R.id.cancelImageView);
            imageCounter = itemView.findViewById(R.id.imageCounterTextView);

        }
    }
}
