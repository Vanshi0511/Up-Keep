package com.living.roomrental.landlord.activity.fragments.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.living.roomrental.R;
import com.living.roomrental.utilities.Validation;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatLandlordAdapter extends RecyclerView.Adapter<ChatLandlordAdapter.ChatsViewHolder> {

    private Context context;
    private ArrayList<ChatLandlordModel> chatLandlordModelArrayList = new ArrayList<>();

    private ChatUserListener listener;

    public ChatLandlordAdapter(Context context){
        this.context = context;
    }

    public void setList(ArrayList<ChatLandlordModel> chatLandlordModelArrayList){
        this.chatLandlordModelArrayList = chatLandlordModelArrayList;
    }

    public void initChatListener(ChatUserListener chatUserListener){
        this.listener = chatUserListener;
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatsViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_user_chat_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatsViewHolder holder, int position) {

        ChatLandlordModel model = chatLandlordModelArrayList.get(position);

        holder.nameTextView.setText(model.getName());
        holder.occupationTextView.setText(model.getOccupation());

        if(!Validation.isStringEmpty(model.getImageUrl())){
            Glide.with(context).load(model.getImageUrl()).into(holder.profileImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClickChat(model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return chatLandlordModelArrayList.size();
    }

    public class ChatsViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView , occupationTextView;
        CircleImageView profileImageView;

        public ChatsViewHolder(@NonNull View itemView) {
            super(itemView);

            nameTextView = itemView.findViewById(R.id.nameTextView);
            occupationTextView = itemView.findViewById(R.id.occupationTextView);
            profileImageView = itemView.findViewById(R.id.profileImageView);
        }
    }

    public interface ChatUserListener{
        void onClickChat(ChatLandlordModel model);
    }
}
