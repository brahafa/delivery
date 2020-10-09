package com.bringit.orders.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bringit.orders.R;
import com.bringit.orders.models.ItemModel;
import com.bringit.orders.models.OrderCategoryModel;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CategoriesDetailsAdapter extends RecyclerView.Adapter<CategoriesDetailsAdapter.CategoriesDetailsAdapterRvHolder> {

    private List<OrderCategoryModel> itemList;
    private Context context;
    private final String shape;

    class CategoriesDetailsAdapterRvHolder extends RecyclerView.ViewHolder {
        TextView name;
        RecyclerView toppingsRv;
        RecyclerView layersRv;

        CategoriesDetailsAdapterRvHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            toppingsRv = view.findViewById(R.id.rvTopping);
            layersRv = view.findViewById(R.id.rvLayers);
        }

    }


    public CategoriesDetailsAdapter(Context context, List<OrderCategoryModel> itemList, String shape) {
        this.itemList = itemList;
        this.context = context;
        this.shape = shape;
    }


    @Override
    public CategoriesDetailsAdapterRvHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false);
        return new CategoriesDetailsAdapterRvHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final CategoriesDetailsAdapterRvHolder holder, final int position) {
        OrderCategoryModel item = itemList.get(position);

        holder.name.setText(item.getName());

        if (item.getProducts().size() != 0)
            initRV(item.getProducts(), shape, item.isToppingDivided(), holder.toppingsRv);

    }

    private void initRV(final List<ItemModel> orderModels, String shape, boolean isToppingDivided, RecyclerView recyclerView) {
        recyclerView.setVisibility(View.VISIBLE);
        ProductsAdapter openOrderAdapter = new ProductsAdapter(context, orderModels, shape, isToppingDivided);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                context, LinearLayoutManager.VERTICAL, false));

        recyclerView.setAdapter(openOrderAdapter);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

}
