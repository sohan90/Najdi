package com.najdi.android.najdiapp.checkout.view;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.databinding.InflateFragCheckoutBinding;
import com.najdi.android.najdiapp.databinding.InflateVariationItemBinding;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.view.CartAdapter;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private final CartAdapter.AdapterClickLisntener clickListener;
    private List<CartResponse.CartData> dataList;

    public CheckoutAdapter(CartAdapter.AdapterClickLisntener clickListener,
                           List<CartResponse.CartData> cartDataList) {

        this.clickListener = clickListener;
        this.dataList = cartDataList;

    }

    public void setDataList(List<CartResponse.CartData> cartDataList) {
        this.dataList = cartDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        InflateFragCheckoutBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.inflate_frag_checkout, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CartResponse.CartData cartData = dataList.get(position);
        holder.binding.setCartModel(cartData);
        holder.binding.variationContainer.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(holder.binding.getRoot().getContext());
        for (Map.Entry<String, String> entry : cartData.getVariation().entrySet()) {
            if (entry.getKey().equalsIgnoreCase("attribute_pa_cutting-way")) {
                continue;

            }
            View view = inflater.inflate(R.layout.inflate_variation_item,
                    holder.binding.variationContainer, false);

            InflateVariationItemBinding binding = InflateVariationItemBinding.bind(view);

            String replaceKey = entry.getKey().replace("attribute_pa_", "");
            String translatedKey;
            try {
                translatedKey = (String) view.getContext().getResources().
                        getText(view.getContext().getResources().
                                getIdentifier(replaceKey, "string", view.getContext().getPackageName()));
            } catch (Resources.NotFoundException e) {

                translatedKey = replaceKey;
            }

            binding.label.setText(translatedKey);
            binding.value.setText(entry.getValue());
            holder.binding.variationContainer.addView(view);
        }
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        InflateFragCheckoutBinding binding;

        public ViewHolder(@NonNull InflateFragCheckoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.edit.setOnClickListener(v -> clickListener.onEdit(dataList.get(getAdapterPosition())));
            binding.close.setOnClickListener(v -> {

                CartResponse.CartData cartData = dataList.get(getAdapterPosition());
                String cartItemKey = cartData.getTm_cart_item_key();
                clickListener.onRemoveItem(getAdapterPosition(), cartItemKey);
                //dataList.remove(getAdapterPosition());
                //notifyItemRemoved(getAdapterPosition());

            });

            binding.increase.setOnClickListener(v -> {
                CartResponse.CartData cartData = dataList.get(getAdapterPosition());
                int updatedQuantity = cartData.getQuantity() + 1;
                updateQuantity(updatedQuantity, cartData.getQuantity());
            });

            binding.decrease.setOnClickListener(v -> {
                CartResponse.CartData cartData = dataList.get(getAdapterPosition());
                int updatedQuantity = cartData.getQuantity() - 1;
                if (updatedQuantity > 0) {
                    updateQuantity(updatedQuantity, cartData.getQuantity());
                }
            });
        }

        private void updateQuantity(int updatedQuantity, int previousQuantity) {
            CartResponse.CartData cartData = dataList.get(getAdapterPosition());
            int updatedTotal = updatedQuantity * Integer.parseInt(cartData.seletedOptionPrice());
            cartData.setPreviousQuantity(previousQuantity);
            cartData.setQuantity(updatedQuantity);
            cartData.setLine_subtotal(updatedTotal);
            notifyItemChanged(getAdapterPosition());
            clickListener.onUpdateQuantity(getAdapterPosition(), cartData.getTm_cart_item_key(),
                    updatedQuantity);
        }
    }
}
