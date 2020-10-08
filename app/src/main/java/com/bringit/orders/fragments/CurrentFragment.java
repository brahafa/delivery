package com.bringit.orders.fragments;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bringit.orders.activities.MainActivity;
import com.bringit.orders.services.MyForeGroundService;
import com.bringit.orders.R;
import com.bringit.orders.adapters.AddressRV;
import com.bringit.orders.models.Address;
import com.bringit.orders.network.Request;
import com.bringit.orders.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.ACTIVITY_SERVICE;

public class CurrentFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PAGE_TYPE = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String pageType;
    private String mParam2;

    private View view;
    private EditText addOrderCode;
    public static LinearLayout orderDatail_layout;
    private TextView addOrder, floor, apartment, enter, pay_type, order_num, pizzaNum, orderTime, comments;
    public TextView address, phone, name, pay, confirmOrderDelivering, dateBtn, date1;
    private RecyclerView recyclerView, order_cart_rv;
    // private ImageView x;

    private Context mContext;


    public CurrentFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CurrentFragment newInstance(String param1, Serializable param2) {
        CurrentFragment fragment = new CurrentFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_TYPE, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageType = getArguments().getString(PAGE_TYPE);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_current, container, false);
        initUI();
        //  getActivity().startService(new Intent(getActivity(), MyLocationService.class));

        initOrderList();

        return view;
    }

    public static void setOrderDatailLayout() {
        orderDatail_layout.setVisibility(View.GONE);
    }

    private void initUI() {
        addOrder = view.findViewById(R.id.add_order);
        addOrderCode = view.findViewById(R.id.add_order_code);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        order_cart_rv = (RecyclerView) view.findViewById(R.id.order_cart_rv);
        orderDatail_layout = (LinearLayout) view.findViewById(R.id.main_layout);
        address = (TextView) view.findViewById(R.id.address);
        name = (TextView) view.findViewById(R.id.name);
        apartment = (TextView) view.findViewById(R.id.apartment);
        enter = (TextView) view.findViewById(R.id.enter);
        floor = (TextView) view.findViewById(R.id.floor);
        order_num = (TextView) view.findViewById(R.id.order_num);
        orderTime = (TextView) view.findViewById(R.id.time);
        pizzaNum = (TextView) view.findViewById(R.id.pizza_num);
        floor = (TextView) view.findViewById(R.id.floor);
        pay_type = (TextView) view.findViewById(R.id.pay_type);
        //x = (LinearLayout) view.findViewById(R.id.x);
        pay = (TextView) view.findViewById(R.id.payment);
        dateBtn = (TextView) view.findViewById(R.id.dateBtn);
        date1 = (TextView) view.findViewById(R.id.date1);
        comments = (TextView) view.findViewById(R.id.comments);
        confirmOrderDelivering = (TextView) view.findViewById(R.id.confirm_order_delivering);
        phone = (TextView) view.findViewById(R.id.phone);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Constants.openAlertDialog(getActivity(), new Constants.DateCallback() {
                    @Override
                    public void onDateChoose(String dateStr) {
                        date1.setText(dateStr);
                    }
                });
            }
        });

        addOrderCode.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_DPAD_CENTER:
                        case KeyEvent.KEYCODE_ENTER:
                            addOrder();
                            return true;
                        default:
                            break;
                    }
                }
                return false;
            }
        });

        addOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrder();
            }
        });
//        x.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                orderDatail_layout.setVisibility(View.GONE);
//            }
//        });

        if (pageType.equals(Constants.FINISH)) {
            addOrder.setVisibility(View.GONE);
            addOrderCode.setVisibility(View.GONE);
            confirmOrderDelivering.setVisibility(View.GONE);
            ((MainActivity) getActivity()).setTitle("הסטוריה");

        } else {
            ((MainActivity) getActivity()).setTitle("הזמנות");
        }
        ((MainActivity) getActivity()).setBottomNavigationVisibility(1);
    }

    private boolean isServiceRunning(String serviceName) {
        boolean serviceRunning = false;
        ActivityManager am = (ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> l = am.getRunningServices(50);
        Iterator<ActivityManager.RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningServiceInfo runningServiceInfo = i
                    .next();

            if (runningServiceInfo.service.getClassName().equals(serviceName)) {
                serviceRunning = true;

                if (runningServiceInfo.foreground) {
                    //service run in foreground
                }
            }
        }
        return serviceRunning;
    }

    //init list of address no
    private void initOrderList() {
        Request.getInstance().getDeliveryOrders(mContext, pageType, json -> {
            try {
                if (!json.getBoolean("status")) {
//                        if(isServiceRunning("MyForeGroundService")){
//                            startService(MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
//                        }
                    if (!pageType.equals(Constants.FINISH)) {
                        stopService();
                    }

                    initRV(new ArrayList<Address>());
                } else {
                    if (!pageType.equals(Constants.FINISH)) {
                        startService();
                    }
                    List<Address> addresses = new ArrayList<>();
                    Gson gson = new Gson();
                    JSONArray jsonArray = json.getJSONArray("orders");
                    StringBuilder orderId = new StringBuilder();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        addresses.add(gson.fromJson(jsonArray.getString(i), Address.class));
                        orderId.append(addresses.get(i).getOrder_id()).append(",");
                    }
                    //    SharePref.getInstance(getActivity()).saveData("id", orderId.toString());
                    //    initAllOrdersList(addresses);
                    initRV(addresses);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }

    // init RV data
    private void initRV(List<Address> orders) {
        AddressRV mAdapter = new AddressRV(orders, pageType, getActivity(), new AddressRV.AdapterCallback() {
            @Override
            public void onItemChoose(Address order) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("param2", order);
                bundle.putString("param1", pageType);
                OrderDetailsFragment orderDetailsFragment = new OrderDetailsFragment();
                orderDetailsFragment.setArguments(bundle);
                ((MainActivity) getActivity()).openNewFragment(orderDetailsFragment, "currentFragment");

            }
        });

        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private void addOrder() {
        Request.getInstance().addOrder(mContext, addOrderCode.getText().toString(),
                isDataSuccess -> {
                    if (isDataSuccess) startService();
                    initOrderList();
                    addOrderCode.setText("");
                });
    }

    private void stopService() {
        if (isServiceRunning("com.bringit.orders.services.MyForeGroundService")) {
            Intent intent = new Intent(getActivity(), MyForeGroundService.class);
            intent.setAction(MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getActivity().startForegroundService(intent);
                Log.d("START333", MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
            } else {
                getActivity().startService(intent);
                Log.d("START222", MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);

            }
        }
    }

    private void startService() {
        if (!isServiceRunning("com.bringit.orders.services.MyForeGroundService")) {
            Intent intent = new Intent(getActivity(), MyForeGroundService.class);
            intent.setAction(MyForeGroundService.ACTION_START_FOREGROUND_SERVICE);
            // getActivity().startService(intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                getActivity().startForegroundService(intent);
                Log.d("START111", MyForeGroundService.ACTION_START_FOREGROUND_SERVICE);
            } else {
                getActivity().startService(intent);
                Log.d("START000", MyForeGroundService.ACTION_START_FOREGROUND_SERVICE);

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // put your code here...
        Log.d("onResume", "onResume");
        //  initOrderList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}
