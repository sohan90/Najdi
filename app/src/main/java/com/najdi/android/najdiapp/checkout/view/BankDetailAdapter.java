package com.najdi.android.najdiapp.checkout.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.common.BaseResponse;
import com.najdi.android.najdiapp.databinding.ItemBankAccountsBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BankDetailAdapter extends RecyclerView.Adapter<BankDetailAdapter.ViewHolder> {

    private List<BaseResponse.BankResponse> list;

    public BankDetailAdapter(List<BaseResponse.BankResponse> list) {
        this.list = list;
    }

    public void setData(List<BaseResponse.BankResponse> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemBankAccountsBinding binding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_bank_accounts, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BaseResponse.BankResponse bankResponse = list.get(position);
        holder.setBinding(bankResponse);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ItemBankAccountsBinding binding;

        public ViewHolder(@NonNull ItemBankAccountsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

        public void setBinding(BaseResponse.BankResponse bankResponse) {
            binding.setViewModel(bankResponse);
        }
    }
}
