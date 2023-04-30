package com.living.roomrental.landlord.activity.fragments.chat;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.living.roomrental.R;
import com.living.roomrental.databinding.FragmentChatLandlordBinding;
import com.living.roomrental.utilities.AppBoiler;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ChatLandlordFragment extends Fragment {

    private FragmentChatLandlordBinding binding;

    private ChatLandlordViewModel chatLandlordViewModel;

    private Dialog progressDialog;

    private ChatLandlordAdapter adapter;

    public static ChatLandlordFragment newInstance() {
        ChatLandlordFragment fragment = new ChatLandlordFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        chatLandlordViewModel = new ViewModelProvider(this).get(ChatLandlordViewModel.class);

        initListener();

        binding = FragmentChatLandlordBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }


    private void initListener() {
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(AppBoiler.isInternetConnected(getContext())){

            progressDialog = AppBoiler.setProgressDialog(getContext());
            getAllChats();
        } else {
            AppBoiler.showSnackBarForInternet(getContext(),binding.getRoot());
        }

    }

    private void getAllChats() {

        LiveData<ArrayList<String>> receiverKeyListLiveData = chatLandlordViewModel.getAllUserChatFromRepository();
        receiverKeyListLiveData.observe(getViewLifecycleOwner(), new Observer<ArrayList<String>>() {
            @Override
            public void onChanged(ArrayList<String> strings) {

                if(strings == null){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                } else if(strings.size()==0){
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "No Chats yet", Toast.LENGTH_SHORT).show();

                } else {
                    getUserProfileByKeys(strings);
                }
            }
        });
    }

    private void getUserProfileByKeys(ArrayList<String> receiversKeys) {

        LiveData<ArrayList<ChatLandlordModel>> chatModelLivedata = chatLandlordViewModel.getUserProfilesFromRepository(receiversKeys);
        chatModelLivedata.observe(getViewLifecycleOwner(), new Observer<ArrayList<ChatLandlordModel>>() {
            @Override
            public void onChanged(ArrayList<ChatLandlordModel> chatLandlordModels) {

                progressDialog.dismiss();

                if(chatLandlordModels == null){
                    Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }  else {
                    setAdapter(chatLandlordModels);
                }
            }
        });

    }

    private void setAdapter(ArrayList<ChatLandlordModel> chatLandlordModels) {

        ChatLandlordAdapter adapter = new ChatLandlordAdapter(getContext());
        adapter.setList(chatLandlordModels);
        binding.chatsRecyclerView.setAdapter(adapter);
    }
}