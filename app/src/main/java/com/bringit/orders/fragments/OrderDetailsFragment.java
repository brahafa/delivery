package com.bringit.orders.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bringit.orders.adapters.ProductsAdapter;
import com.bringit.orders.databinding.FragmentOrderDetailsBinding;
import com.bringit.orders.models.Address;
import com.bringit.orders.models.ItemModel;
import com.bringit.orders.models.OrderDetailsModel;
import com.bringit.orders.network.Request;
import com.bringit.orders.utils.Constants;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Locale;

import static java.lang.String.format;


public class OrderDetailsFragment extends Fragment {

    private FragmentOrderDetailsBinding binding;

    private Context mContext;

    private Address mOrderItem;
    private String pageType;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageType = OrderDetailsFragmentArgs.fromBundle(getArguments()).getPageType();
        mOrderItem = OrderDetailsFragmentArgs.fromBundle(getArguments()).getOrderItem();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false);

        initUI();

        getDetails(mOrderItem.getOrderId());

        return binding.getRoot();
    }

    private void initUI() {
        binding.confirmOrderDelivering.setVisibility(pageType.equals(Constants.FINISHED) ? View.GONE : View.VISIBLE);

        binding.ivOpenProducts.setOnClickListener(view -> {
            binding.ivOpenProducts.setRotation(binding.ivOpenProducts.getRotation() + 180);
            binding.orderCartRv.setVisibility(binding.orderCartRv.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
        binding.x.setOnClickListener(view -> getActivity().onBackPressed());

        binding.confirmOrderDelivering.setOnClickListener(v ->
                Request.getInstance().confirmOrderDelivery(mContext, mOrderItem.getOrderId(),
                        isDataSuccess -> {
                            if (isDataSuccess) getActivity().onBackPressed();
                            Log.d("CONFIRM_DELIVERING", String.valueOf(isDataSuccess));
                        }));

    }

    private void getDetails(String orderId) {
        Request.getInstance().getOrderDetailsByID(mContext, orderId, this::initOrderList);
    }

    //init list of address no
    private void initOrderList(OrderDetailsModel order) {
        initRVCart(order.getProducts());

        binding.address.setText(format("%s  %s  %s  ",
                "אשדוד",// order.getClient().getAddress().getCity(), //fixme get City name when works on server
                order.getClient().getAddress().getStreet(),
                order.getClient().getAddress().getHouseNum()));
        binding.name.setText(order.getClient().getFName());
        binding.phone.setText(order.getClient().getPhone());
        binding.enter.setText(order.getClient().getAddress().getEntrance());
        binding.floor.setText(order.getClient().getAddress().getFloor());
        binding.apartment.setText(order.getClient().getAddress().getHouseNum());
        binding.orderNum.setText(order.getId());
//        binding.pizzaNum.setText(order.getPizza_count());
        binding.time.setText(order.getOrderTime());
        binding.comments.setText(order.getNotes());
        binding.payType.setText(order.getPaymentDisplay());
        binding.deliveryPrice.setText(String.format(Locale.US, "%.2f", Double.parseDouble(order.getDeliveryPrice())));
        binding.payment.setText(String.format(Locale.US, "%.2f", Double.parseDouble(order.getTotalWithDelivery())));

    }

    private void initRVCart(List<ItemModel> products) {
        ProductsAdapter openOrderAdapter = new ProductsAdapter(mContext, products);
        binding.orderCartRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        binding.orderCartRv.setItemAnimator(new DefaultItemAnimator());
        binding.orderCartRv.setAdapter(openOrderAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

}
