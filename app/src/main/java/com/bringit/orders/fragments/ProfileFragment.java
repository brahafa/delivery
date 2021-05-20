package com.bringit.orders.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bringit.orders.activities.MainActivity;
import com.bringit.orders.R;
import com.bringit.orders.utils.Constants;

import androidx.fragment.app.Fragment;

import static com.bringit.orders.utils.SharedPrefs.getData;

public class ProfileFragment extends Fragment {
    private EditText street, city, homeNum, apartmentNum, pass,confirmPass, email,  fName, lName, phone, tz, confirm_num;
    private CheckBox check;
    TextView log_out,confirm_btn;
    View view;
    private LinearLayout confirm_number_layout;
    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_profile, container, false);
        initUI();
        ((MainActivity)getActivity()).setTitle("פרטים אישיים");
        // Inflate the layout for this fragment
        return view;
    }

    private void initUI() {
        log_out= (TextView) view.findViewById(R.id.log_out);
        confirm_btn= (TextView) view.findViewById(R.id.confirm_btn);
        fName= (EditText)view.findViewById(R.id.f_name);
        tz= (EditText)view.findViewById(R.id.tz);
        confirm_num= (EditText)view.findViewById(R.id.confirm_num);
        lName= (EditText)view.findViewById(R.id.l_name);
        phone= (EditText) view.findViewById(R.id.phone);
        confirmPass= (EditText) view.findViewById(R.id.confirm_pass);
        pass= (EditText) view.findViewById(R.id.pass);
        email= (EditText) view.findViewById(R.id.email);
        street= (EditText) view.findViewById(R.id.street);
        city= (EditText) view.findViewById(R.id.city);
        homeNum= (EditText) view.findViewById(R.id.home_num);
        apartmentNum= (EditText) view.findViewById(R.id.apartment_num);
        check= (CheckBox) view.findViewById(R.id.check);
        confirm_number_layout= (LinearLayout) view.findViewById(R.id.confirm_number_layout);
        initDataFromPref();
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.logOut(getActivity());
            }
        });


    }
    private void initDataFromPref() {
        if(getData(Constants.F_NAME_PREF)!=null){
            fName.setText(getData(Constants.F_NAME_PREF));
        }
        if(getData(Constants.L_NAME_PREF)!=null){
            lName.setText(getData(Constants.L_NAME_PREF));
        }
        if(getData(Constants.PHONE_PREF)!=null){
            phone.setText(getData(Constants.PHONE_PREF));
        }
        if(getData(Constants.STREET)!=null){
            street.setText(getData(Constants.STREET));
        }
        if(getData(Constants.CITY)!=null){
            city.setText(getData(Constants.CITY));
        }

        if(getData(Constants.HOME)!=null){
            homeNum.setText(getData(Constants.HOME));
        }

        if(getData(Constants.ENTER)!=null){
            apartmentNum.setText(getData(Constants.ENTER));
        }
//        if(getData(Constants.PASS_PREF)!=null){
//            pass.setText(getData(Constants.PASS_PREF));
//        }
        if(getData(Constants.EMAIL_PREF)!=null){
            email.setText(getData(Constants.EMAIL_PREF));
        }
        if(getData(Constants.T_Z)!=null){
            tz.setText(getData(Constants.T_Z));
        }
    }


}
