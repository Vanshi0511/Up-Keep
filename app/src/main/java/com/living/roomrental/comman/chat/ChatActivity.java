package com.living.roomrental.comman.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.databinding.ActivityChatBinding;
import com.living.roomrental.landlord.activity.fragments.chat.ChatLandlordModel;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;
import com.living.roomrental.utilities.Validation;


public class ChatActivity extends AppCompatActivity {

    private ActivityChatBinding binding;

    private ChatViewModel chatViewModel;

    private  String currentUserUid;
    private ChatAdapter adapter;

    private Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String receiverKey = getBundles();
        chatViewModel = new ViewModelProvider(this, new ChatViewModelFactory(receiverKey)).get(ChatViewModel.class);
        currentUserUid = FirebaseAuth.getInstance().getUid();

        binding.header.headerTitle.setText("Chat");
        initListener();
        initChatAdapter();
        getMessagesFromServer();
    }

    private String getBundles() {
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            return bundle.getString("receiver_key");
        } else
            return null;
    }

    private void initListener(){

        binding.header.backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.sendImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String message = binding.messageEditText.getText().toString().trim();
                if(!Validation.isStringEmpty(message)){

                    if(AppBoiler.isInternetConnected(ChatActivity.this)){
                        binding.messageEditText.setText("");
                        sendMessageToServer(message);
                    }
                    else {
                        AppBoiler.showSnackBarForInternet(ChatActivity.this, binding.rootLayoutOfChat);
                    }
                }
            }
        });
    }

    private void initChatAdapter(){
        adapter = new ChatAdapter(this);
        binding.messageRecyclerView.setAdapter(adapter);
    }
    private void sendMessageToServer(String message) {

        ChatModel model = new ChatModel("30 min",message,currentUserUid);
        chatViewModel.sendMessageToRepository(model);
    }

    public void getMessagesFromServer(){

        LiveData<ChatModel> chatModelLiveData =  chatViewModel.getMessagesFromRepository();

        chatModelLiveData.observe(this, new Observer<ChatModel>() {
            @Override
            public void onChanged(ChatModel model) {

                if(model!=null){
                    notifyChatAdapter(model);
                }
            }
        });
    }

    private void notifyChatAdapter(ChatModel model) {
        adapter.setListData(model);
        binding.messageRecyclerView.scrollToPosition(adapter.getItemCount()-1);
    }
}