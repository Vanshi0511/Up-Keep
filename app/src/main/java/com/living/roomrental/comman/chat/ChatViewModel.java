package com.living.roomrental.comman.chat;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChatViewModel extends ViewModel {

    private ChatRepository repository;

    public ChatViewModel(String receiverKey){
        repository = new ChatRepository(receiverKey);
    }

    public void sendMessageToRepository(ChatModel model){
        repository.sendChatToServer(model);
    }

    public MutableLiveData<ChatModel> getMessagesFromRepository(){
        return repository.getChatFromServer();
    }
}
