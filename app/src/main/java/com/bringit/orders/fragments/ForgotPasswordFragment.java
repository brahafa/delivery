package com.bringit.orders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bringit.orders.databinding.FragmentForgotPasswordBinding;
import com.bringit.orders.network.Request;
import com.bringit.orders.utils.Constants;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class ForgotPasswordFragment extends Fragment {

    private Context mContext;

    private FragmentForgotPasswordBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String phoneNumber = ForgotPasswordFragmentArgs.fromBundle(requireArguments()).getPhoneNumber();

        sendSms(phoneNumber);

        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false);

        binding.phone.setText(phoneNumber);

        binding.go.setOnClickListener(v -> {

            if (checkValidation())
                Request.getInstance().confirmResetPassword(mContext,
                        binding.password.getText().toString(), binding.phone.getText().toString(), binding.smsCode.getText().toString(),
                        isDataSuccess -> {
                            if (isDataSuccess) {
                                NavHostFragment.findNavController(this)
                                        .navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment());
                            }
                        });
        });

        return binding.getRoot();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    private void sendSms(String phoneNumber) {
        Request.getInstance().resetPassword(mContext, phoneNumber, isDataSuccess -> {
        });
    }

    private boolean checkValidation() {

        if (binding.phone.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא טלפון");
            return false;
        }
        if (binding.password.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא סיסמה");
            return false;
        }
        if (binding.smsCode.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "enter sms code"); //fixme translate
            return false;
        }
        return true;

    }
}
