package com.living.roomrental.utilities.repository;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseController {
    private FirebaseAuth auth;
    private FirebaseUser user;
    public static FirebaseController controller;

    private FirebaseController(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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
}
