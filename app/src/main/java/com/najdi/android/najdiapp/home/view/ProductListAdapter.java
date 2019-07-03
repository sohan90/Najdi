package com.najdi.android.najdiapp.home.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.databinding.ItemProductBinding;
import com.najdi.android.najdiapp.home.model.ProductDetailBundleModel;
import com.najdi.android.najdiapp.home.model.ProductListResponse;
import com.najdi.android.najdiapp.home.viewmodel.HomeScreenViewModel;
import com.najdi.android.najdiapp.home.viewmodel.ProductListItemModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {

    private final HomeScreenViewModel homeScreenViewModel;
    private List<ProductListResponse> list;

    public ProductListAdapter(HomeScreenViewModel viewModel, List<ProductListResponse> list) {
        this.homeScreenViewModel = viewModel;
        this.list = list;
    }

    public void setData(List<ProductListResponse> listResponses) {
        this.list = listResponses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemProductBinding binding = DataBindingUtil.inflate(inflater, R.layout.item_product,
                parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductListResponse listResponse = list.get(position);
        holder.bind(listResponse);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemProductBinding binding;

        public ViewHolder(@NonNull ItemProductBinding itemView) {
            super(itemView.getRoot());
            this.binding = itemView;
            binding.parent.setOnClickListener((v -> {
                ProductListResponse productListResponse = list.get(getAdapterPosition());
                ProductDetailBundleModel model = new ProductDetailBundleModel();
                model.setProductId(productListResponse.getId());
                homeScreenViewModel.getLaunchProductDetailLiveData().setValue(model);
            }));

        }

        public void bind(ProductListResponse productListResponse) {
            if (binding.getViewModel() == null) {
                binding.setViewModel(new ProductListItemModel(productListResponse, View.VISIBLE));
            } else {
                binding.getViewModel().bind(productListResponse);
            }
        }
    }
}
