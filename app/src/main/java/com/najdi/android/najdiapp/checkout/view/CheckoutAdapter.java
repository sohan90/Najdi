package com.najdi.android.najdiapp.checkout.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.GenericClickListener;
import com.najdi.android.najdiapp.databinding.InflateFragCheckoutBinding;
import com.najdi.android.najdiapp.databinding.InflateVariationItemBinding;
import com.najdi.android.najdiapp.shoppingcart.model.CartResponse;

import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder> {

    private final OnItemClickListener clickListener;
    private List<CartResponse.CartData> dataList;

    public CheckoutAdapter(OnItemClickListener clickListener,
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
        holder.binding.edit.setOnClickListener(v -> clickListener.onEdit(cartData));
        holder.binding.close.setOnClickListener(v -> clickListener.onClose(cartData));

        LayoutInflater inflater = LayoutInflater.from(holder.binding.getRoot().getContext());
        for (Map.Entry<String, String> entry : cartData.getVariation().entrySet()) {
            View view = inflater.inflate(R.layout.inflate_variation_item,
                    holder.binding.variationContainer, false);
            InflateVariationItemBinding binding = InflateVariationItemBinding.bind(view);
            String replaceKey = entry.getKey().replace("attribute_pa_", "");
            binding.label.setText(replaceKey);
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
        }
    }

    public interface OnItemClickListener{
        void onEdit(CartResponse.CartData cartData);
        void onClose(CartResponse.CartData cartData);
    }

}
