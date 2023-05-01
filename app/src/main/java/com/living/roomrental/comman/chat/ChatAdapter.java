package com.living.roomrental.comman.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.R;
import com.living.roomrental.utilities.AppConstants;

import java.util.ArrayList;
import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private String currentUserUid;
    private List<ChatModel> chatModelList = new ArrayList<>();

    public ChatAdapter(Context context){
        this.context=context;
        currentUserUid = FirebaseAuth.getInstance().getUid();
    }
    public void setListData(ChatModel model){
        this.chatModelList.add(model);
        System.out.println("========= SIZE ============= "+chatModelList.size());
        notifyItemChanged(chatModelList.size()-1);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder;
        if(viewType == AppConstants.SENDER_TYPE){
            viewHolder =  new SenderViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_chat_sender_message,parent,false));
        }else{
            viewHolder = new ReceiverViewHolder(LayoutInflater.from(context).inflate(R.layout.listitem_chat_receiver_message,parent,false));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType()){

            case AppConstants.SENDER_TYPE :
                SenderViewHolder senderViewHolder = (SenderViewHolder) holder;

                senderViewHolder.message.setText(chatModelList.get(position).getMessage());
                senderViewHolder.time.setText(chatModelList.get(position).getTime());
                break;

            case AppConstants.RECEIVER_TYPE :
                ReceiverViewHolder receiverViewHolder = (ReceiverViewHolder) holder;

                receiverViewHolder.message.setText(chatModelList.get(position).getMessage());
                receiverViewHolder.time.setText(chatModelList.get(position).getTime());
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(chatModelList.get(position).getSenderId().equals(currentUserUid))
            return AppConstants.SENDER_TYPE;
        else
            return AppConstants.RECEIVER_TYPE;
    }

    @Override
    public int getItemCount() {
        return chatModelList.size();
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView message , time;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.messageText);
            time = itemView.findViewById(R.id.messageTime);
        }
    }
    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView message , time;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            message= itemView.findViewById(R.id.messageText);
            time = itemView.findViewById(R.id.messageTime);
        }
    }
}
