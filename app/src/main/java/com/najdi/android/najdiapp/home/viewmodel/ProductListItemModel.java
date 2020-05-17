package com.najdi.android.najdiapp.home.viewmodel;

import android.os.Build;
import android.text.Html;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.najdi.android.najdiapp.R;
import com.najdi.android.najdiapp.home.model.ProductListResponse;

public class ProductListItemModel extends BaseObservable {
    private int showDetailButton;
    private ProductListResponse product;

    public ProductListItemModel(ProductListResponse productListResponse, int visibility) {
        this.product = productListResponse;
        this.showDetailButton = visibility;
    }

    @Bindable
    public String getTitle() {
        return product.getName();
    }

    @Bindable
    public String getProductImg() {
        return product.getImage();
    }

    @BindingAdapter("setImageUrl")
    public static void setImageUrl(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).placeholder(R.drawable.najdi_logo).
                into(imageView);
    }

    @Bindable
    public String getPrice() {
        String price;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            price = Html.fromHtml(product.getPrice(), Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            price = Html.fromHtml(product.getPrice()).toString();
        }
        return price;
    }

    @Bindable
    public String getDesc() {
        String desc = "";
        if (product.getDescription() == null) return desc;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            desc = Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_COMPACT).toString();
        } else {
            desc = Html.fromHtml(product.getDescription()).toString();
        }
        return desc;
    }

    @Bindable
    public String getShortDesc(){
        return product.getShortDescription();
    }

    @Bindable
    public boolean isOnSale() {
        return product.isOn_sale();
    }

    @Bindable
    public int getShowDetailButton() {
        return showDetailButton;
    }

    public void bind(ProductListResponse productListResponse) {
        product = productListResponse;
        notifyChange();
    }

}
