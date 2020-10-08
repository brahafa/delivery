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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bringit.orders.activities.MainActivity;
import com.bringit.orders.R;
import com.bringit.orders.models.RegistrationModel;
import com.bringit.orders.network.Request;
import com.bringit.orders.utils.Constants;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import static com.bringit.orders.utils.SharedPrefs.getData;
import static com.bringit.orders.utils.SharedPrefs.saveData;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {
    private EditText street, city, homeNum, apartmentNum, pass, confirmPass, email, fName, lName, phone, tz, confirm_num;
    private CheckBox check;
    TextView go, confirm_btn, exist_user;
    View view;
    String base64Self, base64TZ;
    private LinearLayout confirm_number_layout;

    public RegisterFragment() {
        // Required empty public constructor
    }

    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);
        initUI();
        ImageView camera1 = (ImageView) view.findViewById(R.id.take_pic_tz);
        ImageView camera2 = (ImageView) view.findViewById(R.id.take_pic_self);

        camera1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Log.d(",,,", "  camera1  ");
                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    checkPermission(1);
                } else {
                    startActivityForResult(cameraIntent, 1);
                }
            }
        });

        camera2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Log.d(",,,", "  camera2  ");
                //cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    checkPermission(2);
                } else {
                    startActivityForResult(cameraIntent, 2);
                }
            }
        });
        ((MainActivity) getActivity()).setTitle("הרשמה");
        ((MainActivity) getActivity()).setBottomNavigationVisibility(8);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission(int id) {
        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, id);
        }
    }

    private void initUI() {
        go = (TextView) view.findViewById(R.id.go);
        exist_user = (TextView) view.findViewById(R.id.exist_user);
        confirm_btn = (TextView) view.findViewById(R.id.confirm_btn);
        fName = (EditText) view.findViewById(R.id.f_name);
        tz = (EditText) view.findViewById(R.id.tz);
        confirm_num = (EditText) view.findViewById(R.id.confirm_num);
        lName = (EditText) view.findViewById(R.id.l_name);
        phone = (EditText) view.findViewById(R.id.phone);
        confirmPass = (EditText) view.findViewById(R.id.confirm_pass);
        pass = (EditText) view.findViewById(R.id.pass);
        email = (EditText) view.findViewById(R.id.email);
        street = (EditText) view.findViewById(R.id.street);
        city = (EditText) view.findViewById(R.id.city);
        homeNum = (EditText) view.findViewById(R.id.home_num);
        apartmentNum = (EditText) view.findViewById(R.id.apartment_num);
        check = (CheckBox) view.findViewById(R.id.check);
        confirm_number_layout = (LinearLayout) view.findViewById(R.id.confirm_number_layout);

        confirm_btn.setOnClickListener(v -> confirmPhoneServer());

        go.setOnClickListener(v -> {
            saveDataToPref();
            if (checkValidation()) {
                sendProfileToServer();
            }
        });
        exist_user.setOnClickListener(view -> ((MainActivity) getActivity()).openNewFragment(new LogInFragment(), ""));
        initDataFromPref();

    }

    private void confirmPhoneServer() {
        Request.getInstance().confirmUser(mContext, phone.getText().toString(), confirm_num.getText().toString(),
                isDataSuccess -> {
                    if (isDataSuccess) {
                        ((MainActivity) getActivity()).openNewFragment(new TimerFragment(), "");
                    }
                });
    }

    private void sendProfileToServer() {
        JSONObject data = new JSONObject();
        JSONObject address = new JSONObject();

        RegistrationModel registrationModel = new RegistrationModel();

        registrationModel.setName(fName.getText().toString());
        registrationModel.setLastName(lName.getText().toString());
        registrationModel.setPhone(phone.getText().toString());
        registrationModel.setmEmail(email.getText().toString());
        registrationModel.setmIdentity(tz.getText().toString());
        registrationModel.setmPassword(pass.getText().toString());
        registrationModel.setmConfirmPass(confirmPass.getText().toString());

        registrationModel.getAddress().setCityName(city.getText().toString());
        registrationModel.getAddress().setStreet(street.getText().toString());
        registrationModel.getAddress().setHouseNum(homeNum.getText().toString());
        registrationModel.getAddress().setAptNum(apartmentNum.getText().toString());

        registrationModel.setmSelfImage("base64Self");
        registrationModel.setmIdentityImage("base64TZ");
//            data.put("self_image",base64Self);
//            data.put("identity_image",base64TZ);
        Log.d("PARAMS", data.toString());

        Request.getInstance().signUp(mContext, registrationModel,
                isDataSuccess -> {
                    if (isDataSuccess) {
                        ((MainActivity) getActivity()).openNewFragment(new TimerFragment(), "");
                    }
                });
    }

    private boolean checkValidation() {
        if (fName.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא שם פרטי");
            return false;
        }
        if (lName.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא שם משפחה");
            return false;
        }
        if (phone.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא טלפון");
            return false;
        }
        if (tz.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא מס זהות");
            return false;
        }
        if (city.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא עיר");
            return false;
        }
        if (street.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא רחוב");
            return false;
        }
        if (apartmentNum.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא מס בית");
            return false;
        }

        if (pass.getText().toString().equals("")) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא מלא סיסמה");
            return false;
        }
        if (!pass.getText().toString().equals(confirmPass.getText().toString())) {
            Constants.openAlertDialog(getActivity(), "חסרים פרטים", "נא אמת סיסמתך");
            return false;
        }
        if (email.getText().toString().equals("") || !email.getText().toString().contains("@")) {
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
        if (!check.isChecked()) {
            Constants.openAlertDialog(getActivity(), "", "נא אשר את תנאי השימוש");
            return false;
        }
        return true;
    }

    /**
     * The activity returns with the photo.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            ImageView imageView = (ImageView) view.findViewById(R.id.pic_tz);
            Log.d(",,,", "  ssas  ");
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");

            imageView.setImageBitmap(Constants.getResizedBitmap(bitmap, Constants.getScreenWidth(getActivity())));
            base64TZ = convertBmpToBase64(bitmap);
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            ImageView imageView = (ImageView) view.findViewById(R.id.pic_self);
            final Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(Constants.getResizedBitmap(bitmap, Constants.getScreenWidth(getActivity())));
            base64Self = convertBmpToBase64(bitmap);

        } else {
            Toast.makeText(getActivity(), "Image Capture Failed", Toast.LENGTH_SHORT)
                    .show();
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
        saveData(Constants.F_NAME_PREF, fName.getText().toString());
        saveData(Constants.L_NAME_PREF, lName.getText().toString());
        saveData(Constants.PHONE_PREF, phone.getText().toString());
        saveData(Constants.EMAIL_PREF, email.getText().toString());
        saveData(Constants.PASS_PREF, pass.getText().toString());

        saveData(Constants.STREET, street.getText().toString());
        saveData(Constants.CITY, city.getText().toString());
        saveData(Constants.HOME, homeNum.getText().toString());
        saveData(Constants.ENTER, apartmentNum.getText().toString());
        saveData(Constants.T_Z, tz.getText().toString());
    }

    private void initDataFromPref() {
        if (getData(Constants.F_NAME_PREF) != null) {
            fName.setText(getData(Constants.F_NAME_PREF));
        }
        if (getData(Constants.L_NAME_PREF) != null) {
            lName.setText(getData(Constants.L_NAME_PREF));
        }
        if (getData(Constants.PHONE_PREF) != null) {
            phone.setText(getData(Constants.PHONE_PREF));
        }
        if (getData(Constants.STREET) != null) {
            street.setText(getData(Constants.STREET));
        }
        if (getData(Constants.CITY) != null) {
            city.setText(getData(Constants.CITY));
        }

        if (getData(Constants.HOME) != null) {
            homeNum.setText(getData(Constants.HOME));
        }

        if (getData(Constants.ENTER) != null) {
            apartmentNum.setText(getData(Constants.ENTER));
        }
        if (getData(Constants.PASS_PREF) != null) {
            pass.setText(getData(Constants.PASS_PREF));
        }
        if (getData(Constants.EMAIL_PREF) != null) {
            email.setText(getData(Constants.EMAIL_PREF));
        }
        if (getData(Constants.T_Z) != null) {
            tz.setText(getData(Constants.T_Z));
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
