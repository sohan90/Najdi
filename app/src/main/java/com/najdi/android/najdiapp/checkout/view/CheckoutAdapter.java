package com.najdi.android.najdiapp.checkout.view;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.databinding.InflateFragCheckoutBinding;
import com.najdi.android.najdiapp.databinding.InflateVariationItemBinding;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;
import com.najdi.android.najdiapp.shoppingcart.view.CartAdapter;

import java.util.List;
import java.util.Map;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private final CartAdapter.AdapterClickLisntener clickListener;
    private List<CartResponse.CartData> dataList;

    CheckoutAdapter(CartAdapter.AdapterClickLisntener clickListener,
                    List<CartResponse.CartData> cartDataList) {

        this.clickListener = clickListener;
        this.dataList = cartDataList;

    }

    void setDataList(List<CartResponse.CartData> cartDataList) {
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

        if(cartData.getVariation() == null) return;

        for (Map.Entry<String, String> entry : cartData.getVariation().entrySet()) {

            View view = inflater.inflate(R.layout.inflate_variation_item,
                    holder.binding.variationContainer, false);

            InflateVariationItemBinding binding = InflateVariationItemBinding.bind(view);

            String replaceKey = entry.getKey();

            String translatedKey;
            try {
                translatedKey = (String) view.getContext().getResources().
                        getText(view.getContext().getResources().
                                getIdentifier(replaceKey, "string",
                                        view.getContext().getPackageName()));
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

    class ViewHolder extends RecyclerView.ViewHolder {
        InflateFragCheckoutBinding binding;

        ViewHolder(@NonNull InflateFragCheckoutBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.edit.setOnClickListener(v -> clickListener.onEdit(dataList.get(getAdapterPosition())));
            binding.close.setOnClickListener(v -> {

                CartResponse.CartData cartData = dataList.get(getAdapterPosition());
                String cartItemKey = cartData.getId();
                clickListener.onRemoveItem(getAdapterPosition(), cartItemKey);

            });

            binding.increase.setOnClickListener(v -> {
                CartResponse.CartData cartData = dataList.get(getAdapterPosition());
                int updatedQuantity = Integer.parseInt(cartData.getQty()) + 1;
                updateQuantity(updatedQuantity, Integer.parseInt(cartData.getQty()));
            });

            binding.decrease.setOnClickListener(v -> {
                CartResponse.CartData cartData = dataList.get(getAdapterPosition());
                int updatedQuantity = Integer.parseInt(cartData.getQty()) - 1;
                if (updatedQuantity > 0) {
                    updateQuantity(updatedQuantity, Integer.parseInt(cartData.getQty()));
                }
            });
        }

        private void updateQuantity(int updatedQuantity, int previousQuantity) {
            CartResponse.CartData cartData = dataList.get(getAdapterPosition());
            clickListener.onUpdateQuantity(getAdapterPosition(), cartData.getId(),
                    updatedQuantity);
        }
    }
}
