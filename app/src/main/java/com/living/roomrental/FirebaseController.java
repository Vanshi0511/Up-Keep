package com.living.roomrental;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseController {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    public static FirebaseController controller;

    private FirebaseController(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("RoomRental");
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
}
