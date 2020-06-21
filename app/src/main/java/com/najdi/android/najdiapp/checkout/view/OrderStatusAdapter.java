package com.najdi.android.najdiapp.checkout.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.model.OrderStatus;
import com.najdi.android.najdiapp.databinding.ItemOrderStatusBinding;

import java.util.List;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {

    private List<OrderStatus.Detail> list;

    OrderStatusAdapter(List<OrderStatus.Detail> list) {
        this.list = list;
    }

    public void setData(List<OrderStatus.Detail> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemOrderStatusBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.item_order_status, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderStatus.Detail orderStatus = list.get(position);
        holder.binding.setViewModel(orderStatus);
       /* String status = orderStatus.getOrderStatusLabel();
        if (orderStatus.getOrderStatusLabel().equals("pending")) {
            status = holder.binding.getRoot().getContext().getString(R.string.pending);
        } else if (orderStatus.getOrderStatusLabel().equals("completed")) {
            status = holder.binding.getRoot().getContext().getString(R.string.completed);
        }
        holder.binding.status.setText(status);*/
        holder.binding.price.setText(orderStatus.getSub_total().concat(" " + holder.binding.getRoot().
                getContext().getString(R.string.currency)));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemOrderStatusBinding binding;

        ViewHolder(@NonNull ItemOrderStatusBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
