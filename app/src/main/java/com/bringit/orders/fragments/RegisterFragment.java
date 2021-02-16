package com.bringit.orders.fragments;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.bringit.orders.activities.MainActivity;
import com.bringit.orders.databinding.FragmentRegisterBinding;
import com.bringit.orders.models.RegistrationModel;
import com.bringit.orders.network.Request;
import com.bringit.orders.utils.Constants;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import static com.bringit.orders.utils.SharedPrefs.getData;
import static com.bringit.orders.utils.SharedPrefs.saveData;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private Context mContext;

    private FragmentRegisterBinding binding;

    String base64Self, base64TZ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater, container, false);

        initUI();
        binding.takePicTz.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Log.d(",,,", "  camera1  ");
            //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                checkPermission(1);
            } else {
                startActivityForResult(cameraIntent, 1);
            }
        });

        binding.takePicSelf.setOnClickListener(v -> {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Log.d(",,,", "  camera2  ");
            //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                checkPermission(2);
            } else {
                startActivityForResult(cameraIntent, 2);
            }
        });
        ((MainActivity) getActivity()).setBottomNavigationVisibility(8);

        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission(int id) {
        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, id);
        }
    }

    private void initUI() {

        binding.confirmBtn.setOnClickListener(v -> confirmPhoneServer());

        binding.go.setOnClickListener(v -> {
            saveDataToPref();
            if (checkValidation()) {
                sendProfileToServer();
            }
        });
        binding.existUser.setOnClickListener(view -> NavHostFragment.findNavController(this)
                .navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()));
        initDataFromPref();

    }

    private void confirmPhoneServer() {
        Request.getInstance().confirmUser(mContext, binding.phone.getText().toString(), binding.confirmNum.getText().toString(),
                isDataSuccess -> {
                    if (isDataSuccess) {
                        NavHostFragment.findNavController(this)
                                .navigate(RegisterFragmentDirections.actionRegisterFragmentToTimerFragment());
                    }
                });
    }

    private void sendProfileToServer() {
        JSONObject data = new JSONObject();
        JSONObject address = new JSONObject();

        RegistrationModel registrationModel = new RegistrationModel();

        registrationModel.setName(binding.fName.getText().toString());
        registrationModel.setLastName(binding.lName.getText().toString());
        registrationModel.setPhone(binding.phone.getText().toString());
        registrationModel.setmEmail(binding.email.getText().toString());
        registrationModel.setmIdentity(binding.tz.getText().toString());
        registrationModel.setmPassword(binding.pass.getText().toString());
        registrationModel.setmConfirmPass(binding.confirmPass.getText().toString());

        registrationModel.getAddress().setCityName(binding.city.getText().toString());
        registrationModel.getAddress().setStreet(binding.street.getText().toString());
        registrationModel.getAddress().setHouseNum(binding.homeNum.getText().toString());
        registrationModel.getAddress().setAptNum(binding.apartmentNum.getText().toString());

        registrationModel.setmSelfImage("base64Self");
        registrationModel.setmIdentityImage("base64TZ");
//            data.put("self_image",base64Self);
//            data.put("identity_image",base64TZ);

        Request.getInstance().signUp(mContext, registrationModel,
                isDataSuccess -> {
                    if (isDataSuccess) binding.confirmNumberLayout.setVisibility(View.VISIBLE);
                });
    }

    private boolean checkValidation() {
        if (binding.fName.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא שם פרטי");
            return false;
        }
        if (binding.lName.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא שם משפחה");
            return false;
        }
        if (binding.phone.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא טלפון");
            return false;
        }
        if (binding.tz.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא מס זהות");
            return false;
        }
        if (binding.city.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא עיר");
            return false;
        }
        if (binding.street.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא רחוב");
            return false;
        }
        if (binding.apartmentNum.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא מס בית");
            return false;
        }

        if (binding.pass.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא סיסמה");
            return false;
        }
        if (!binding.pass.getText().toString().equals(binding.confirmPass.getText().toString())) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא אמת סיסמתך");
            return false;
        }
        if (binding.email.getText().toString().equals("") || !binding.email.getText().toString().contains("@")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא רחוב");
            return false;
        }
        if (base64Self == null || base64Self.equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא צלם תמונת פרופיל");
            return false;
        }
        if (base64TZ == null || base64TZ.equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא צלם תעודת מזהה");
            return false;
        }
        if (!binding.check.isChecked()) {
            Constants.openAlertDialog(getActivity(), "", "נא אשר את תנאי השימוש");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Log.d(",,,", "  ssas  ");
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            binding.picTz.setImageBitmap(Constants.getResizedBitmap(bitmap, Constants.getScreenWidth(getActivity())));
            base64TZ = convertBmpToBase64(bitmap);
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            binding.picSelf.setImageBitmap(Constants.getResizedBitmap(bitmap, Constants.getScreenWidth(getActivity())));
            base64Self = convertBmpToBase64(bitmap);
        } else {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1 || requestCode == 2) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, requestCode);
            } else {
                //Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
                Constants.openAlertDialog(getActivity(), "", "לא ניתן להמשיך ללא הרשאה");
            }

        }
    }


    private void saveDataToPref() {
        saveData(Constants.F_NAME_PREF, binding.fName.getText().toString());
        saveData(Constants.L_NAME_PREF, binding.lName.getText().toString());
        saveData(Constants.PHONE_PREF, binding.phone.getText().toString());
        saveData(Constants.EMAIL_PREF, binding.email.getText().toString());
        saveData(Constants.PASS_PREF, binding.pass.getText().toString());

        saveData(Constants.STREET, binding.street.getText().toString());
        saveData(Constants.CITY, binding.city.getText().toString());
        saveData(Constants.HOME, binding.homeNum.getText().toString());
        saveData(Constants.ENTER, binding.apartmentNum.getText().toString());
        saveData(Constants.T_Z, binding.tz.getText().toString());
    }

    private void initDataFromPref() {
        if (getData(Constants.F_NAME_PREF) != null) {
            binding.fName.setText(getData(Constants.F_NAME_PREF));
        }
        if (getData(Constants.L_NAME_PREF) != null) {
            binding.lName.setText(getData(Constants.L_NAME_PREF));
        }
        if (getData(Constants.PHONE_PREF) != null) {
            binding.phone.setText(getData(Constants.PHONE_PREF));
        }
        if (getData(Constants.STREET) != null) {
            binding.street.setText(getData(Constants.STREET));
        }
        if (getData(Constants.CITY) != null) {
            binding.city.setText(getData(Constants.CITY));
        }

        if (getData(Constants.HOME) != null) {
            binding.homeNum.setText(getData(Constants.HOME));
        }

        if (getData(Constants.ENTER) != null) {
            binding.apartmentNum.setText(getData(Constants.ENTER));
        }
        if (getData(Constants.PASS_PREF) != null) {
            binding.pass.setText(getData(Constants.PASS_PREF));
        }
        if (getData(Constants.EMAIL_PREF) != null) {
            binding.email.setText(getData(Constants.EMAIL_PREF));
        }
        if (getData(Constants.T_Z) != null) {
            binding.tz.setText(getData(Constants.T_Z));
        }
    }

    private String convertBmpToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}
