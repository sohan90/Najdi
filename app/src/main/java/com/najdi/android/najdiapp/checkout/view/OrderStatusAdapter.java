package com.najdi.android.najdiapp.checkout.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.checkout.model.OrderResponse;
import com.najdi.android.najdiapp.databinding.ItemOrderStatusBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class OrderStatusAdapter extends RecyclerView.Adapter<OrderStatusAdapter.ViewHolder> {

    private List<OrderResponse> list;

    public OrderStatusAdapter(List<OrderResponse> list) {
        this.list = list;
    }

    public void setData(List<OrderResponse> list) {
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
        OrderResponse orderResponse = list.get(position);
        holder.binding.setViewModel(orderResponse);
        String status = orderResponse.getStatus();
        if (orderResponse.getStatus().equals("pending")) {
            status = holder.binding.getRoot().getContext().getString(R.string.pending);
        } else if (orderResponse.getStatus().equals("completed")) {
            status = holder.binding.getRoot().getContext().getString(R.string.completed);
        }
        holder.binding.status.setText(status);
        holder.binding.price.setText(orderResponse.getTotal().concat(" "+holder.binding.getRoot().
                getContext().getString(R.string.currency)));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemOrderStatusBinding binding;

        public ViewHolder(@NonNull ItemOrderStatusBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
