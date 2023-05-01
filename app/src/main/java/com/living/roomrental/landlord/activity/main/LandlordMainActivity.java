package com.living.roomrental.landlord.activity.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupWindow;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationBarView;
import com.living.roomrental.PopupWindowsMenuListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.profile.view.ViewProfileBottomSheet;
import com.living.roomrental.comman.about.AboutActivity;
import com.living.roomrental.comman.help.HelpActivity;
import com.living.roomrental.comman.more.MoreActivity;
import com.living.roomrental.databinding.ActivityLandlordMainBinding;
import com.living.roomrental.landlord.activity.create_property.CreatePropertyActivity;
import com.living.roomrental.landlord.activity.fragments.FragmentController;
import com.living.roomrental.landlord.activity.fragments.chat.ChatLandlordFragment;
import com.living.roomrental.landlord.activity.fragments.home.MyPropertyFragment;
import com.living.roomrental.landlord.activity.fragments.request.MyRequestFragment;
import com.living.roomrental.repository.local.SharedPreferenceStorage;
import com.living.roomrental.repository.local.SharedPreferencesController;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.MenuOperation;
import com.living.roomrental.utilities.Validation;

import java.util.ArrayList;

public class LandlordMainActivity extends AppCompatActivity {

    private ActivityLandlordMainBinding binding;

    private ViewProfileBottomSheet bottomSheet;

    private FragmentController controller  = new FragmentController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLandlordMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.bottomNavigationView.setBackground(null);
        binding.bottomNavigationView.getMenu().getItem(2).setEnabled(false);

        initListeners();
    }

    private void getDataFromLocal(){
        ArrayList<String> list ;
        list = SharedPreferenceStorage.getUserExtraData(SharedPreferencesController.getInstance(this).getPreferences());

        if(!Validation.isStringEmpty(list.get(1)))
             Glide.with(this).load(list.get(1)).into(binding.header.headerProfileImageView);
        else{
            binding.header.headerProfileImageView.setImageResource(R.drawable.ic_person);
        }
        binding.header.headerUserName.setText(list.get(0));
    }

    private void initListeners() {

        binding.fabCreateProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppBoiler.navigateToActivity(LandlordMainActivity.this, CreatePropertyActivity.class,null);
            }
        });

        binding.header.headerProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheet = new ViewProfileBottomSheet();
                bottomSheet.show(getSupportFragmentManager(),"ViewProfileBottomSheet");
            }
        });

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);
        if(fragment==null)
          controller.addFragment(new MyPropertyFragment(),R.id.fragmentContainer);

        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){

                    case R.id.myProperty :
                         controller.replaceFragment(new MyPropertyFragment(),R.id.fragmentContainer);
                         break;
                    case R.id.myRequest:
                         controller.replaceFragment(new MyRequestFragment(),R.id.fragmentContainer);
                        break;
                    case R.id.myChats:
                        controller.replaceFragment(new ChatLandlordFragment(),R.id.fragmentContainer);
                        break;
                    case R.id.myMore:
                        AppBoiler.navigateToActivity(LandlordMainActivity.this, MoreActivity.class,null);
                        break;
                    default:
                }
                return true;
            }
        });

        binding.bottomNavigationView.setOnItemReselectedListener(new NavigationBarView.OnItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {

            }
        });

        binding.header.moreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                AppBoiler.showMenuPopupWindow(LandlordMainActivity.this,binding.header.moreImageView, new PopupWindowsMenuListener() {
//                    @Override
//                    public void onClickHelp() {
//
//                    }
//
//                    @Override
//                    public void onClickAbout() {
//
//                    }
//
//                    @Override
//                    public void onClickLogOut() {
//                        MenuOperation.logOutUser(LandlordMainActivity.this);
//                    }
//
//                    @Override
//                    public void onClickDeactivate() {
//
//                    }
//                });
//                System.out.println("====== popup");
                //menuPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

                PopupMenu popupMenu = new PopupMenu(LandlordMainActivity.this,binding.header.moreImageView);
                popupMenu.getMenuInflater().inflate(R.menu.landlord_popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        switch (menuItem.getItemId()){
                            case R.id.helpMenu:
                                AppBoiler.navigateToActivity(LandlordMainActivity.this, HelpActivity.class,null);
                                break;
                            case R.id.aboutMenu:
                                AppBoiler.navigateToActivity(LandlordMainActivity.this, AboutActivity.class,null);
                                break;
                            case R.id.logOutMenu:
                                MenuOperation.logOutUser(LandlordMainActivity.this);
                                break;
                            case R.id.deactivateMenu:

                        }
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataFromLocal();
        if(bottomSheet!=null){
            bottomSheet.dismiss();
        }
    }

}