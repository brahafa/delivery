package com.bringit.orders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bringit.orders.activities.MainActivity;
import com.bringit.orders.databinding.FragmentLogInBinding;
import com.bringit.orders.network.Request;
import com.bringit.orders.utils.Constants;
import com.google.firebase.FirebaseApp;

public class LogInFragment extends Fragment {

    private Context mContext;

    private FragmentLogInBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLogInBinding.inflate(inflater, container, false);

//        Button startServiceButton = (Button)view. findViewById(R.id.start_foreground_service_button);
//        startServiceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), MyForeGroundService.class);
//                intent.setAction(MyForeGroundService.ACTION_START_FOREGROUND_SERVICE);
//                getActivity().startService(intent);
//            }
//        });
//
//        Button stopServiceButton = (Button)view.findViewById(R.id.stop_foreground_service_button);
//        stopServiceButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent( getActivity(), MyForeGroundService.class);
//                intent.setAction(MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
//                getActivity().startService(intent);
//            }
//        });

        binding.go.setOnClickListener(v -> Request.getInstance().logIn(mContext,
                binding.password.getText().toString(), binding.phone.getText().toString(),
                isDataSuccess -> {
                    if (isDataSuccess) {
                        NavHostFragment.findNavController(this)
                                .navigate(LogInFragmentDirections.actionLoginFragmentToTimerFragment());
                    }
                }));
        FirebaseApp.initializeApp(getActivity());


        binding.userDetailsLayout.setOnClickListener(view -> {
        });

        binding.newUser.setOnClickListener(v -> NavHostFragment.findNavController(this)
                .navigate(LogInFragmentDirections.actionLoginFragmentToRegisterFragment()));

        binding.forgotPassword.setOnClickListener(v -> {
            if (binding.phone.getText().toString().equals("")) {
                Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא טלפון");
            } else {
                NavHostFragment.findNavController(this)
                        .navigate(LogInFragmentDirections.actionLoginFragmentToForgotPasswordFragment(binding.phone.getText().toString()));
            }
        });
        ((MainActivity) getActivity()).setBottomNavigationVisibility(8);

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
