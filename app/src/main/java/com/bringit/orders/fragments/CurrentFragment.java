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

import com.bringit.orders.adapters.AddressRV;
import com.bringit.orders.databinding.FragmentCurrentBinding;
import com.bringit.orders.models.Address;
import com.bringit.orders.network.Request;
import com.bringit.orders.services.MyForeGroundService;
import com.bringit.orders.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import static android.content.Context.ACTIVITY_SERVICE;

public class CurrentFragment extends Fragment {

    private FragmentCurrentBinding binding;
    private Context mContext;

    private String pageType;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageType = CurrentFragmentArgs.fromBundle(getArguments()).getPageType();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCurrentBinding.inflate(inflater, container, false);

        initUI();
        //  mContext.startService(new Intent(mContext, MyLocationService.class));

        initOrderList();

        return binding.getRoot();
    }

    private void initUI() {

        binding.rvOrders.setItemAnimator(new DefaultItemAnimator());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1);
        binding.rvOrders.setLayoutManager(gridLayoutManager);

        binding.dateBtn.setOnClickListener(view ->
                Constants.openAlertDialog(getActivity(), dateStr -> binding.date1.setText(dateStr)));

        binding.addOrderCode.setOnKeyListener((v, keyCode, event) -> {
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
        });

        binding.addOrder.setOnClickListener(v -> addOrder());

        if (pageType.equals(Constants.FINISHED)) {
            binding.addOrder.setVisibility(View.GONE);
            binding.addOrderCode.setVisibility(View.GONE);
        }
    }

    private boolean isServiceRunning(String serviceName) {
        boolean serviceRunning = false;
        if (mContext != null) {
            ActivityManager am = (ActivityManager) requireActivity().getSystemService(ACTIVITY_SERVICE);
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
                    if (!pageType.equals(Constants.FINISHED)) {
                        stopService();
                    }

                    initRV(new ArrayList<Address>());
                } else {
                    if (!pageType.equals(Constants.FINISHED)) {
                        startService();
                    }
                    List<Address> addresses = new ArrayList<>();
                    Gson gson = new Gson();
                    JSONArray jsonArray = json.getJSONArray("orders");
                    StringBuilder orderId = new StringBuilder();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        addresses.add(gson.fromJson(jsonArray.getString(i), Address.class));
                        orderId.append(addresses.get(i).getOrderId()).append(",");
                    }
                    //    SharePref.getInstance(mContext).saveData("id", orderId.toString());
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
        AddressRV mAdapter = new AddressRV(orders, pageType, mContext, order -> {
            NavHostFragment.findNavController(this)
                    .navigate(CurrentFragmentDirections.actionCurrentOrdersFragmentToOrderDetailsFragment(order, pageType));
        });

        binding.rvOrders.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }


    private void addOrder() {
        binding.addOrder.setEnabled(false);
        Request.getInstance().addOrder(mContext, binding.addOrderCode.getText().toString(),
                isDataSuccess -> {
                    if (isDataSuccess) startService();
                    initOrderList();
                    binding.addOrderCode.setText("");
                    binding.addOrder.setEnabled(true);
                });
    }

    private void stopService() {
        if (isServiceRunning("com.bringit.orders.services.MyForeGroundService")) {
            Intent intent = new Intent(mContext, MyForeGroundService.class);
            intent.setAction(MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(intent);
                Log.d("START333", MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);
            } else {
                mContext.startService(intent);
                Log.d("START222", MyForeGroundService.ACTION_STOP_FOREGROUND_SERVICE);

            }
        }
    }

    private void startService() {
        if (!isServiceRunning("com.bringit.orders.services.MyForeGroundService")) {
            Intent intent = new Intent(mContext, MyForeGroundService.class);
            intent.setAction(MyForeGroundService.ACTION_START_FOREGROUND_SERVICE);
            // mContext.startService(intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mContext.startForegroundService(intent);
                Log.d("START111", MyForeGroundService.ACTION_START_FOREGROUND_SERVICE);
            } else {
                mContext.startService(intent);
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
