package com.living.roomrental.landlord.activity.create_property;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.living.roomrental.R;

import java.util.ArrayList;

public class PropertyImageAdapter extends RecyclerView.Adapter<PropertyImageAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Uri> imagesUri =new ArrayList<>();
    private ItemListener listener;

    public PropertyImageAdapter(Context context){
        this.context = context;
    }
    public void setImagesList(ArrayList<Uri> imagesUri){
        System.out.println("array size=========="+imagesUri.size());
        this.imagesUri = imagesUri;
        notifyDataSetChanged();
    }
    public void setListener(ItemListener listener){
        this.listener = listener;
    }
    public void removeImage(int position){
        System.out.println("array size=========="+imagesUri.size()+"     "+position);
        imagesUri.remove(position);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_property_images,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.propertyImage.setImageURI(imagesUri.get(position));
        holder.imageCounter.setText((position+1)+" / "+imagesUri.size());
    }

    @Override
    public int getItemCount() {
        return imagesUri.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView propertyImage , cancelImageView;
        TextView imageCounter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            propertyImage = itemView.findViewById(R.id.propertyImage);
            cancelImageView = itemView.findViewById(R.id.cancelImageView);
            imageCounter = itemView.findViewById(R.id.imageCounterTextView);

            cancelImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onClickCancel(getAdapterPosition());
        }
    }
    public interface ItemListener{
        void onClickCancel(int position);
    }
}
