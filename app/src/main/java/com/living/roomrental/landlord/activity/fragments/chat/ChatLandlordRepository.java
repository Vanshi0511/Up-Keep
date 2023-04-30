package com.living.roomrental.landlord.activity.fragments.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.living.roomrental.FirebaseController;
import com.living.roomrental.utilities.AppConstants;


import java.util.ArrayList;


public class ChatLandlordRepository {

    private MutableLiveData<ArrayList<String>> listMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<ArrayList<ChatLandlordModel>> listChatModelMutableData = new MutableLiveData<>();

    private DatabaseReference databaseReference;

    public ChatLandlordRepository(){
        databaseReference = FirebaseController.getInstance().getDatabaseReference();
    }

    public MutableLiveData<ArrayList<String>> getAllUsersChat(){

        String currentUserUid = FirebaseController.getInstance().getUser().getUid();
        int lengthOfUid = currentUserUid.length();

        ArrayList<String> receiverKeyList = new ArrayList<>();

        databaseReference.child("Chat").child(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.exists()){

                    for(DataSnapshot keySnapshot : snapshot.getChildren()){
                        String receiverKey = keySnapshot.getKey().substring(lengthOfUid+1);
                        receiverKeyList.add(receiverKey);
                    }

                    listMutableLiveData.setValue(receiverKeyList);
                    System.out.println("=========== RECEIVER KEY SIZE ========= "+receiverKeyList.size());
                } else {
                    listMutableLiveData.setValue(receiverKeyList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("=========== EXCEPTION ========= "+error.getMessage());
                listMutableLiveData.setValue(null);
            }
        });
        return listMutableLiveData;
    }

    public MutableLiveData<ArrayList<ChatLandlordModel>> getUserData(ArrayList<String> receiverKeys){

        ArrayList<ChatLandlordModel> listChatModel = new ArrayList<>();

        DatabaseReference reference = databaseReference.child(AppConstants.USER_PROFILE);

        for(String key : receiverKeys){

            reference.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){

                        String name = snapshot.child("name").getValue().toString();
                        String occupation = snapshot.child("occupation").getValue().toString();
                        String imageUrl = null ;
                        try{
                            imageUrl = snapshot.child("imageUrl").getValue().toString();
                        }catch (NullPointerException e){}

                        ChatLandlordModel model = new ChatLandlordModel(name , occupation ,imageUrl, key);
                        listChatModel.add(model);

                    }

                    System.out.println("================ SIZEc ============ "+listChatModel.size());
                    if(listChatModel.size() == receiverKeys.size()){
                        listChatModelMutableData.setValue(listChatModel);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listChatModelMutableData.setValue(null);
                    System.out.println("============= ERROR ============== "+error.getMessage());
                }
            });
        }
        return listChatModelMutableData;
    }
}
