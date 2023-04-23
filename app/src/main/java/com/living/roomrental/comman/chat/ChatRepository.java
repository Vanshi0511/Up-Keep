package com.living.roomrental.comman.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.living.roomrental.FirebaseController;

import java.util.List;

public class ChatRepository {

    private DatabaseReference databaseReference;

    private String receiverKey ;
    private String senderKey;
    private String key;

    public ChatRepository(String receiverKey){

       databaseReference = FirebaseController.getInstance().getDatabaseReference().child("Chat");
       senderKey = FirebaseController.getInstance().getAuth().getUid();
       this.receiverKey = receiverKey;

        key = senderKey+"_"+receiverKey;

        System.out.println("============= KEYS ========= SENDER = "+senderKey+"  RECEIVER = "+receiverKey+" === FINAL KEY ==== "+key);
    }

    public void getChatFromServer(){

        databaseReference.child(key).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                System.out.println("======= ON CHILD ADDED =========== "+previousChildName);

                if(snapshot.exists()){
                    System.out.println("======== KEY ====== "+snapshot.getKey());
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

    }

    public void sendChatToServer(ChatModel model){

        databaseReference.child(key).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                System.out.println("----------- SUCCESS ----------- ");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println("----------- Failed ----------- "+e.getMessage());
            }
        });
    }
}
