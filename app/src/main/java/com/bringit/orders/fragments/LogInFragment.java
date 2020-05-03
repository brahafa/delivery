package com.bringit.orders.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bringit.orders.MainActivity;
import com.bringit.orders.MyForeGroundService;
import com.bringit.orders.R;
import com.bringit.orders.utils.Constants;
import com.bringit.orders.utils.Network;
import com.bringit.orders.utils.SharePref;
import com.google.firebase.FirebaseApp;

import org.json.JSONException;
import org.json.JSONObject;

public class LogInFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    int test1=1;
    private View view;
    private TextView newUser, go, test, production;
    private EditText phone, password;
    private LinearLayout user_details_layout, connection_layout;
    public LogInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LogInFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LogInFragment newInstance(String param1, String param2) {
        LogInFragment fragment = new LogInFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_log_in, container, false);
        phone = view.findViewById(R.id.phone);
        password = view.findViewById(R.id.password);
        test = view.findViewById(R.id.test);
        production = view.findViewById(R.id.production);
        newUser = view.findViewById(R.id.new_user);
        go = view.findViewById(R.id.go);
        user_details_layout = view.findViewById(R.id.user_details_layout);
        connection_layout = view.findViewById(R.id.connection_layout);



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

        go.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            logIn();
            }
        });
        FirebaseApp.initializeApp(getActivity());



        user_details_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        newUser.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ((MainActivity)getActivity()).openNewFragment(new RegisterFragment(),"");
            }
        });
        ((MainActivity)getActivity()).setTitle("כניסת משתמש");
        ((MainActivity)getActivity()).setBottomNavigationVisibility(8);

        return view;
    }



    private void logIn() {
            JSONObject data = new JSONObject();

            try {
                data.put("phone",phone.getText().toString());
                data.put("password",password.getText().toString());

                Log.d("PARAMS", data.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Network network = new Network(new Network.NetworkCallBack() {
                @Override
                public void onDataDone(Network.RequestName requestName, JSONObject json) {
                    Log.d("LOG_IN", json.toString());
                    try {

                        if(json.getBoolean("status")){
                            Constants.saveLoggedIn(getActivity(),json.getString("utoken"),(json.getJSONObject("user")).getString("id") );
                            ((MainActivity)getActivity()).openNewFragment(new TimerFragment(),"");
                            initPref(json.getJSONObject("user"));
                        }else{
                            Constants.openAlertDialog(getActivity(),"", json.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            },getActivity());
            network.sendPostRequest(getActivity(), data, Network.RequestName.LOG_IN);

    }

    private void initPref(JSONObject user) {
        try {
        SharePref.getInstance(getActivity()).saveData(Constants.F_NAME_PREF, user.getString("name"));
       // SharePref.getInstance(getActivity()).saveData(Constants.L_NAME_PREF, lName.getText().toString());
        SharePref.getInstance(getActivity()).saveData(Constants.PHONE_PREF, user.getString("phone"));
        SharePref.getInstance(getActivity()).saveData(Constants.EMAIL_PREF,user.getString("email"));
        SharePref.getInstance(getActivity()).saveData(Constants.PASS_PREF, password.getText().toString());

        SharePref.getInstance(getActivity()).saveData(Constants.STREET, user.getJSONObject("address").getString("street"));
        SharePref.getInstance(getActivity()).saveData(Constants.CITY,  user.getJSONObject("address").getString("city"));
        SharePref.getInstance(getActivity()).saveData(Constants.HOME, user.getJSONObject("address").getString("houseNumber"));
        SharePref.getInstance(getActivity()).saveData(Constants.ENTER,  user.getJSONObject("address").getString("apartment"));
        SharePref.getInstance(getActivity()).saveData(Constants.T_Z, user.getString("id"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
