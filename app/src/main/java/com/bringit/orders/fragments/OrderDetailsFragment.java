package com.bringit.orders.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bringit.orders.MainActivity;
import com.bringit.orders.R;
import com.bringit.orders.adapters.CartRV;
import com.bringit.orders.models.Address;
import com.bringit.orders.models.GlobalObj;
import com.bringit.orders.models.Order;
import com.bringit.orders.utils.Constants;
import com.bringit.orders.utils.Network;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;


public class OrderDetailsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PAGE_TYPE = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private Address mParam2;
    private String pageType;
    private View view;
    public static LinearLayout orderDatail_layout;
    private TextView floor, apartment, enter, pay_type, order_num, pizzaNum, orderTime, comments;
    public TextView address, phone, name, pay, confirmOrderDelivering;
    private RecyclerView recyclerView, order_cart_rv;
    private LinearLayout x;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    public OrderDetailsFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static OrderDetailsFragment newInstance(String param1, String param2) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putString(PAGE_TYPE, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pageType = getArguments().getString(PAGE_TYPE);
            mParam2 =(Address) getArguments().getSerializable(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.order_details, container, false);
        getDetails(mParam2);
      //  getActivity().startService(new Intent(getActivity(), MyLocationService.class));

        return view;
    }

    private void getDetails(Address mParam2) {
            final Network network = new Network(new Network.NetworkCallBack() {
                @Override
                public void onDataDone(Network.RequestName requestName, JSONObject json) {
                    Log.d("GET_ORDER_DETAILS_BY_ID", json.toString());
                    Gson gson= new Gson();
                    try {
                        if(!json.getJSONObject("order").getString("address").equals("")) {
                            initUI(gson.fromJson(String.valueOf(json.getString("order")), Order.class));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            },getActivity());
            network.sendRequest(getActivity(), Network.RequestName.GET_ORDER_DETAILS_BY_ID, mParam2.getOrder_id(), "");
//        }
//    }



    }

    private void initUI(Order order) {
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
        x = (LinearLayout) view.findViewById(R.id.x);
        pay = (TextView) view.findViewById(R.id.payment);
        comments = (TextView) view.findViewById(R.id.comments);
        confirmOrderDelivering = (TextView) view.findViewById(R.id.confirm_order_delivering);
        phone = (TextView) view.findViewById(R.id.phone);
      //  recyclerView.setItemAnimator(new DefaultItemAnimator());
       // GridLayoutManager gridLayoutManager  = new GridLayoutManager(getActivity(), 1);
       // recyclerView.setLayoutManager(gridLayoutManager);

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  orderDatail_layout.setVisibility(View.GONE);
                ((MainActivity)getActivity()).popFragment(pageType);
            }
        });

        if(pageType.equals(Constants.FINISH)){
            confirmOrderDelivering.setVisibility(View.GONE);
            ((MainActivity)getActivity()).setTitle("הסטוריה");

        }else {
            ((MainActivity)getActivity()).setTitle("הזמנות");
        }
        ((MainActivity)getActivity()).setBottomNavigationVisibility(1);


        initOrderList(order);
    }
    //init list of address no
    private  void initOrderList(Order order) {
        initRVCart(order.getOrder_items());
        orderDatail_layout.setVisibility(View.VISIBLE);
        address.setText(format("%s  %s  %s  ", order.getAddress().getCity(), order.getAddress().getStreet(), order.getAddress().getHouse_num()));
        name.setText(order.getName());
        phone.setText(order.getPhone());
        enter.setText(order.getAddress().getEntrance());
        floor.setText(order.getAddress().getFloor());
        apartment.setText(order.getAddress().getHouse_num());
        order_num.setText(order.getOrder_id());
        pizzaNum.setText(order.getPizza_count());
        orderTime.setText(order.getTime());
        comments.setText(order.getOrder_notes());
        pay_type.setText(order.getPayment_display());
        pay.setText(String.format("%s", order.getTotal_paid()));
        confirmOrderListener(order);

    }
    private void initRVCart(List<GlobalObj> globalObjs) {

        CartRV mAdapter = new CartRV(globalObjs,getActivity());
        GridLayoutManager gridLayoutManager1  = new GridLayoutManager(getActivity(), 1);
        order_cart_rv.setLayoutManager(gridLayoutManager1);
        order_cart_rv.setItemAnimator(new DefaultItemAnimator());
        order_cart_rv.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void confirmOrderListener(final Order order) {
        confirmOrderDelivering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject data = new JSONObject();
                try {
                    data.put("order_id", order.getOrder_id());
                    Log.d("data", data.toString());
                   // orderDatail_layout.setVisibility(View.GONE);
                    ((MainActivity)getActivity()).popFragment(pageType);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Network network = new Network(new Network.NetworkCallBack() {
                    @Override
                    public void onDataDone(Network.RequestName requestName, JSONObject json) {
                        Log.d("CONFIRM_DELIVERING", json.toString());
                       // initOrderList();

                    }
                }, getActivity());
                network.sendPostRequest(getActivity(), data, Network.RequestName.CONFIRM_ORDER_DELIVERING);
            }
        });
    }






}
