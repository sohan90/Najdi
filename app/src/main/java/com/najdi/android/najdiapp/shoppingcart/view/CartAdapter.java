package com.najdi.android.najdiapp.shoppingcart.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.databinding.ItemCartBinding;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private final AdapterClickLisntener clickListener;
    private List<CartResponse.CartData> cartDataList;

    public CartAdapter(AdapterClickLisntener clickListener, List<CartResponse.CartData> cartDataList) {
        this.clickListener = clickListener;
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
                cartData.setPreviousQuantity(cartData.getQuantity());
                int updatedQuantity = cartData.getQuantity() - 1;
                cartData.setQuantity(updatedQuantity);
                notifyItemChanged(getAdapterPosition());
                clickListener.onUpdateQuantity(getAdapterPosition(), cartData.getTm_cart_item_key(),
                        updatedQuantity);
            });

            cartBinding.increase.setOnClickListener(v -> {
                CartResponse.CartData cartData = cartDataList.get(getAdapterPosition());
                cartData.setPreviousQuantity(cartData.getQuantity());
                int updatedQuantity = cartData.getQuantity() + 1;
                cartData.setQuantity(updatedQuantity);
                notifyItemChanged(getAdapterPosition());
                clickListener.onUpdateQuantity(getAdapterPosition(), cartData.getTm_cart_item_key(),
                        updatedQuantity);
            });

            cartBinding.delete.setOnClickListener(v -> {
                CartResponse.CartData cartData = cartDataList.get(getAdapterPosition());
                String cartItemKey = cartData.getTm_cart_item_key();
                clickListener.onRemoveItem(cartItemKey);
                cartDataList.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
            });

            cartBinding.edit.setOnClickListener(v -> {
                CartResponse.CartData cartData = cartDataList.get(getAdapterPosition());
                clickListener.onEdit(cartData);
            });
        }
    }

    public interface AdapterClickLisntener {
        void onRemoveItem(String cartItemKey);

        void onEdit(CartResponse.CartData cartData);

        void onUpdateQuantity(int adapterPosition, String cartItemKey, int quantity);
    }
}
