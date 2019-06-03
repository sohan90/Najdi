package com.najdi.android.najdiapp.shoppingcart.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.databinding.ItemCartBinding;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.viewmodel.CartViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final CartViewModel cartViewModel;
    private List<CartResponse.CartData> cartDataList;

    public CartAdapter(CartViewModel cartViewModel, List<CartResponse.CartData> cartDataList) {
        this.cartViewModel = cartViewModel;
        this.cartDataList = cartDataList;
    }

    public void setData(List<CartResponse.CartData> cartDataList) {
        this.cartDataList = cartDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemCartBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.item_cart,
                parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartResponse.CartData cartData = cartDataList.get(position);
        holder.cartBinding.setCartModel(cartData);
    }

    @Override
    public int getItemCount() {
        return cartDataList == null ? 0 : cartDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemCartBinding cartBinding;

        public ViewHolder(@NonNull ItemCartBinding binding) {
            super(binding.getRoot());
            cartBinding = binding;
            cartBinding.dec.setOnClickListener(v -> {
                CartResponse.CartData cartData = cartDataList.get(getAdapterPosition());
                int updatedQuantity = cartData.getQuantity() - 1;
                cartData.setQuantity(updatedQuantity);
                notifyItemChanged(getAdapterPosition());
            });

            cartBinding.increase.setOnClickListener(v -> {
                CartResponse.CartData cartData = cartDataList.get(getAdapterPosition());
                int updatedQuantity = cartData.getQuantity() + 1;
                cartData.setQuantity(updatedQuantity);
                notifyItemChanged(getAdapterPosition());
            });

            cartBinding.delete.setOnClickListener(v -> {
                CartResponse.CartData cartData = cartDataList.get(getAdapterPosition());
                String cartItemKey = cartData.getTm_cart_item_key();
                cartViewModel.getCartItemKeyLiveData().setValue(cartItemKey);
                notifyItemRemoved(getAdapterPosition());
            });
        }
    }
}
