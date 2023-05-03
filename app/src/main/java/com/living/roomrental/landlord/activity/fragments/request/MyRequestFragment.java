package com.living.roomrental.landlord.activity.fragments.request;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.living.roomrental.ViewProfileListener;
import com.living.roomrental.DialogListener;
import com.living.roomrental.R;
import com.living.roomrental.activity.profile.model.ProfileModel;
import com.living.roomrental.comman.chat.ChatActivity;
import com.living.roomrental.databinding.FragmentRequestBinding;
import com.living.roomrental.utilities.AppBoiler;
import com.living.roomrental.utilities.AppConstants;
import com.living.roomrental.utilities.ImplicitUtils;

import java.util.ArrayList;
import java.util.List;

public class MyRequestFragment extends Fragment {
    ;

    private FragmentRequestBinding binding;
    private MyRequestViewModel myRequestViewModel;

    private MyRequestAdapter adapter;

    private Dialog progressDialog , responseDialog;

    private ArrayList<MyRequestsModel> myRequestsModelList = new ArrayList<>();

    private ArrayList<ProfileModel> profileModelArrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRequestBinding.inflate(inflater, container, false);

        myRequestViewModel = new ViewModelProvider(this).get(MyRequestViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressDialog = AppBoiler.setProgressDialog(getContext());
        setAdapter();
        getRequestDataFromServer();
    }

    private void setAdapter(){

        if(adapter==null)
            adapter = new MyRequestAdapter(getContext());
        adapter.setRequestList(myRequestsModelList);
        adapter.setProfileList(profileModelArrayList);

        adapter.initContactInterface(new ViewProfileListener() {
            @Override
            public void onClickContact(String contactNo) {
                ImplicitUtils.intentForCall(getActivity(),contactNo);
            }

            @Override
            public void onClickChat(String receiverKey) {
                Bundle bundle = new Bundle();
                bundle.putString("receiver_key",receiverKey);
                AppBoiler.navigateToActivity(getContext(), ChatActivity.class,bundle);
            }
        });
        adapter.initConfirmationInterface(new MyRequestAdapter.ConfirmationListener() {
            @Override
            public void onClickAccept(String tenantName , int position) {

                    new AlertDialog.Builder(getContext())
                            .setIcon(R.drawable.ic_double_correct)
                            .setMessage("Are you sure to accept '"+tenantName+"'s request?")
                            .setTitle("Accept")
                            .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    progressDialog.show();
                                    acceptProperty(position);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();
            }

            @Override
            public void onClickReject(String tenantName , int position) {

                new AlertDialog.Builder(getContext())
                        .setIcon(R.drawable.ic_cancel)
                        .setMessage("Are you sure to reject '"+tenantName+"'s request?")
                        .setTitle("Reject")
                        .setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.show();
                                rejectProperty(position);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .show();
            }
        });

        binding.myRequestRecyclerView.setAdapter(adapter);
    }
    private void getRequestDataFromServer(){

        LiveData<List<MyRequestsModel>> listLiveData = myRequestViewModel.getRequest();
        listLiveData.observe(getViewLifecycleOwner(), new Observer<List<MyRequestsModel>>() {
            @Override
            public void onChanged(List<MyRequestsModel> myRequestsModels) {

                if(myRequestsModels == null){
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();

                } else if(myRequestsModels.size()>0){
                    binding.noRequestTextView.setVisibility(View.GONE);
                    myRequestsModelList = (ArrayList<MyRequestsModel>) myRequestsModels;
                    getProfileDataFromServer();
                }
                else {
                    progressDialog.dismiss();
                    System.out.println("+============ No data found ===================");
                    adapter.clearList();
                    binding.noRequestTextView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void getProfileDataFromServer(){

        LiveData<List<ProfileModel>> profileModelLiveData = myRequestViewModel.getProfileDataOfTenant(myRequestsModelList);

        profileModelLiveData.observe(getViewLifecycleOwner(), new Observer<List<ProfileModel>>() {
            @Override
            public void onChanged(List<ProfileModel> profileModels) {

                progressDialog.dismiss();
                if(profileModels!=null){
                    profileModelArrayList = (ArrayList<ProfileModel>) profileModels;
                    notifyAdapter();
                } else {
                    System.out.println("=========== No profile data ===========");
                }
            }
        });
    }
    private void notifyAdapter(){

        for(int i=0 ; i< myRequestsModelList.size() ; i++){
            myRequestsModelList.get(i).setNameOfTenant(profileModelArrayList.get(i).getName());
        }
        System.out.println("============= notify adapter =======");
        adapter.setRequestList(myRequestsModelList);
        adapter.setProfileList(profileModelArrayList);
        adapter.notifyDataSetChanged();
    }

    private void rejectProperty(int position){
        //todo delete request from property
        MyRequestsModel model = myRequestsModelList.get(position);
        LiveData<String> deleteLiveData = myRequestViewModel.removePropertyRequest(myRequestsModelList.get(position).getUidOfTenant() ,  myRequestsModelList.get(position).getPropertyKey());
        deleteLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {

                progressDialog.dismiss();
                if(s.equals(AppConstants.SUCCESS)){
                    responseDialog = AppBoiler.customDialogWithBtn(getContext(),"Request Deleted Successfully", R.drawable.ic_done, new DialogListener() {
                        @Override
                        public void onClick() {
                            responseDialog.dismiss();
                            myRequestViewModel.updateStatusOfTenant(model.getUidOfTenant(), model.getPropertyKey() ,"Rejected");
                        }
                    });
                } else {
                    responseDialog = AppBoiler.customDialogWithBtn(getContext(), s, R.drawable.ic_error, new DialogListener() {
                        @Override
                        public void onClick() {
                            responseDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void acceptProperty(int position){
        //todo accept property of user -> delete all requests + update bookingStatus
        MyRequestsModel currentRequestModel = myRequestsModelList.get(position);
        LiveData<String> deleteAllRequest = myRequestViewModel.deleteAllRequest(currentRequestModel.getPropertyKey());

        deleteAllRequest.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressDialog.dismiss();
                if(s.equals(AppConstants.SUCCESS)){
                    // todo update booking status
                    updateBookingStatusOfProperty(currentRequestModel);
                } else {
                    responseDialog = AppBoiler.customDialogWithBtn(getContext(), s, R.drawable.ic_error, new DialogListener() {
                        @Override
                        public void onClick() {
                            responseDialog.dismiss();
                        }
                    });
                }
            }
        });
    }

    private void updateBookingStatusOfProperty(MyRequestsModel model){

        LiveData<String> updateBookingLiveData = myRequestViewModel.updateBooking(model);
        updateBookingLiveData.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                progressDialog.dismiss();
               if(s.equals(AppConstants.SUCCESS)){

                   responseDialog = AppBoiler.customDialogWithBtn(getContext(),"Request Accepted", R.drawable.ic_done, new DialogListener() {
                       @Override
                       public void onClick() {
                           responseDialog.dismiss();
                           myRequestViewModel.updateStatusOfTenant(model.getUidOfTenant(), model.getPropertyKey(), "Accepted");
                       }
                   });

               } else {
                   responseDialog = AppBoiler.customDialogWithBtn(getContext(), s, R.drawable.ic_error, new DialogListener() {
                       @Override
                       public void onClick() {
                           responseDialog.dismiss();
                       }
                   });
               }
            }
        });
    }
}