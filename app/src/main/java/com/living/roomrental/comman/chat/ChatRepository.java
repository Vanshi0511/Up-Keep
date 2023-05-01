package com.living.roomrental.comman.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.living.roomrental.FirebaseController;

public class ChatRepository {

    private DatabaseReference senderReference;
    private DatabaseReference receiverReference;

    private String receiverKey ;
    private String senderKey;

    private String keyForSender;

    private String keyForReceiver;

    private MutableLiveData<ChatModel> chatModelMutableLiveData = new MutableLiveData<>();

    public ChatRepository(String receiverKey){

        senderReference = FirebaseController.getInstance().getDatabaseReference().child("Chat");
        receiverReference = FirebaseController.getInstance().getDatabaseReference().child("Chat");

        senderKey = FirebaseController.getInstance().getAuth().getUid();
        this.receiverKey = receiverKey;

        keyForSender = senderKey+"_"+receiverKey;
        keyForReceiver = receiverKey+"_"+senderKey;

        System.out.println("============= KEYS ========= SENDER = "+senderKey+"  RECEIVER = "+receiverKey+" === FINAL KEY ==== "+ keyForSender);
    }

    public MutableLiveData<ChatModel> getChatFromServer(){

        senderReference.child(senderKey).child(keyForSender).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("======= ON CHILD ADDED =========== "+previousChildName);

                if(snapshot.exists()){
                    System.out.println("======== KEY ====== "+snapshot.getKey());

                    ChatModel model = snapshot.getValue(ChatModel.class);
                    chatModelMutableLiveData.setValue(model);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("======= ON CHILD CHANGED =========== "+previousChildName);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                System.out.println("======= ON CHILD REMOVED =========== ");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("======= ON CHILD MOVED =========== "+previousChildName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("======= ON CANCELLED =========== ");
            }
        });
        return chatModelMutableLiveData;
    }

    public void sendChatToServer(ChatModel model){

        senderReference.child(senderKey).child(keyForSender).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("----------- SUCCESS ON SENDER SIDE----------- ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("----------- Failed ----------- "+e.getMessage());
            }
        });


        receiverReference.child(receiverKey).child(keyForReceiver).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("----------- SUCCESS ON RECEIVER SIDE----------- ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("----------- Failed ----------- "+e.getMessage());
            }
        });
    }
}
