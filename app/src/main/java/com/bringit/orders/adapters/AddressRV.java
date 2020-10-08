package com.bringit.orders.adapters;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bringit.orders.R;
import com.bringit.orders.activities.MainActivity;
import com.bringit.orders.models.Address;
import com.bringit.orders.utils.Constants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static java.lang.String.format;


public class AddressRV extends RecyclerView.Adapter<AddressRV.CartHolder> {

    private List<Address> addresses;
    private Context context;
    private String pageType;
    private AdapterCallback adapterCallback;

     class CartHolder extends RecyclerView.ViewHolder {
         TextView address;
         View view;
        TextView color, global_date;
        LinearLayout openLayout;
        ImageView  call, waze;

        CartHolder(View view) {
            super(view);
            this.view = view;
            address = (TextView) view.findViewById(R.id.address);
            global_date = (TextView) view.findViewById(R.id.global_date);
            openLayout = view.findViewById(R.id.openLayout);
            call = (ImageView) view.findViewById(R.id.call);
            waze = (ImageView) view.findViewById(R.id.waze);
        }
    }

    public AddressRV(List<Address> addresses, String pageType, Context context, AdapterCallback adapterCallback) {
        this.addresses = addresses;
        this.context = context;
        this.pageType = pageType;
        this.adapterCallback=adapterCallback;
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false);
        return new CartHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartHolder holder,  int position) {
       // final Order product = addresses.get(position);
        final int position1= position;
        holder.address.setText(format("%s  %s  %s  ", addresses.get(position).getCity_name(),  addresses.get(position).getStreet(),  addresses.get(position).getHouse_num()));
        holder.waze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initWaze( addresses.get(holder.getAdapterPosition()));
            }
        });
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCall( addresses.get(holder.getAdapterPosition()));
            }
        });

        if (pageType.equals(Constants.FINISH)) {
            holder.waze.setVisibility(View.GONE);
            holder.call.setVisibility(View.GONE);
        } else {
            holder.waze.setVisibility(View.VISIBLE);
            holder.call.setVisibility(View.VISIBLE);
        }

        holder.global_date.setText(Constants.dateFormatToDisplay( addresses.get(position).getDay()));

            if(position1==0){
                Log.d("position 0", String.valueOf(position1));
                holder.global_date.setVisibility(View.VISIBLE);
            }else if((Constants.stringToDate(addresses.get(position).getDay()).compareTo(Constants.stringToDate(addresses.get(position-1).getDay())))!=0){
                Log.d("position 1", String.valueOf(position1));
                holder.global_date.setVisibility(View.VISIBLE);
            }else{
                Log.d("position else", String.valueOf(position1));
                holder.global_date.setVisibility(View.GONE);
            }
        holder.address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallback.onItemChoose( addresses.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void initCall(Address product) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + product.getPhone()));
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(((MainActivity)context) , new String[]{Manifest.permission.CALL_PHONE},15);
        }
        else
        {
            context.startActivity(intent);
        }
    }

    private void initWaze(Address product) {
        try
        {
            String url =  "https://waze.com/ul?q="+product.getCity_name()+"%20"+product.getHouse_num()+"%20"+product.getStreet();
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
            context.startActivity( intent );
        }
        catch (ActivityNotFoundException ex  )
        {
            // If Waze is not installed, open it in Google Play:
            Intent intent = new Intent( Intent.ACTION_VIEW, Uri.parse( "market://details?id=com.waze" ) );
            context.startActivity(intent);
        }
    }


    @Override
    public int getItemCount() {
        return addresses.size();
    }

    public interface AdapterCallback {
        void onItemChoose(Address orderItem);
    }
}
