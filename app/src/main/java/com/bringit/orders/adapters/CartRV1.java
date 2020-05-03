package com.bringit.orders.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bringit.orders.R;
import com.bringit.orders.models.GlobalObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 11/09/2017.
 */

public class CartRV1 extends RecyclerView.Adapter<CartRV1.CartHolder> {

    private List<GlobalObj> globalObjList,allglobalObjs;
    private Context context;
    private AdapterCallback adapterCallback;
//    private GridLayoutManager gridLayoutManager;


    class CartHolder extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView recyclerView;
        View view, bottomLine;
        ImageView halfRight,halfLeft,tr, tl, bl, br, allPizza;
        RelativeLayout toppingImage;
        LinearLayout layout2, layout4,main_layout;

        CartHolder(View view) {
            super(view);
            this.view=view;
            name= (TextView) view.findViewById(R.id.name);
            toppingImage= (RelativeLayout) view.findViewById(R.id.topping_image);
            //  bottomLine= view.findViewById(R.id.bottom_line);
            halfRight= (ImageView) view.findViewById(R.id.pizza_half_right);

            //gridLayoutManager  = new GridLayoutManager(((MainActivity)context), 1);
            recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            halfLeft= (ImageView) view.findViewById(R.id.pizza_half_left);
            tl= (ImageView) view.findViewById(R.id.tl);
            br= (ImageView) view.findViewById(R.id.br);
            bl= (ImageView) view.findViewById(R.id.bl);
            tr= (ImageView) view.findViewById(R.id.tr);
            layout2= (LinearLayout) view.findViewById(R.id.layout2);
            layout4= (LinearLayout) view.findViewById(R.id.layout4);
            main_layout= (LinearLayout) view.findViewById(R.id.main_layout);
            allPizza= (ImageView) view.findViewById(R.id.allPizza);
        }
    }

    public CartRV1(List<GlobalObj> globalObjs,List<GlobalObj> allglobalObjs, Context context, AdapterCallback adapterCallback) {
        this.globalObjList = globalObjs;
        this.allglobalObjs = allglobalObjs;
        this.adapterCallback= adapterCallback;
        this.context=context;
    }

    @Override
    public CartRV1.CartHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        return new CartRV1.CartHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartRV1.CartHolder holder, final int position) {
        final GlobalObj globalObj = globalObjList.get(position);
        //holder.name.setText(globalObj.getName());
        setPrice(holder,globalObj);
        steDealText(holder,globalObj);
        if(globalObj.getToppingLocation()!=null){
            setRedTopping(holder, globalObj);
        }
        //  if(holder.main_layout!=null)
        if(globalObj.getFather_id()==null) {
            initRV1(holder, globalObj);
          //  holder.main_layout.setBackground(context.getResources().getDrawable(R.drawable.cart_item));
            holder.recyclerView.setVisibility(View.VISIBLE);
            holder.main_layout.setPadding(10,0,10,0);
        }else{
            holder.main_layout.setBackgroundColor(context.getResources().getColor(R.color.opacity));
            holder.recyclerView.setVisibility(View.GONE);
            holder.main_layout.setPadding(0,0,0,0);

        }
        initUi(holder, globalObj);
    }
    private void initRV1(CartHolder holder, GlobalObj globalObj) {
        final List<GlobalObj> list1 = new ArrayList<>();
        for(int i=0; i<allglobalObjs.size() ; i++){
            if( allglobalObjs.get(i).getFather_id()!=null&& globalObj.getCart_id()!=null && allglobalObjs.get(i).getFather_id().equals(globalObj.getCart_id())){
                list1.add(allglobalObjs.get(i));
//                if(list1.get(list1.size()-1).getObject_type().equals("Food")){
//                    for(int j=0; j<allglobalObjs.size(); j++){
//                        //  if(allglobalObjs.get(j).getFather_id()!=null && allglobalObjs.get(j).getFather_id().equals(list1.get(list1.size()-1).getCart_id())){
//                        if(allglobalObjs.get(j).getFather_id()!=null && allglobalObjs.get(j).getFather_id().equals(allglobalObjs.get(i).getCart_id())){
//                            list1.add(allglobalObjs.get(j));
//                        }
//                    }
//                }
            }
        }
        CartRV1 mAdapter = new CartRV1(list1, allglobalObjs , context, new CartRV1.AdapterCallback() {
            @Override
            public void onItemChoose(GlobalObj globalObj) {
                //    CartFragment.removeFood(list,globalObj);
                adapterCallback.onItemChoose(globalObj);
            }
        });
        holder.recyclerView.setLayoutManager( new LinearLayoutManager(context));
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    private void setRedTopping(CartRV1.CartHolder holder, GlobalObj topping) {
        holder.layout2.setVisibility(View.GONE);
        holder.layout4.setVisibility(View.GONE);
        holder.allPizza.setVisibility(View.GONE);
        if (topping.getToppingLocation().equals("tl") || topping.getToppingLocation().equals("tr") || topping.getToppingLocation().equals("bl") || topping.getToppingLocation().equals("br")){
            holder.layout4.setVisibility(View.VISIBLE);
        }else if(topping.getToppingLocation().equals("leftHalfPizza") ||  (topping.getToppingLocation().equals("rightHalfPizza"))){
            holder.layout2.setVisibility(View.VISIBLE);
        }else{
            holder.allPizza.setVisibility(View.VISIBLE);
        }
        if (topping.getToppingLocation().equals("tl")) {
            holder.tl.setImageResource(R.mipmap.redquarterpizza14ccart);

        }else{
            holder.tl.setImageResource(R.mipmap.whitequarterpizza14ccart);
        }

        if (topping.getToppingLocation().equals("tr")) {
            holder.tr.setImageResource(R.mipmap.redquarterpizza14acart);
        }else{
            holder.tr.setImageResource(R.mipmap.whitequarterpizza14acart);
        }

        if (topping.getToppingLocation().equals("br")) {
            holder.br.setImageResource(R.mipmap.redquarterpizza14bcart);

        }else{
            holder.br.setImageResource(R.mipmap.whitequarterpizza14bcart);
        }

        if (topping.getToppingLocation().equals("bl")) {
            holder.bl.setImageResource(R.mipmap.redquarterpizza14dcart);
        }else{
            holder.bl.setImageResource(R.mipmap.whitequarterpizza14dcart);
        }

        if (topping.getToppingLocation().equals("leftHalfPizza")) {
            holder.halfLeft.setImageResource(R.mipmap.redhalfpizzaleft14cart);
        }else{
            holder.halfLeft.setImageResource(R.mipmap.whitehalfpizzaleft14cart);
        }

        if (topping.getToppingLocation().equals("rightHalfPizza")) {
            holder.halfRight.setImageResource(R.mipmap.redhalfpizzaright14cart);
        }else{
            holder.halfRight.setImageResource(R.mipmap.whitehalfpizzaright14cart);
        }

        if (topping.getToppingLocation().equals("full") || topping.getToppingLocation().equals("special") ) {
            holder.allPizza.setImageResource(R.mipmap.redcircle14cart);
        }else{
            holder.allPizza.setImageResource(R.mipmap.whitecircle14cart);
        }
    }


    private void setPrice(CartHolder holder, GlobalObj globalObj) {

    }

    private void initUi(CartHolder holder, GlobalObj globalObj) {

        if(globalObj.getItem_type().equals("Topping")){
            holder.toppingImage.setVisibility(View.VISIBLE);
        }else{
            holder.toppingImage.setVisibility(View.GONE);
        }

    }

    private void steDealText(CartHolder holder, GlobalObj globalObj) {
        String name=globalObj.getItem_name();
        if(globalObj.getItem_name().contains("+")) {
            name = globalObj.getItem_name().replace("+", "\n+");
        }
        holder.name.setText(name);
    }
    @Override
    public int getItemCount() {
        return globalObjList.size();
    }

    public void swap(List<GlobalObj> list, List<GlobalObj> allItems){
        if (globalObjList != null) {
            globalObjList.clear();
            globalObjList.addAll(list);
        }
        else {
            globalObjList = list;
        }
        if(allItems!=null) {
            allglobalObjs.clear();
            allglobalObjs.addAll(allItems);
        }else{

        }
        notifyDataSetChanged();
    }

    public interface AdapterCallback {
        void onItemChoose(GlobalObj globalObj);
    }

}
