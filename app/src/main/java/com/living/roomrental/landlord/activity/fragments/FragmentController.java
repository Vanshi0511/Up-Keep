package com.living.roomrental.landlord.activity.fragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class FragmentController {

    private FragmentManager manager;

    public FragmentController(AppCompatActivity activity){
        manager = activity.getSupportFragmentManager();
    }

    public void addFragment(Fragment fragment , int rootId){

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(rootId,fragment);
        transaction.commit();
    }

    public void reloadFragment(Fragment fragment) {

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.detach(fragment).attach(fragment).commit();
    }

    public void replaceFragment(Fragment fragment , int rootId){

        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(rootId,fragment);
        transaction.commit();
    }

}
