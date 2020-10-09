package com.bringit.orders.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bringit.orders.R;
import com.bringit.orders.models.ItemModel;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * manage the cart list
 */

public class CartRV extends RecyclerView.Adapter<CartRV.CartHolder> {

    private List<ItemModel> itemList;
    private Context context;

    class CartHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private View view;
        private ImageView halfRight, halfLeft, tr, tl, bl, br, allPizza;
        private RelativeLayout toppingImage;
        private LinearLayout layout2, layout4;

        CartHolder(View view) {
            super(view);
            this.view = view;
            name = (TextView) view.findViewById(R.id.name);
            toppingImage = (RelativeLayout) view.findViewById(R.id.topping_image);
            halfRight = (ImageView) view.findViewById(R.id.pizza_half_right);
            halfLeft = (ImageView) view.findViewById(R.id.pizza_half_left);
            tl = (ImageView) view.findViewById(R.id.tl);
            br = (ImageView) view.findViewById(R.id.br);
            bl = (ImageView) view.findViewById(R.id.bl);
            tr = (ImageView) view.findViewById(R.id.tr);
            layout2 = (LinearLayout) view.findViewById(R.id.layout2);
            layout4 = (LinearLayout) view.findViewById(R.id.layout4);
            allPizza = (ImageView) view.findViewById(R.id.allPizza);
        }
    }

    public CartRV(List<ItemModel> products, Context context) {
        this.itemList = products;
        this.context = context;
    }

    @Override
    public CartRV.CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new CartRV.CartHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartRV.CartHolder holder, final int position) {
        final ItemModel item = itemList.get(position);
        //holder.name.setText(globalObj.getName());
        steDealText(holder, item);
//        if (item.getToppingLocation() != null) {
//            setRedTopping(holder, item);
//        }
        //remove item from cart
        initUi(holder, item);
    }

    //paint the current topping location on the pizza
//    private void setRedTopping(CartRV.CartHolder holder, ItemModel topping) {
//        holder.layout2.setVisibility(View.GONE);
//        holder.layout4.setVisibility(View.GONE);
//        holder.allPizza.setVisibility(View.GONE);
//        if (topping.getToppingLocation().equals("tl") || topping.getToppingLocation().equals("tr") || topping.getToppingLocation().equals("bl") || topping.getToppingLocation().equals("br")) {
//            holder.layout4.setVisibility(View.VISIBLE);
//        } else if (topping.getToppingLocation().equals("leftHalfPizza") || (topping.getToppingLocation().equals("rightHalfPizza"))) {
//            holder.layout2.setVisibility(View.VISIBLE);
//        } else {
//            holder.allPizza.setVisibility(View.VISIBLE);
//        }
//        if (topping.getToppingLocation().equals("tl")) {
//            holder.tl.setImageResource(R.mipmap.redquarterpizza14ccart);
//
//        } else {
//            holder.tl.setImageResource(R.mipmap.whitequarterpizza14ccart);
//        }
//
//        if (topping.getToppingLocation().equals("tr")) {
//            holder.tr.setImageResource(R.mipmap.redquarterpizza14acart);
//        } else {
//            holder.tr.setImageResource(R.mipmap.whitequarterpizza14acart);
//        }
//
//        if (topping.getToppingLocation().equals("br")) {
//            holder.br.setImageResource(R.mipmap.redquarterpizza14bcart);
//
//        } else {
//            holder.br.setImageResource(R.mipmap.whitequarterpizza14bcart);
//        }
//
//        if (topping.getToppingLocation().equals("bl")) {
//            holder.bl.setImageResource(R.mipmap.redquarterpizza14dcart);
//        } else {
//            holder.bl.setImageResource(R.mipmap.whitequarterpizza14dcart);
//        }
//
//        if (topping.getToppingLocation().equals("leftHalfPizza")) {
//            holder.halfLeft.setImageResource(R.mipmap.redhalfpizzaleft14cart);
//        } else {
//            holder.halfLeft.setImageResource(R.mipmap.whitehalfpizzaleft14cart);
//        }
//
//        if (topping.getToppingLocation().equals("rightHalfPizza")) {
//            holder.halfRight.setImageResource(R.mipmap.redhalfpizzaright14cart);
//        } else {
//            holder.halfRight.setImageResource(R.mipmap.whitehalfpizzaright14cart);
//        }
//
//        if (topping.getToppingLocation().equals("full") || topping.getToppingLocation().equals("special")) {
//            holder.allPizza.setImageResource(R.mipmap.redcircle14cart);
//        } else {
//            holder.allPizza.setImageResource(R.mipmap.whitecircle14cart);
//        }
//    }


    private void initUi(CartHolder holder, ItemModel item) {
        if (item.getTypeName().equals("Topping")) {
            holder.toppingImage.setVisibility(View.VISIBLE);
        } else {
            holder.toppingImage.setVisibility(View.GONE);
        }
    }

    private void steDealText(CartHolder holder, ItemModel item) {
        String name = item.getName();
        holder.name.setText(name);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void swap(List<ItemModel> list) {
        if (itemList != null) {
            itemList.clear();
            itemList.addAll(list);
        } else {
            itemList = list;
        }
        notifyDataSetChanged();
    }

    public interface AdapterCallback {
        void onItemChoose(ItemModel item);
    }

}
