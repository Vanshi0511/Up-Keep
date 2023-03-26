package com.living.roomrental;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseController {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    public static FirebaseController controller;

    private FirebaseController(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("RoomRental");
        storageReference = FirebaseStorage.getInstance().getReference("RoomRental");
    }
    public static FirebaseController getInstance(){
        if(controller==null){
            controller = new FirebaseController();
        }
        return controller;
    }
    public FirebaseAuth getAuth(){
        return auth;
    }

    public DatabaseReference getDatabaseReference(){
        return databaseReference;
    }

    public FirebaseUser getUser(){
        return user;
    }

    public StorageReference getStorageReference(){
        return storageReference;
    }
}
