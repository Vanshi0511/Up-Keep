package com.living.roomrental.landlord.activity.fragments.chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatLandlordViewModel extends ViewModel {

    private ChatLandlordRepository repository = new ChatLandlordRepository();

    public MutableLiveData<ArrayList<String>> getAllUserChatFromRepository(){
        return repository.getAllUsersChat();
    }

    public MutableLiveData<ArrayList<ChatLandlordModel>> getUserProfilesFromRepository(ArrayList<String> receiverKeys){
        return repository.getUserData(receiverKeys);
    }
}
